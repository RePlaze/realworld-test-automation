#!/bin/bash

# RealWorld Test Runner Script

set -e

echo "🚀 RealWorld Test Automation Framework"
echo "====================================="

# Function to display usage
usage() {
    echo "Usage: $0 [OPTION]"
    echo "Options:"
    echo "  all       - Run all tests"
    echo "  api       - Run API tests only"
    echo "  ui        - Run UI tests only"
    echo "  smoke     - Run smoke tests"
    echo "  report    - Generate and open Allure report"
    echo "  clean     - Clean build artifacts"
    echo "  help      - Display this help message"
}

# Function to run tests
run_tests() {
    local test_type=$1
    
    case $test_type in
        "all")
            echo "📋 Running all tests..."
            ./gradlew clean test
            ;;
        "api")
            echo "🔌 Running API tests..."
            ./gradlew clean test --tests "*api.*"
            ;;
        "ui")
            echo "🖥️  Running UI tests..."
            ./gradlew clean test --tests "*ui.*"
            ;;
        "smoke")
            echo "🔥 Running smoke tests..."
            ./gradlew clean test --tests "*ArticleApiTest.Should create article*" --tests "*ArticleUiTest.Should create article*"
            ;;
        *)
            echo "❌ Unknown test type: $test_type"
            usage
            exit 1
            ;;
    esac
}

# Function to generate report
generate_report() {
    echo "📊 Generating Allure report..."
    ./gradlew allureReport
    
    # Try to open the report
    if command -v open &> /dev/null; then
        open build/reports/allure-report/index.html
    elif command -v xdg-open &> /dev/null; then
        xdg-open build/reports/allure-report/index.html
    else
        echo "✅ Report generated at: build/reports/allure-report/index.html"
    fi
}

# Main script logic
case "${1:-all}" in
    "all"|"api"|"ui"|"smoke")
        run_tests "$1"
        echo ""
        echo "✨ Tests completed!"
        echo "Run './run-tests.sh report' to view the test report"
        ;;
    "report")
        generate_report
        ;;
    "clean")
        echo "🧹 Cleaning build artifacts..."
        ./gradlew clean
        echo "✅ Clean completed!"
        ;;
    "help"|"-h"|"--help")
        usage
        ;;
    *)
        echo "❌ Invalid option: $1"
        usage
        exit 1
        ;;
esac
