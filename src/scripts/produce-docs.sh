#!/bin/bash

# This script will build the enunciate documentation for the rest api
# and upload it to the apidocs.dev.qhrtech.com repo.
#
# Commits to qhrtech/apidocs.dev.qhrtech.com will trigger a redeploy to GitHub Pages

#Prints the usage message and exits the script
function usage() {
  cat <<-END

    USAGE: ./produce-docs.sh [OPTIONS]

    OPTIONS:
      -h    Print this message and exit
      -b    Include a build of the documentation: mvn install -Penunciate
      -r    Release the documentation by committing and pushing it.
      -d    Dirty Run. This option will not clean up the apidocs.dev.qhrtech.com repo.
      -s    Sandbox Run. This option will save docs in the sandbox subfolder of the apidocs.dev.qhrtech.com.
END
  exit 0;
}

# Logs a message to the console
function message() {
  echo "[DOC PRODUCER]: $1"
}

# Exits the script with an error code and an optional message
function error() {

  # $1 = Optional error message

  if [ -z "$1" ]
  then
    (>&2 echo "[ERROR]: Exiting script.")
  else
    (>&2 echo "[ERROR]: $1")
  fi
  exit 1
}

# Entry point for the script
function start() {

  # $1 = Enunciate Build Flag
  # $2 = Release Run - Actually commit the doc changes
  # $3 = Dirty Run - Leave the static sites repo after committing
  # $4 = Sandbox Documentation Build - Pushes the updated docs to a subfolder

  message "Starting Documentation Build Script"
  if [ "$1" = true ]
  then
    build_docs
  else
    message "Skipping Enunciate build."
  fi
  tmp_dir="tmp"
  checkout_sites ${tmp_dir}
  move_files "docs" ${tmp_dir} "$4"
  move_files "docs-internal" ${tmp_dir} "$4"
  move_files "docs-patient" ${tmp_dir} "$4"
  move_files "docs-provider" ${tmp_dir} "$4"
  if [ "$2" = true ]
  then
    commit_changes ${tmp_dir} "$4"
  else
    message "Leaving documentation un-staged."
  fi
  if [ "$3" = false ]
  then
    clean_up ${tmp_dir}
  else
    message "Leaving documentation in $PWD/$tmp_dir/"
  fi
}


# Build the documentation
function build_docs() {
  message "Building Enunciate Documentation."
  message "mvn clean install -DskipTests -Penunciate"
  mvn clean install -DskipTests -Penunciate
  if [ $? != 0 ]
  then
    # we don't want to continue to run if the documentation did not build properly
    error "Maven Build Failed, Terminating Script."
  fi
}

# Checkout the static sites repo
function checkout_sites() {

  # $1 = Directory to check the static sites repo out into

  if [ -z "$1" ]
  then
    error "Must pass a directory"
  fi
  message "Checking out qhrtech/apidocs.dev.qhrtech.com into $1"
  if ! [ -e $1 ]
  then
    #if the passed directory doesn't exist, make it and cd into it
    message "mkdir $1"
    mkdir $1

    message "cd $1"
    cd $1
  elif [ -d $1 ]
  then
    #otherwise, if it is a director, just cd into it
    message "cd $1"
    cd $1
  else
    # if it exists and is not a directory, we need to exit
    error "$1 Already exists and is not a directory."
  fi
  #Remove the checkout location in the event the script is re-urn
  message "rm -rf apidocs.dev.qhrtech.com"
  rm -rf apidocs.dev.qhrtech.com

  #once the directory is made, we can check out the apidocs repo
  message "git clone git@github.com:qhrtech/apidocs.dev.qhrtech.com.git"
  git clone git@github.com:qhrtech/apidocs.dev.qhrtech.com.git

  # then we change directories into the static sites repo to checkout master
  message "cd apidocs.dev.qhrtech.com"
  cd apidocs.dev.qhrtech.com

  message "git checkout main"
  git checkout main

  message "cd ../.."
  cd ../..
}

# Moves a passed file from the build directory, to the docs repo directory
function move_files() {

  # $1 = File to move
  # $2 = Destination folder to move to.
  # $3 = Sandbox Run - If the build is a sandbox document deploy

  if [ -z "$1" ] && [ -z "$2" ]
  then
    error "Must pass a file or directory to move"
  fi
  location="$2/apidocs.dev.qhrtech.com/"
  sandboxDirectory="sandbox/"
  if [ "$3" = true ]
  then
    location="$location$sandboxDirectory"
    if ! [ -d ${location} ]
    then
      #if the sandbox directory doesn't exist, make it
      message "mkdir $location"
      mkdir ${location}
    fi
  fi
  message "Moving $1 to temporary directory."
  cp -r target/$1 ${location}
}

# Commits and pushes the changes to the apidocs repository
function commit_changes() {

  # $1 = Directory the document repository was checked out into
  # $2 = Sandbox Run - If the build is a sandbox document deploy

  message "Committing documentation to apidocs repo."
  message "cd $1/apidocs.dev.qhrtech.com"
  cd $1/apidocs.dev.qhrtech.com

  message "git add ."
  git add .

  if [ "$2" = true ]
  then
    commit_message="Updating Accuro API Sandbox Documentation."
  else
    commit_message="Updating Accuro API Documentation after release."
  fi

  message "git commit -m '${commit_message}'"
  git commit -m "$commit_message"

  message "git push origin master"
  git push origin master

  message "cd ../.."
  cd ../..
}

# cleans up temporary files
function clean_up() {

  # $1 = The temporary file name to remove

  message "Removing temporary files"
  rm -rf "$1"
}

# initialization function, gathers command line args and kicks off script
function init() {
  build=false
  release=false
  dirty=false
  sandbox=false
  while getopts ":hbrds" opt; do
    case ${opt} in
      h )
        #print help message
        usage
        ;;
      b )
        #run the enunciate build
        message "Enunciate Build Queued."
        build=true
        ;;
      r )
        #perform a release run. This will stage, commit and push the docs
        message "Performing Release Run."
        release=true
        ;;
      d )
        #perform a dirty run, leaving the api docs repo undeleted
        #this flag can be useful for development testing to examine the repo
        message "Performing Dirty Run, API Docs Repo will be left."
        dirty=true
        ;;
      s )
        #save the docs to the sandbox subfolder
        #this flag can be used for deploying docs for the sandbox
        message "Performing Sandbox docs deploy."
        sandbox=true
        ;;
      \? )
        #unknown option, print help and exit
        message "Unknown option, print help and exit."
        usage
    esac
  done
  shift $((OPTIND-1))

  start ${build} ${release} ${dirty} ${sandbox}
  message 'All Done!'
}

# Script Start
init "$@"
exit
