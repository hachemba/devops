pipeline {
    agent any

    tools{
        nodejs 'nodeJsPackage'
    }

    environment {
     //   PATH = "$PATH:/usr/local/bin"
       DOCKERHUB_CREDENTIALS = credentials('hbaDockerHub')
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
          stage('SonarQube Integration') {
            steps {
                dir('DevOps_Project') {
                    withSonarQubeEnv('sonarqubeVar') {
                        sh 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.9.0.2155:sonar -Dsonar.host.url=http://192.168.33.10:9000 -Dsonar.login=admin -Dsonar.password=sonar'
                    }
                }
            }
        }
        stage('Deployments') {
            steps {
                dir('DevOps_Project') {
                    script {
                 sh 'mvn deploy'
                    }
                }
            }
        }
           stage('Login Docker') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }
          stage('Build & Push Docker Image (Backend)') {
            steps {
                script {
                    def dockerImage = 'hachembenarab/alpine:1.0.0'
                    def imageExists = sh(script: "docker inspect --type=image $dockerImage", returnStatus: true) == 0

                    if (imageExists) {
                        dir('DevOps_Project') {
                            sh "docker build -t $dockerImage ."
                            sh "docker push $dockerImage"
                        }
                    } else {
                        echo "Docker image $dockerImage already exists. Skipping the build and push steps."
                    }
                }
            }
        }
           stage('Compose grafana and prometheus') {
            steps {
                dir('DevOps_Project') {

                script {
                    sh 'docker-compose -f docker-compose-prometheus.yml -f docker-compose-grafana.yml up -d'
                }
                }
            }
        }

         /* stage('Build Frontend') {
            steps {
                dir('DevOps_Project_Front') {
                    script {
//                        export PATH="/path/to/node_modules/.bin:$PATH"
                        sh 'npm install'
                        sh 'ng build '
                    }
                }
            }
        }
*/
 

        

        
        

       
    }
}