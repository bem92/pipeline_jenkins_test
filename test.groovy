pipeline {

 // Some global default variables
    environment {
        MAVEN_IMAGE_NAME = 'mymaven'
        TOMCAT_IMAGE_NAME = 'mytomcat'
        TEST_LOCAL_PORT = 8888
    }

    parameters {
        string (name: 'GIT_BRANCH',       defaultValue: 'master',                               description: 'Git branch to build')
        string (name: 'DOCKER_REG',       defaultValue: 'docker-registry.my',                   description: 'Docker registry')
        string (name: 'DOCKER_TAG',       defaultValue: 'v2.0',                                 description: 'Docker tag')
        string (name: 'DOCKER_USR',       defaultValue: 'admin',                                description: 'Your docker repository user')
        string (name: 'DOCKER_PSW',       defaultValue: 'password',                             description: 'Your docker repository password')
    }
    
    
   agent { node { label 'master' } }
   stages {

   stage('Git Clone') { 
   steps {
      // Get some code from a GitHub repository
      git 'https://github.com/walidsaad/maven-jenkins-pipeline.git'
   }
   }     
   stage('Build Docker Maven Image') {
   steps {
      // Run the maven docker build
    echo "Build Docker Maven Image With Sample WebApp"
    sh "docker build -t=\"${MAVEN_IMAGE_NAME}:${DOCKER_TAG}\" ./maven/"
   }
   }
   stage('Generate Maven Artifact') {
   steps {
    echo "Run Docker Container and Generate Artifcat"
    sh "docker run --rm -d -v /home/stagiaire/.m2:/root/.m2 -w /app/training-webapp/ --name test-maven  ${MAVEN_IMAGE_NAME}:${DOCKER_TAG} mvn clean install"
   }
   }
   stage('Deploy Artifcat') {
   steps {
      // Run the tomcat deploy
    echo "Build Tomcat Image with Artifact"
    sh "docker build -t=\"${TOMCAT_IMAGE_NAME}:${DOCKER_TAG}\" ./tomcat/"
    echo "Run Tomcat Container"
    sh "docker run --rm -d -v /home/stagiaire/.m2/repository/com/mycompany/app/training-webapp/1.0-SNAPSHOT/:/usr/local/tomcat/webapps/ -p ${TEST_LOCAL_PORT}:8080 --name maven-webapp ${TOMCAT_IMAGE_NAME}:${DOCKER_TAG}"
   }
   }
   stage('Push Images To DockerHUb') {
   steps {
    echo "Login to Docker HUB"
    sh "docker login -u ${DOCKER_USR} -p ${DOCKER_PSW}"
    echo "Push Maven Image vers DockerHUb"
    sh "docker tag ${MAVEN_IMAGE_NAME}:${DOCKER_TAG} ${DOCKER_REG}/${MAVEN_IMAGE_NAME}:${DOCKER_TAG}"
    sh "docker push ${DOCKER_REG}/${MAVEN_IMAGE_NAME}:${DOCKER_TAG}"
    echo "Push Tomcat Image vers DockerHUb"
    sh "docker tag ${TOMCAT_IMAGE_NAME}:${DOCKER_TAG} ${DOCKER_REG}/${TOMCAT_IMAGE_NAME}:${DOCKER_TAG}"
    sh "docker push ${DOCKER_REG}/${TOMCAT_IMAGE_NAME}:${DOCKER_TAG}"
   }
   }
}

}
