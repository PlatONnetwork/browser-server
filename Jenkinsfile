pipeline {
    agent any
    stages {
        //stage('Compile') {
        //    agent {
        //        docker { image 'gradle' }
        //    }
        //    steps {
        //        sh 'gradle -x test build'
        //    }
        //}
        stage('BuildImage') {
            agent {
                dockerfile {
                    filename '../../../Dockerfile'
                    dir 'browser-api/build/libs'
                    additionalBuildArgs  '-t browser-api-0.6.1'
                }
            }
            steps {
                sh 'docker images'
            }
        }
    }
}
