pipeline {
    agent any
    stages {
        stage('Compile') {
            agent {
                docker { image 'gradle' }
            }
            steps {
                sh 'gradle -x test build'
            }
        }
        stage('BuildImage') {
            agent {
                dockerfile {
                    filename '../../../Dockerfile'
                    dir 'browser-api/build/libs'
                    additionalBuildArgs  '-t browser-api-0.6.1'
                }
            }
            steps {
                sh 'echo "Build image completed."'
            }
        }
        stage('Start'){
            agent any
            steps {
                sh 'docker images'
                sh 'docker run -d -p 8061:8061 browser-api-0.6.1'
            }
        }
    }
}
