#!/bin/bash

# This script will build the Docusaurus site for AccuroApi
# and upload it to the apidocs.dev.qhrtech.com repo.

# Function to print usage instructions
function usage() {
  cat <<-END
    USAGE: ./deploy-docusaurus.sh [OPTIONS]

    OPTIONS:
      -h    Print this message and exit
      -b    Include a build of the Docusaurus documentation
      -r    Release the documentation by committing and pushing it
      -d    Dirty Run. This option will not clean up the api docs repo

END
  exit 0
}

# Logs a message to the console
function message() {
  echo "[DOC DEPLOY]: $1"
}

# Exits the script with an error code and an optional message
function error() {
  if [ -z "$1" ]; then
    (>&2 echo "[ERROR]: Exiting script.")
  else
    (>&2 echo "[ERROR]: $1")
  fi
  exit 1
}

# Build the Docusaurus documentation
function build_docs() {
  message "Building Docusaurus Documentation."
  # Navigate up one directory from src/scripts, then to accuro-api-guide
  cd ../../accuro-api-guide || error "Docusaurus project directory not found."

  # Run npm install and output the console
  echo "Running npm install..."
  npm install | tee npm-install.log || error "Failed to install dependencies."

  # Run npm run build and output the console
  echo "Running npm run build..."
  npm run build | tee npm-build.log || error "Docusaurus build failed."

  # Navigate back to the original directory
  cd - || error "Failed to return to the original directory."
}

# Checkout the api docs repo
function checkout_sites() {
  local tmp_dir=$1
  if [ -z "${tmp_dir}" ]; then
    error "Temporary directory not specified."
  fi

  message "Checking out apidocs.dev.qhrtech.com into ${tmp_dir}"
  mkdir -p "${tmp_dir}" && cd "${tmp_dir}" || error "Failed to create or navigate to ${tmp_dir}."
  git clone --depth 1 git@github.com:qhrtech/apidocs.dev.qhrtech.com.git || error "Failed to clone repository."
  cd apidocs.dev.qhrtech.com || error "Failed to navigate into apidocs.dev.qhrtech.com."
  git checkout main || error "Failed to checkout main branch."
  cd ../.. || error "Failed to navigate back to the original directory."
}

# Moves the Docusaurus build files to the api docs repo directory
function move_files() {
  local tmp_dir=$1

  # The relative path from the script location to the Docusaurus build directory
  local build_dir="../../accuro-api-guide/build"

  [ -z "${build_dir}" ] && error "Build directory not set."
  [ -z "${tmp_dir}" ] && error "Temporary directory not specified."

  local destination="${tmp_dir}/apidocs.dev.qhrtech.com/apiguide"
  message "Copying Docusaurus build files to ${destination}"
  mkdir -p "${destination}" || error "Failed to create destination directory."
  cp -r "${build_dir}/." "${destination}/" || error "Failed to copy Docusaurus build files."
}


# Commits and pushes the changes to the apidocs.dev.qhrtech.com repository
function commit_changes() {
  local tmp_dir=$1
  cd "${tmp_dir}/apidocs.dev.qhrtech.com" || error "Failed to navigate to apidocs.dev.qhrtech.com."

  git add . || error "Failed to add changes."
  git commit -m "Updating Docusaurus API Documentation." || error "Failed to commit changes."
  git push origin main || error "Failed to push changes."
  cd ../.. || error "Failed to navigate back to the original directory."
}

# Cleans up temporary files
function clean_up() {
  local tmp_dir=$1
  message "Cleaning up ${tmp_dir}."
  rm -rf "${tmp_dir}" || message "Warning: Failed to fully clean up ${tmp_dir}."
}

# Main function to control the flow of the script
function main() {
  local build=false release=false dirty=false

  while getopts ":hbrd" opt; do
    case ${opt} in
      h ) usage ;;
      b ) build=true ;;
      r ) release=true ;;
      d ) dirty=true ;;
      \? ) usage ;;
    esac
  done

  local tmp_dir="tmp_$(date +%s)"
   [ "${build}" = true ] && build_docs
    checkout_sites "${tmp_dir}"
    move_files "${tmp_dir}"

  if [ "${release}" = true ]; then
    commit_changes "${tmp_dir}"
  else
    message "Documentation updated locally. Run with -r to release."
  fi

  if [ "${dirty}" = false ]; then
    clean_up "${tmp_dir}"
  else
    message "Leaving temporary files in ${tmp_dir}."
  fi
}

main "$@"
