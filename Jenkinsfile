pipeline {
    agent any

    tools {
        maven "MavenTool"
    }

    stages {
        stage("Checkout") {
            steps {
                checkout scmGit(
                    branches: [[name: '*/main']],
                    browser: github('https://github.com/gpk0402/xstack-jenkins-task'),
                    extensions: [],
                    userRemoteConfigs: [[url: 'https://github.com/gpk0402/xstack-jenkins-task.git']]
                )
            }
        }

        stage("Build Modules & Build Docker Images") {
            steps {
                script {
                    // def modules = findFiles(glob: '**/pom.xml')
                    def modules = ['GymMicroservice', 'GymReportMicroservice', 'NotificationMicroservice', 'GymAuthenticationService-1', 'EurekaServer', 'GatewayServer',]
                    for (def module in modules) {
                        dir("${module}") {
                            echo "Building ${module}..."
                            bat "mvn clean install"
                        }
                    }
                }
            }
        }

        // stage("Push Docker Images to Docker Hub") {
        //     steps {
        //         script {
        //             def dockerHubUsername = 'danvi'
        //             def dockerHubPassword = 'DanviShanmuki@2'
        //
        //             //def modules = findFiles(glob: '**/pom.xml')
        //             def modules = ['gymservice', 'gymnotificationservice']
        //             for (def module in modules) {
        //                 def imageName = "${module}"
        //
        //                 echo "Pushing Docker image: ${imageName}"
        //                 bat "docker login -u ${dockerHubUsername} -p ${dockerHubPassword}"
        //                 bat "docker push ${imageName}"
        //             }
        //         }
        //     }
        // }
    }
}

def findFiles(pomContent) {
    def pom = new XmlSlurper().parseText(pomContent)
    return pom.modules.module.collect { it.text() }
}
