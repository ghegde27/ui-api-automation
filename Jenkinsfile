
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

        stage('Run UI Tests') {
            steps {
                sh """
        mkdir -p ${WORKSPACE}/allure-results

        docker run --rm \
          -v ${WORKSPACE}/allure-results:/app/build/allure-results \
          -v ${WORKSPACE}/test-results:/app/build/test-results \
          api-automation:${BUILD_NUMBER} \
          clean test \
          -DsuiteXml=src/test/resources/testng.xml \
          -DexecutionType=grid \
          -Dbrowser=firefox \
          -Dselenium.grid.url=http://host.docker.internal:4444
        """
            }
        }

        stage('Verify Results') {
            steps {
                sh '''
                echo "===== Test Results ====="
                find build -type f | sort || true

                echo "===== Allure Results ====="
                ls -lrt allure-results

            '''
            }
        }
    }

    post {
        always {

            allure([

                    results: [[path: 'allure-results']]

            ])
        }
    }
}