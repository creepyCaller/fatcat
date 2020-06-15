#!/bin/bash

# 构建->执行Shell
# pwd
# sh d.sh ${ref} ${repository_name}

ref=${1}
repo_name=${2}
echo "ref: ${ref}"
echo "repo_name: ${repo_name}"
echo '${repo_name}'
echo "${repo_name}"
master="refs/heads/master"
dev="refs/heads/dev"
mavenSetting="m2/settings.xml"
if [ $ref = $dev ]; then
	echo "Develope Branch"
	echo "Current directory:"
	pwd
	echo "Maven version:"
	mvn -v
	echo "Start maven package"
	mvn -s ${mavenSetting} clean package
fi
if [ $ref = $master ]; then
	echo "Master Branch"
	echo "do nothing"
fi
