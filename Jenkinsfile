
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

                sh """
            mkdir -p ${WORKSPACE}/results/allure-results
            mkdir -p ${WORKSPACE}/results/test-results

            docker run --rm \
              -v ${WORKSPACE}/results:/results \
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

    stage('Generate Allure Report') {
        steps {

            script {

                def allureHome = tool 'Allure'

                sh """
            ${allureHome}/bin/allure generate \
                results/allure-results \
                -o allure-report \
                --clean
            """
            }
        }
    }

    post {
        always {

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