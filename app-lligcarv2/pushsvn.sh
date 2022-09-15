#!/bin/sh
# Requirement For Windows
#  1. Versioned Project with git,svn. 
#  2. Download kiuwan application
#  3. Username and password for kiuwan and svn
#  4. Enable CLI in  tortoise
#  5. pushsvn.sh file
#  6. Use git bash

# Steps 
#  1. Add both kiuwan and svn credentials in windows environment 
#  2. put pushsvn.sh file on the root directory of  your project
#  3. On git bash terminal execute this file with git/svn comment as --- sh pushsvn.sh "Pushed to Kiuwan"
#  4. Hit Enter
#  3. Grab a cup of coffee, that single command automates the code versioning process
#DEVELOPERS  DAVID MANDUKU AND OSCAR MUIGAI


#Kiuwan analyzer location/path

#alias agent='D:/KiuwanLocalAnalyzer/KiuwanLocalAnalyzer/bin/agent.cmd'
alias agent='C:/KiuwanLocalAnalyzer/bin/agent.cmd'

CI_PROJECT_DIR=$(pwd)

COMMENT_ARGUMENT="$1"
 #  rm -rf "svnD"
 # svn -username=$SVN_USERNAME --password=$SVN_PASSWORD 
 # kiuwan -user $KIUWAN_USER --pass $KIUWAN_PASSWD


#svn status

#svn  add * --force  

# get credentials from windows environment
#svn commit -m "$COMMENT_ARGUMENT" --username=$SVN_USERNAME --password=$SVN_PASSWORD

git add .

git commit -m "$COMMENT_ARGUMENT"

#getting remote name 
for OUTPUT in $(git remote -v | grep -w "fetch" | awk '{print $1}')
do
	echo $OUTPUT
	git push  $OUTPUT 

done

#PUSHING CODE TO KIUWAN

#getting folder name
CI_PROJECT_DIR=$(pwd)
basename "$CI_PROJECT_DIR"
folderName="$(PWD | sed 's!.*/!!')"

#C:\Users\dkimani\StudioProjects\cargill-app-dynamic-features\cargill
#C:\Users\dkimani\StudioProjects\cargill-app-dynamic-features\app\src\main\java
agent -n "Kotlin" -s "$CI_PROJECT_DIR/app/src/main/java" -l "devops_appmodule_$folderName" -c --user $KIUWAN_USER --pass $KIUWAN_PASSWD
#MODULES
agent -n "Kotlin" -s "$CI_PROJECT_DIR/authcargill/src/main/java" -l "devops_authcargill_$folderName" -c --user $KIUWAN_USER --pass $KIUWAN_PASSWD
agent -n "Kotlin" -s "$CI_PROJECT_DIR/cargillfarmer/src/main/java" -l "devops_cargillfarmer_$folderName" -c --user $KIUWAN_USER --pass $KIUWAN_PASSWD
agent -n "Kotlin" -s "$CI_PROJECT_DIR/cargillbuyer/src/main/java" -l "devops_cargillbuyer_$folderName" -c --user $KIUWAN_USER --pass $KIUWAN_PASSWD
agent -n "Kotlin" -s "$CI_PROJECT_DIR/cargillcoop/src/main/java" -l "devops_cargillcoop_$folderName" -c --user $KIUWAN_USER --pass $KIUWAN_PASSWD








