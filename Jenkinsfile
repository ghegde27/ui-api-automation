
pipeline {
    agent any

    tools {
        allure 'Allure'
    }

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    parameters {
        choice(
                name: 'BROWSER',
                choices: ['firefox'],
                description: 'Browser'
        )

        choice(
                name: 'EXECUTION_TYPE',
                choices: ['grid'],
                description: 'Execution Type'
        )

        choice(
                name: 'SUITE_FILE',
                choices: ['src/test/resources/testng.xml','src/test/resources/api-testng.xml'],
                description: 'Execution Type'
        )
    }

    stages {
        stage('Clean Workspace') {
            steps {
                cleanWs()
            }
        }

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build Docker Image') {
            steps {
                sh """
                    docker build \
                        -t api-automation:${BUILD_NUMBER} .
                """
            }
        }

        stage('Run UI Tests') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    sh """
                        mkdir -p "${WORKSPACE}/results/allure-results"
                        mkdir -p "${WORKSPACE}/results/test-results"

                        docker run --rm \
                            --add-host=host.docker.internal:host-gateway \
                            -v "${WORKSPACE}/results:/results" \
                            api-automation:${BUILD_NUMBER} \
                            './gradlew test \
                                -DsuiteXml=${params.SUITE_FILE} \
                                -DexecutionType=${params.EXECUTION_TYPE} \
                                -Dbrowser=${params.BROWSER} \
                                -Dselenium.grid.url=http://host.docker.internal:4444; \
                              status=\$?; \
                              mkdir -p /results/allure-results /results/test-results; \
                              cp -R build/allure-results/. /results/allure-results/ 2>/dev/null || true; \
                              cp -R build/test-results/. /results/test-results/ 2>/dev/null || true; \
                              exit \$status'
                    """
                }
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    def allureHome = tool 'Allure'

                    sh """
                        if [ -d results/allure-results ] && [ -n "\$(find results/allure-results -type f -print -quit)" ]; then
                            ${allureHome}/bin/allure generate \
                                results/allure-results \
                                -o allure-report \
                                --clean
                        else
                            mkdir -p allure-report
                            printf '<html><body><h1>No Allure results found</h1></body></html>' > allure-report/index.html
                        fi
                    """
                }
            }
        }
    }

    post {
        always {
            junit allowEmptyResults: true, testResults: 'results/test-results/**/*.xml'

            publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'allure-report',
                    reportFiles: 'index.html',
                    reportName: 'Allure HTML Report'
            ])
        }
    }

}
