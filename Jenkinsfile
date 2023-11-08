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
                        sh 'mvn clean test'
                    }
                }
            }            
        }
          stage('Build Frontend') {
            steps {
                dir('DevOps_Project_Front') {
                    script {
                        export PATH="/path/to/node_modules/.bin:$PATH"
                        sh 'npm install'
                        sh 'ng build '
                    }
                }
            }
        }

 

        

        
        

       
    }
}