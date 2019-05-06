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
                    filename 'Dockerfile'
                    dir 'browser-api/build/libs'
                    label 'browser-api'
                    additionalBuildArgs  '-t platon:browser-api'
                }
            }
        }
    }
}
