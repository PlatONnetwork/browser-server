pipeline {
    agent {
        docker { image 'gradle' }
    }
    stages {
        stage('build') {
            steps {
                sh 'gradle -x test build'
            }
        }
        stage('deploy') {
            steps {
                sh 'docker build -f ./Dockerfile ./browser-api/build/libs'
            }
        }
    }
}
