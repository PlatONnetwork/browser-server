pipeline {
    agent any
    stages {
        stage('BuildImage') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    dir 'browser-api/build/libs'
                }
            }
            steps {
                sh 'echo "Build docker image..."'
            }
        }
    }
}
