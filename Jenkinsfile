
pipeline {
    agent any

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

                sh """
            mkdir -p ${WORKSPACE}/results/allure-results
            mkdir -p ${WORKSPACE}/results/test-results

            docker run --rm \
              -v ${WORKSPACE}/results:/results \
              api-automation:${BUILD_NUMBER} \
              test \
              -DsuiteXml=src/test/resources/testng.xml \
              -DexecutionType=${params.EXECUTION_TYPE} \
              -Dbrowser=${params.BROWSER} \
              -Dselenium.grid.url=http://host.docker.internal:4444
            """
            }
        }
    }

    post {

        always {

            script {

                echo "===== Results Directory ====="

                sh '''
            pwd
            ls -lrt
            find results || true
            '''

                junit(
                        allowEmptyResults: true,
                        testResults: 'results/test-results/**/*.xml'
                )

                archiveArtifacts(
                        artifacts: 'results/**',
                        allowEmptyArchive: true
                )

                if (fileExists('results/allure-results')) {

                    echo "Publishing Allure Report..."

                    allure([
                            results: [[path: 'results/allure-results']]
                    ])

                } else {

                    echo 'Allure results directory not found'

                }
            }
        }
    }
}