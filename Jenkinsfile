pipeline {
    agent any

    

    environment {
     //   PATH = "$PATH:/usr/local/bin"
       DOCKERHUB_CREDENTIALS = credentials('hbaDockerHub')
    }
    
    stages {
        stage('Github Connection') {
            steps {
                echo "Getting Project from Git"
                checkout scm
            }
        }



        stage('Test Backend') {
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
               post {
                success {
                    script {
                      

                        mail(
                            subject: "Build Success - ${currentBuild.fullDisplayName}",
                            body: "Nexus is good",
                            to: "hachembenarab@gmail.com",
                        )
                    }
                }
                failure {
                    script {
                

                        mail(
                            subject: "Build Failure - ${currentBuild.fullDisplayName}",
                            body: "The build has failed in the Jenkins pipeline. Please investigate and take appropriate action.",
                            to: "hachembenarab@gmail.com",
                        )
                    }
                }
                
            }
        }
           stage('Login Docker') {
            steps {
                sh 'echo $DOCKERHUB_CREDENTIALS_PSW | docker login -u $DOCKERHUB_CREDENTIALS_USR --password-stdin'
            }
        }
          stage(' Push Docker Image to DockerHub') {
            steps {
                script {
                    def dockerImage = 'hachembenarab/devops'

                        dir('DevOps_Project') {
                            sh "docker build -t $dockerImage ."
                            sh "docker push $dockerImage"
                        }
                    
                }
            }
        }
        stage('docker compose') {
            steps {
                dir('DevOps_Project') {
                    script {
                        sh 'docker-compose -f docker-compose.yml up -d'
                    }
                }
            }
        }


          
    
        

        
        

       
    }
}