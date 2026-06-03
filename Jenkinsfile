
pipeline {
    agent any

    options {
        timestamps()
        buildDiscarder(logRotator(numToKeepStr: '20'))
    }

    stages {

        stage('Verify Workspace') {
            steps {
                sh '''
                echo "Current Workspace:"
                pwd

                echo "Files:"
                ls -la

                echo "Docker Version:"
                docker --version

                echo "Docker Containers:"
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
                docker run --rm \
                    --name api-tests-${BUILD_NUMBER} \
                    api-automation:${BUILD_NUMBER}
            '''
            }
        }
    }

    post {

        always {


            archiveArtifacts(
                    artifacts: 'build/allure-results/**',
                    allowEmptyArchive: true
            )

            archiveArtifacts(
                    artifacts: 'build/reports/**',
                    allowEmptyArchive: true
            )

            publishHTML([
                    allowMissing         : true,
                    alwaysLinkToLastBuild: true,
                    keepAll              : true,
                    reportDir            : 'build/reports/allure-report/allureReport',
                    reportFiles          : 'index.html',
                    reportName           : 'Allure Report'
            ])
        }
    }
}