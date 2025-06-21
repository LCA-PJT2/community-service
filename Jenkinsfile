#!/usr/bin/env groovy
def APP_NAME
def APP_VERSION
def DOCKER_IMAGE_NAME

pipeline {
    agent {
        node {
            label 'master'
        }
    }

    environment {
        GIT_URL = "https://github.com/LCA-PJT2/community-service.git"
        GITHUB_CREDENTIAL = "github-token"
        ARTIFACTS = "build/libs/**"
        DOCKER_REGISTRY = "suhyunkim7288"
        DOCKERHUB_CREDENTIAL = 'dockerhub-token'

        DISCORD_WEBHOOK = 'https://discord.com/api/webhooks/your_webhook_url_here'
        APP_CONFIG_REPO = "https://<token>@github.com/your-org/infra-repo.git"
    }

    options {
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: "30", artifactNumToKeepStr: "30"))
        timeout(time: 120, unit: 'MINUTES')
    }

    tools {
        gradle 'Gradle 8.14.2'
        jdk 'OpenJDK 17'
        dockerTool 'Docker'
    }

    stages {
        stage('Check Branch') {
            steps {
                script {
                    def ref = env.GIT_REF ?: ''
                    echo "Webhook ref: ${ref}"

                    if (!ref.endsWith('/main')) {
                        echo "⛔️ Not main branch (${ref}), skipping pipeline."
                        currentBuild.result = 'SUCCESS'
                        return
                    }
                }
            }
        }

        stage('Set Version') {
            steps {
                script {
                    APP_NAME = sh(
                        script: "grep rootProject.name settings.gradle | cut -d \"'\" -f2",
                        returnStdout: true
                    ).trim()

                    APP_VERSION = sh(
                        script: "grep '^version' build.gradle | cut -d \"'\" -f2",
                        returnStdout: true
                    ).trim()

                    DOCKER_IMAGE_NAME = "${DOCKER_REGISTRY}/${APP_NAME}:${APP_VERSION}"

                    echo "✅ IMAGE_NAME: ${APP_NAME}"
                    echo "✅ IMAGE_VERSION: ${APP_VERSION}"
                    echo "✅ DOCKER_IMAGE_NAME: ${DOCKER_IMAGE_NAME}"
                }
            }
        }

        stage('Build & Test Application') {
            steps {
                sh "gradle clean build"
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${DOCKER_IMAGE_NAME}")
                }
            }
        }

        stage('Push Docker Image') {
            steps {
                script {
                    docker.withRegistry("", DOCKERHUB_CREDENTIAL) {
                        docker.image("${DOCKER_IMAGE_NAME}").push()
                    }
                    sh "docker rmi ${DOCKER_IMAGE_NAME}"
                }
            }
        }

        stage('Update Deployment Manifest & Git Push') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'GITHUB_CREDENTIAL', variable: 'GITHUB_TOKEN')]) {
                        sh """
                        # 1. 클린업 및 GitOps 저장소 clone
                        rm -rf app-config
                        git clone https://${GITHUB_TOKEN}@github.com/LCA-PJT2/app-config.git

                        cd app-config

                        # 2. image 태그 자동 업데이트
                        sed -i 's|image: suhyunkim7288/${APP_NAME}:.*|image: suhyunkim7288/${APP_NAME}:${APP_VERSION}|' community-service/prd/community-service-deploy.yml

                        # 3. Git 커밋 및 푸시
                        git config user.name "SuHyunKKim"
                        git config user.email "kimsteven728@dgu.ac.kr"
                        git add community-service/prd/community-service-deploy.yml
                        git commit -m "🔁 update image tag to ${APP_VERSION}"
                        git push origin main
                        """
                    }
                }
            }
        }
    }
}
