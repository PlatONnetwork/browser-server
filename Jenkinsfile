pipeline {
    agent any
    stages {
        stage('build') {
            agent {
                docker { image 'gradle' }
            }
            steps {
                sh 'gradle -x test build'
            }
        }
    }
}
