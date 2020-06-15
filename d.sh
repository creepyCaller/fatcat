#!/bin/bash

# 构建->执行Shell
# pwd
# sh d.sh ${ref}

ref=${1}
echo "ref: ${ref}"
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
	mvn -Dmaven.test.skip=true -s ${mavenSetting} clean package
	if [ -e target/fatcat-0.0.1.jar ]; then
		if [ ! -d fatcat ]; then
			echo "Not exist fatcat directory, make one"
			mkdir fatcat
		else
			echo "Clean fatcat/"
			rm -rf fatcat/*
		fi
		echo "Copy files to fatcat/"
		mv target/Libs fatcat
		mv target/fatcat-0.0.1.jar fatcat/fatcat.jar
		cp -r Resources fatcat
		mv fatcat/Resources/Script/fatcat.bat fatcat/fatcat.bat
		mv fatcat/Resources/Script/fatcat.sh fatcat/fatcat.sh
		cp -r WAR fatcat
		cp -r Settings fatcat
		cp -r WebApplication fatcat
		echo "Zip artificals"
		if [ ! -d artifical ]; then
			mkdir artifical
		fi
		zip -r artifical/fatcat.zip fatcat
		echo "Clean fatcat/"
		rm -rf fatcat/*
	else
		echo "Maven package failure!"
	fi


fi
if [ $ref = $master ]; then
	echo "Master Branch"
	echo "do nothing"
fi
