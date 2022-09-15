#!/bin/sh

#To do 

alias agent='C:/KiuwanLocalAnalyzer/bin/agent.cmd'

CI_PROJECT_DIR=$(pwd)

COMMENT_ARGUMENT="$1"
 #  rm -rf "svnD"
 # svn checkout "https://svn.ekenya.co.ke/svn/GAMBIA/Trust_Bank/UPGRADE/Mobile_Banking/IOS%20APP/svnD" --username=$SVN_USERNAME --password=$SVN_PASSWORD

  # rm -rf $CI_PROJECT_DIR/.svn
# # # echo "$CI_PROJECT_DIR/IOS APP/.svn"
 # cp -a "$CI_PROJECT_DIR/svnD/.svn" . sh pushsvn.sh "comment"


#svn status
#
#svn  add * --force
#
#svn commit -m "$COMMENT_ARGUMENT" --username=$SVN_USERNAME --password=$SVN_PASSWORD

git add .

git commit -m "$COMMENT_ARGUMENT"

#getting remote name 
for OUTPUT in $(git remote -v | grep -w "fetch" | awk '{print $1}')
do
	echo $OUTPUT
	git push  $OUTPUT master

done

#PUSHING CODE TO KIUWAN

#getting folder name
CI_PROJECT_DIR=$(pwd)
basename "$CI_PROJECT_DIR"
folderName="$(PWD | sed 's!.*/!!')"

 #agent -n "Kotlin" -s "$CI_PROJECT_DIR/app/src/main/java/" -l "cargillApp_v1" -c --user $KIUWAN_USER --pass $KIUWAN_PASSWD


	
agent -n "Kotlin" -s "$CI_PROJECT_DIR/app/src/main/java" -l "devops_appmodule_$folderName" -c --user $KIUWAN_USER --pass $KIUWAN_PASSWD








