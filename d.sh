#!/bin/bash
ref=${1}
repo_name=${2}
echo "ref: ${ref}"
echo "repo_name: ${repo_name}"
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
