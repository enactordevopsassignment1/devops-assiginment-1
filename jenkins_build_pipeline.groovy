pipeline {
    agent any
    stages {
        stage('Checkout simple_web_app') {
            steps {
                retry(3) {
                    checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '58fcf397-774b-4287-ad40-49b88de738fb', url: 'https://github.com/enactordevopsassignment1/devops-assiginment-1.git']]])
                }
            }
        }
        stage('Build and deploy simple_web_app'){
            steps{
                dir('simple_web_app') {
                    // Run the maven build and upload artifacts
                    retry(3){
                        sh 'mvn clean deploy'
                    }
                }
            }
        }
        stage('Build docker container, and push to the docker-hub') {
            steps {
                script {
                    def docker_image = docker.build("madumalt/simple_web_app")
                    retry(3) {
                        docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                            docker_image.push("${env.BUILD_TIMESTAMP}")
                            docker_image.push("latest")
                        }
                    }
                }
            }
        }
    }
    post {
        failure {
            emailext attachLog: true, body: '''Please see the attached log. Thanks.''', subject: '[Jenkins][simple_web_app] Build Failure', to: 'madumalt@gmail.com,thilina.11@cse.mrt.ac.lk'
        }
    }
}