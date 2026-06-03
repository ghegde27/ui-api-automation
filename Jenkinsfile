pipeline {

    agent any

    options {
        timestamps()
    }

    stages {

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

        stage('Execute Tests') {
            steps {
                sh """
                docker run --rm \
                  --name api-tests-${BUILD_NUMBER} \
                  api-automation:${BUILD_NUMBER}
                """
            }
        }
    }

    post {

        always {

            junit allowEmptyResults: true,
                    testResults: 'build/test-results/test/*.xml'

            archiveArtifacts(
                    artifacts: 'build/reports/**',
                    allowEmptyArchive: true
            )

            allure(

                    includeProperties: false,

                    results: [[path: 'build/allure-results']]

            )
        }
    }
}