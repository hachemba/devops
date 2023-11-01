pipeline {
    agent any
    
    stages {
        stage('Github Preperation') {
            steps {
                echo "Getting Project from Git"
                checkout scm
            }
        }

        stage('Run Unit Tests ') {
            steps {
                dir('DevOps_Project') {
                    script {
                        sh 'mvn clean install'
                    }
                }
            }            
        }
        
        

       
    }
}