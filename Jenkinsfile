#!groovy

String workspace = "/var/jenkinsworkspace" // 工作空间路径
String mavenSetting = "m2/settings.xml" // Maven配置文件

pipeline {
    // 代理
    agent {
        node {
            label "slave" // 指定标签为slave的节点运行
            customWorkspace "${workspace}" // 运行工作空间(可选)
        }
    }
    // 选项
    options {
        timestamps() // 日志显示时间戳，需要插件Timestamper
        skipDefaultCheckout() // 删除隐式checkout scm语句 
        disableConcurrentBuilds() // 禁止并行构建
        timeout(time: 1, unit: "HOURS") // 防止因为错误导致流水线不结束而消耗资源，只允许运行一小时
    }

    stages {
        // ?
        stage('Checkout'){
            //必须有，该checkout步骤将检出从源控制代码; scm是一个特殊变量，指示checkout步骤克隆触发此Pipeline运行的特定修订。
            checkout scm
        }

        // 构建
        stage("Build") {
            steps {
                // 调用Maven
                sh """
                    pwd
                    mvn --version
                    mvn -s ${mavenSetting} clean package
                    """
            }
        }

        // 发布
        stage("Release") {
            steps {
                sh """
                    mv target/fatcat-0.0.1.jar target/fatcat.jar
                    """
                archiveArtifacts 'target/fatcat.jar'
            }
        }
    }
        // 构建后操作
    post {
        always {
            // 总是执行
            script {
                // currentBuild是全局变量
                // currentBuild.description是构建描述
                currentBuild.description = 'post - always'
            }
        }

        success {
            // 构建成功的执行
            // TODO: 部署或者发布Artificial
            script {
                currentBuild.description += '\npost - success'
            }
        }

        failure {
            // 构建失败的执行
            // TODO: 生成报告、通知管理员
            script {
                currentBuild.description += '\npost - failure'
            }
        }

        aborted {
            // 构建取消的操作
            // TODO: 通知管理员
            script {
                currentBuild.description += '\npost - aborted'
            }
        }
    }
}
