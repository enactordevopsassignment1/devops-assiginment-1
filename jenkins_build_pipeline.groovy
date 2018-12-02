pipeline {
    agent any
    stages {
        stage('Checkout and Build simple_web_app') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '58fcf397-774b-4287-ad40-49b88de738fb', url: 'https://github.com/enactordevopsassignment1/devops-assiginment-1.git']]])
                dir('simple_web_app') {
                    // Run the maven build
                    sh 'mvn clean package'
                }
            }
        }
        stage('Checkout Dockerfile, Copy artifact to Doeckerfile directory, Build container, and push to the docker-hub') {
            steps {
                checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '58fcf397-774b-4287-ad40-49b88de738fb', url: 'https://github.com/enactordevopsassignment1/docker-oraclejava8-tomcat8-mysql5.7.git']]])
                sh 'cp simple_web_app/target/simple_web_app.war simple_web_app.war'
                script {
                    def docker_image
                    docker_image = docker.build("madumalt/simple_web_app", "--build-arg web_app_war_file=simple_web_app.war .")

                    /* Finally, we'll push the image with two tags:
                    * First, the incremental build number from Jenkins
                    * Second, the 'latest' tag.
                    * Pushing multiple tags is cheap, as all the layers are reused. */
                    docker.withRegistry('https://registry.hub.docker.com', 'docker-hub-credentials') {
                        docker_image.push("${env.BUILD_NUMBER}")
                        docker_image.push("latest")
                    }
                }
            }
        }
    }
    post {
        failure {
            emailext attachLog: true, body: '''Please see the attached log. Thanks.''', subject: '[Jenkins][simple_web_app] Build Failure', to: 'madumalt@gmail.com'
        }
    }
}