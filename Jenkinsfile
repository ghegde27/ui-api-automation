
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
                choices: [
                        'src/test/resources/testng.xml',
                        'src/test/resources/api-testng.xml'
                ],
                description: 'Suite File'
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

        stage('Prepare Results') {
            steps {
                sh """
                    rm -rf results allure-report
                    mkdir -p results/allure-results results/test-results
                    docker rm -f api-tests-${BUILD_NUMBER} || true
                """
            }
        }

        stage('Run Tests In Docker') {
            steps {
                catchError(buildResult: 'FAILURE', stageResult: 'FAILURE') {
                    sh """
                        docker run \
                            --name api-tests-${BUILD_NUMBER} \
                            --add-host=host.docker.internal:host-gateway \
                            api-automation:${BUILD_NUMBER} \
                            './gradlew clean test \
                                -DsuiteXml=${params.SUITE_FILE} \
                                -DexecutionType=${params.EXECUTION_TYPE} \
                                -Dbrowser=${params.BROWSER} \
                                -Dselenium.grid.url=http://host.docker.internal:4444 \
                                -Dallure.results.directory=build/allure-results'
                    """
                }
            }
        }

        stage('Copy Results To Jenkins') {
            steps {
                sh """
                    mkdir -p results/allure-results results/test-results

                    docker cp api-tests-${BUILD_NUMBER}:/app/build/allure-results/. \
                        results/allure-results/ || true

                    docker cp api-tests-${BUILD_NUMBER}:/app/build/test-results/. \
                        results/test-results/ || true

                    echo "===== Copied results to Jenkins workspace ====="
                    find results -maxdepth 3 -type f | sort || true
                """
            }
        }

        stage('Generate Allure Report') {
            steps {
                script {
                    def allureHome = tool 'Allure'

                    sh """
                        echo "===== Allure raw results ====="
                        find results/allure-results -maxdepth 2 -type f | sort || true

                        if [ ! -d results/allure-results ] || [ -z "\$(find results/allure-results -type f -print -quit)" ]; then
                            echo "No Allure results found under results/allure-results"
                            exit 1
                        fi

                        "${allureHome}/bin/allure" generate \
                            results/allure-results \
                            -o allure-report \
                            --clean
                    """
                }
            }
        }
    }

    post {
        always {
            script {
                sh '''
                    echo "===== Final Jenkins artifacts ====="
                    find results -maxdepth 4 -type f | sort || true
                    find allure-report -maxdepth 4 -type f | sort || true
                '''

                junit(
                        allowEmptyResults: true,
                        testResults: 'results/test-results/**/*.xml'
                )

                publishHTML([
                        allowMissing: true,
                        alwaysLinkToLastBuild: true,
                        keepAll: true,
                        reportDir: 'allure-report',
                        reportFiles: 'index.html',
                        reportName: 'Allure HTML Report'
                ])

                archiveArtifacts(
                        artifacts: 'allure-report/**, results/**',
                        allowEmptyArchive: true
                )

                sh """
                    docker rm -f api-tests-${BUILD_NUMBER} || true
                """
            }
        }
    }
}
