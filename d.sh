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
if [ $ref = $dev ]; then
	echo "Develope Branch"
	pwd
	mvn -v
fi
if [ $ref = $master ]; then
	echo "Master Branch"
fi
