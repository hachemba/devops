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
               post {
                success {
                    script {
                       // def subject = "Deployment"
                        //def body = "Nexus is good"
                        //def to = 'hachembenarab@gmail.com'

                        mail(
                            subject: "Deployment",
                            body: "Nexus is good",
                            to: "hachembenarab@gmail.com",
                        )
                    }
                }
                failure {
                    script {
                        def subject = "Build Failure - ${currentBuild.fullDisplayName}"
                        def body = "The build has failed in the Jenkins pipeline. Please investigate and take appropriate action."
                        def to = 'hachembenarab@gmail.com'

                        mail(
                            subject: subject,
                            body: body,
                            to: to,
                        )
                    }
                }
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
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
                  //  def imageExists = sh(script: "docker inspect --type=image $dockerImage", returnStatus: true) == 0

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


          
    
        //    stage('Compose grafana and prometheus') {
        //     steps {
        //         dir('DevOps_Project') {

        //         script {
        //             sh 'docker-compose -f docker-compose-prometheus.yml -f docker-compose-grafana.yml up -d'
        //         }
        //         }
        //     }
        // }

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