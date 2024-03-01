#!/bin/bash

# This script will build the openapi json for AccuroApi
# and upload it to the apidocs.dev.qhrtech.com repo.
#

#Prints the usage message and exits the script
function usage() {
  cat <<-END

    USAGE: ./deploy-openapi.sh [OPTIONS]

    OPTIONS:
      -h    Print this message and exit
      -b    Include a build of the documentation
      -r    Release the documentation by committing and pushing it.
      -d    Dirty Run. This option will not clean up the static sites repo.
      -s    Sandbox Run. This option will save docs in the sandbox subfolder of the static sites.
      -D    Dev Run. This option will save docs in the dev subfolder of the static sites.
      -Q    QA Run. This option will save docs in the qa subfolder of the static sites.

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

  # $1 = Swagger Build Flag
  # $2 = Release Run - Actually commit the doc changes
  # $3 = Dirty Run - Leave the api docs repo after committing
  # $4 = Sandbox Documentation Build - Pushes the updated docs to a subfolder
  # $5 = Dev Documentation Build
  # $6 = QA Documentation Build
  # $7 = Version

  # Use the provided version or extract it from pom.xml if not provided
  version="$7"
  if [ -n "$version" ]; then
      message "Using the provided version: $version"
    else
      version="$(sed -nr '0,/<scm>/s/.*<version>(\w+\.\w+\.\w+-?\w+?)<\/version>.*/\1/p' pom.xml)"
      message "Extracted version from pom.xml: $version"
      fi
  sed -i -r 's/(.*"version": ?")(.*)(".*)/\1'"$version"'\3/' 'src/main/resources/swagger.json'
  message "Starting Documentation Build Script"
  if [ "$1" = true ]
  then
    build_docs
  else
    message "Skipping Swagger build."
  fi
  tmp_dir="tmp"
  checkout_sites ${tmp_dir}
  move_files "openapi-ui" ${tmp_dir} "$4" "$5" "$6"
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
  message "Building OpenApi Documentation."
  message "mvn clean install -Pswagger -DskipTests"
  mvn clean install -Pswagger -DskipTests
  if [ $? != 0 ]
  then
    # we don't want to continue to run if the documentation did not build properly
    error "Maven Build Failed, Terminating Script."
  fi
  cd openapi-ui
  npm install
  npm run build
  cd ..
}

# Checkout the api docs repo
function checkout_sites() {

  # $1 = Directory to check the api docs repo out into

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

  #once the directory is made, we can check out the static sites repo
  message "git clone git@github.com:qhrtech/apidocs.dev.qhrtech.com.git"
  git clone --depth 1 git@github.com:qhrtech/apidocs.dev.qhrtech.com.git

  # then we change directories into the apidocs.dev.qhrtech.com to checkout master
  message "cd "
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
  location="$2/apidocs.dev.qhrtech.com"
  sandboxDirectory="/sandbox/"
  if [ "$3" = true ]; then
    location="$location$sandboxDirectory"
  elif [ "$4" = true ]; then
    location="$location/dev"
  elif [ "$5" = true ]; then
    location="$location/qa"
  fi

  # Ensure the chosen directory exists
  if ! [ -d ${location} ]; then
    message "mkdir $location"
    mkdir -p ${location}
  fi
  message "Moving data from  $1 to temporary directory."
  cp -r $1/dist ${location}/docs
  cp $1/index.html ${location}/docs
  cp $1/target_24x24.png ${location}/docs

  mkdir -p ${location}/docs/json
  cp "target/openapi/openapi.json" ${location}/docs/json
}

# Commits and pushes the changes to the apidocs.dev.qhrtech.com repository
function commit_changes() {

  # $1 = Directory the document repository was checked out into
  # $2 = Sandbox Run - If the build is a sandbox document deploy

  message "Committing documentation to apidocs.dev.qhrtech.com repo."
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

# Initialization function, gathers command line args and kicks off script
function init() {
  build=false
  release=false
  dirty=false
  sandbox=false
  dev=false
  qa=false
  version=
  while getopts ":hbrdDQs:v:" opt; do
    case ${opt} in
      h )
        # Print help message
        usage
        ;;
      b )
        # Run the swagger build
        message "Swagger Build Queued."
        build=true
        ;;
      r )
        # Perform a release run. This will stage, commit, and push the docs
        message "Performing Release Run."
        release=true
        ;;
      d )
        # Perform a dirty run, leaving the api docs repo undeleted
        # This flag can be useful for development testing to examine the repo
        message "Performing Dirty Run, API Docs Repo will be left."
        dirty=true
        ;;
      D )
        # Save the docs to the dev subfolder
        message "Performing Dev docs deploy."
        dev=true
        ;;
      Q )
        # Save the docs to the qa subfolder
        message "Performing QA docs deploy."
        qa=true
        ;;
      s )
        # Save the docs to the sandbox subfolder
        # This flag can be used for deploying docs for the sandbox
        message "Performing Sandbox docs deploy."
        sandbox=true
        ;;
      v)
        version="$OPTARG"
        ;;
      \? )
        # Unknown option, print help and exit
        message "Unknown option, print help and exit."
        usage
    esac
  done
  shift $((OPTIND-1))

  start ${build} ${release} ${dirty} ${sandbox} ${dev} ${qa} "$version"
  message 'All Done!'
}

# Script Start
init "$@"
exit
