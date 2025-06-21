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
        GIT_REF = "${params.GIT_REF ?: ''}"
        GIT_URL = "https://github.com/LCA-PJT2/community-service.git"
        GITHUB_CREDENTIAL = "github-token"
        ARTIFACTS = "build/libs/**"
        DOCKER_REGISTRY = "suhyunkim7288"
        DOCKERHUB_CREDENTIAL = 'dockerhub-token'
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
                    echo "Webhook ref: ${GIT_REF}"

                    if (!GIT_REF.endsWith('/main')) {
                        echo "⛔️ Not main branch (${GIT_REF}), skipping pipeline."
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
    }
}
