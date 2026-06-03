
pipeline {
    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(
                numToKeepStr: '20',
                artifactNumToKeepStr: '20'
        ))
    }

    stages {

        stage('Verify Environment') {
            steps {
                sh '''
                echo "Workspace:"
                pwd

                echo "Files:"
                ls -la

                echo "Docker:"
                docker --version

                docker ps
            '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                docker build \
                  -t api-automation:${BUILD_NUMBER} .
            '''
            }
        }

        stage('Execute Tests') {
            steps {
                sh '''
                mkdir -p build

                docker run --rm \
                  -v ${WORKSPACE}/build:/app/build \
                  --name api-tests-${BUILD_NUMBER} \
                  api-automation:${BUILD_NUMBER}
            '''
            }
        }

        stage('Verify Results') {
            steps {
                sh '''
                echo "===== Test Results ====="
                find build -type f | sort || true

                echo "===== Allure Results ====="
                ls -la build/allure-results || true

                echo "===== JUnit Results ====="
                ls -la build/test-results/test || true
            '''
            }
        }
    }

    post {

        always {

            junit(
                    allowEmptyResults: true,
                    testResults: 'build/test-results/test/*.xml'
            )

            archiveArtifacts(
                    artifacts: 'build/allure-results/**',
                    allowEmptyArchive: true
            )

            archiveArtifacts(
                    artifacts: 'build/reports/**',
                    allowEmptyArchive: true
            )

            publishHTML([
                    allowMissing: true,
                    alwaysLinkToLastBuild: true,
                    keepAll: true,
                    reportDir: 'build/reports/allure-report/allureReport',
                    reportFiles: 'index.html',
                    reportName: 'Allure Report'
            ])

            script {

                if (fileExists('build/allure-results')) {

                    allure([
                            results: [[path: 'build/allure-results']]
                    ])

                } else {

                    echo 'Allure results directory not found'

                }
            }
        }
    }
}