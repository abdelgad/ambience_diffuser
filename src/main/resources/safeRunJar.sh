#!/bin/bash

# Function to check if Java is installed
check_java_installed() {
    if command -v java &> /dev/null; then
        echo "Java is installed."
        return 0
    else
        echo "Java is not installed. Please install Java first."
        return 1
    fi
}

# Function to run the JAR file
run_jar() {
    local jar_file=$1
    if [[ -f $jar_file ]]; then
        echo "Running JAR file: $jar_file"
        java -jar "$jar_file"
    else
        echo "JAR file $jar_file does not exist."
        exit 1
    fi
}

# Main script execution
if [[ $# -ne 1 ]]; then
    echo "Usage: $0 <path_to_jar_file>"
    exit 1
fi

jar_file=$1

check_java_installed
if [[ $? -ne 0 ]]; then
    exit 1
fi

run_jar "$jar_file"
