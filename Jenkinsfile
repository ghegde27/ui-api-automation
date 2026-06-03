
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

        stage('Run Tests') {
            steps {

                script {

                    sh '''
                mkdir -p results
                '''

                    sh """
                docker run \
                  --name api-tests-${BUILD_NUMBER} \
                  --add-host=host.docker.internal:host-gateway \
                  api-automation:${BUILD_NUMBER} \
                  test \
                  -DsuiteXml=${params.SUITE_FILE} \
                  -DexecutionType=${params.EXECUTION_TYPE} \
                  -Dbrowser=${params.BROWSER} \
                  -Dselenium.grid.url=http://host.docker.internal:4444
                """
                }
            }
        }

        stage('Copy Results From Container') {
            steps {

                sh """
            mkdir -p results

            docker cp \
              api-tests-${BUILD_NUMBER}:/app/build/allure-results \
              results/ || true

            docker cp \
              api-tests-${BUILD_NUMBER}:/app/build/test-results \
              results/ || true

            echo "===== RESULTS ====="

            find results || true
            """
            }
        }

        stage('Generate Allure Report') {
            steps {

                script {

                    def allureHome = tool 'Allure'

                    sh """
                echo "===== ALLURE RESULTS ====="

                find results/allure-results || true

                ${allureHome}/bin/allure generate \
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
            echo "===== FINAL DEBUG ====="

            pwd

            find results || true

            find allure-report || true
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
                        artifacts: 'allure-report/**',
                        allowEmptyArchive: true
                )

                archiveArtifacts(
                        artifacts: 'results/**',
                        allowEmptyArchive: true
                )

                sh """
            docker rm -f api-tests-${BUILD_NUMBER} || true
            """
            }
        }
    }

}
