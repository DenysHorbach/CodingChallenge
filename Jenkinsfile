pipeline {
    agent any

    environment {
        // Generate a unique identifier for the image tag
        def BUILD_ID = "${env.BUILD_NUMBER}"
        def TIMESTAMP = new Date().format('yyyyMMddHHmmss')

        // Define the image tag using the unique identifier
        def IMAGE_TAG = "${BUILD_ID}-${TIMESTAMP}"
        def ECR_REPOSITORY = '328895011076.dkr.ecr.eu-central-1.amazonaws.com/coding-challenge'
        def AWS_REGION = 'eu-central-1'
        def STACK_NAME = 'coding-challenge-resource-stack'
        def TEMPLATE_FILE = 'cloudformation/template.yaml'
    }


    tools {
        jdk 'Java17'
    }

    stages {
        stage('Clone repository') {
            steps {
                script {
                    checkout scm
                }
            }
        }

//         stage('Checkout') {
//             steps {
//                 checkout([$class: 'GitSCM',
//                           branches: [[name: '*/main']],
//                           doGenerateSubmoduleConfigurations: false,
//                           extensions: [[$class: 'CleanBeforeCheckout'],
//                                        [$class: 'SubmoduleOption',
//                                         disableSubmodules: false,
//                                         parentCredentials: true,
//                                         recursiveSubmodules: true,
//                                         reference: '',
//                                         trackingSubmodules: false]],
//                           submoduleCfg: [],
//                           userRemoteConfigs: [[credentialsId: 'github-credentials',
//                                                url: 'https://github.com/DenysHorbach/CodingChallenge.git']]])
//             }
//         }

        stage('Build and Test') {
            steps {
                withGradle() {
                    sh './gradlew clean'
                    sh './gradlew openApiGenerate'
                    sh './gradlew build'

                }
            }
        }

        stage('Docker build and push') {
            steps {
                script {
                    docker.withRegistry('https://328895011076.dkr.ecr.eu-central-1.amazonaws.com', 'ecr:eu-central-1:aws-credentials') {
                        def image = docker.build("${ECR_REPOSITORY}:${IMAGE_TAG}", './')
                        image.push()
                    }
                }
            }
        }

        stage('Update CloudFormation Stack') {
            steps {
                withAWS(region: AWS_REGION, credentials: 'aws-credentials') {
                    cfnUpdate(
                        stack: STACK_NAME,
                        file: TEMPLATE_FILE,
                        params: [
                            'ImageTag': IMAGE_TAG
                        ]
                    )
                }
            }
        }
    }
}
