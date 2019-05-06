pipeline {
    agent any
    stages {
        stage('BuildImage') {
            agent {
                dockerfile {
                    filename '../../../Dockerfile'
                    dir 'browser-api/build/libs'
                    additionalBuildArgs  '-t browser-api-0.6.1'
                }
            }
            steps {
                sh 'echo "Build docker image completed"'
            }
        }
    }
}
