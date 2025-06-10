pipeline {
    agent any

    tools {
        // Adjust tool names to match your Jenkins configuration
        jdk 'jdk21'
        maven 'M3'
    }

    environment {
        // Default to mock mode; override with -Denv=real in Jenkins job if needed
        ENV = 'mock'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                // Compile the code
                sh 'mvn clean compile'
            }
        }

        stage('Test') {
            steps {
                // Run the Cucumber-TestNG suite against WireMock stubs
                sh "mvn test -Denv=${ENV}"
            }
        }

        stage('Archive JUnit Results') {
            steps {
                // Archive TestNG XML reports so Jenkins can display them
                junit 'target/surefire-reports/*.xml'
            }
        }

        stage('Publish Extent Report') {
            steps {
                // Requires the “HTML Publisher Plugin” in Jenkins
                publishHTML([
                    reportDir:    'target/extent-report',
                    reportFiles:  'Spark.html',
                    reportName:   'Extent Report',
                    keepAll:      true,
                    alwaysLinkToLastBuild: true
                ])
            }
        }
    }

    post {
        always {
            // In case you want to capture logs or cleanup
            echo "Build completed (status: ${currentBuild.currentResult})"
        }
    }
}
