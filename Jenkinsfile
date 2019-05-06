pipeline {
    agent any
    stages {
        stage('BuildImage') {
            agent {
                dockerfile {
                    filename 'Dockerfile'
                    dir 'browser-api/build/libs'
                    label 'browser-api'
                }
            }
            steps {
                sh 'echo "Build docker image..."'
            }
        }
    }
}
