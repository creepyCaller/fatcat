#!/bin/bash
ref=${1}
repo_name=${2}
echo "ref: ${ref}"
echo "repo_name: ${repo_name}"
dev="refs/heads/master"
master="refs/heads/dev"
if [ $ref = $dev ]; then
	pwd
	mvn -v
fi
