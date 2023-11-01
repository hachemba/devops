pipeline {
    agent any

    tools{
        nodejs 'nodeJsPackage'
    }
    
    stages {
        stage('GITHUB') {
            steps {
                echo "Getting Project from Git"
                checkout scm
            }
        }

        stage('BUILD + TEST BACKEND') {
            steps {
                dir('DevOps_Project') {
                    script {
                        sh 'mvn clean install'
                    }
                }
            }            
        }
        
        stage('BUILD FRONTEND') {
            steps {
                dir('DevOps_Project_Front') {
                    script {
                        
                        sh 'npm install' 
                        sh 'ng build '      
                    }
                }
            }
        }
        

       
    }
}