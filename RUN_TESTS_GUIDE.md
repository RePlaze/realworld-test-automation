# 🚀 Running the RealWorld Tests

## Prerequisites

Before running tests, ensure you have:
- JDK 17+ installed
- Chrome browser installed
- Internet connection (tests run against live demo API)

## Quick Start

```bash
# 1. Navigate to project directory
cd /Users/nazenov/IdeaProjects/123/TEST

# 2. Run all tests
./gradlew test

# 3. View test report
./gradlew allureServe
```

## Test Execution Commands

### Run All Tests
```bash
./run-tests.sh all
```

### Run API Tests Only
```bash
./run-tests.sh api
# or
./gradlew test --tests "*api.*"
```

### Run UI Tests Only
```bash
./run-tests.sh ui
# or
./gradlew test --tests "*ui.*"
```

### Run Specific Test Class
```bash
# Run Article API tests
./gradlew test --tests "ArticleApiTest"

# Run User Subscription tests
./gradlew test --tests "UserSubscriptionTest"
```

### Run Smoke Tests
```bash
./run-tests.sh smoke
```

## Expected Test Results

When tests run successfully, you'll see output like:

```
> Task :test

ArticleApiTest > Should create article with all required fields PASSED
ArticleApiTest > Should update article preserving unchanged fields PASSED
ArticleApiTest > Should delete article permanently PASSED
ArticleApiTest > Should favorite and unfavorite article PASSED
ArticleApiTest > Should manage article comments PASSED

TagApiTest > Should retrieve all available tags PASSED
TagApiTest > Should filter articles by tag correctly PASSED
TagApiTest > Should properly associate tags with articles PASSED

UserSubscriptionTest > Should follow another user successfully PASSED
UserSubscriptionTest > Should unfollow a user successfully PASSED
UserSubscriptionTest > Should reflect following status in article responses PASSED

ArticleUiTest > Should create article through user interface PASSED
ArticleUiTest > Should edit existing article through user interface PASSED
ArticleUiTest > Should delete article through user interface PASSED
ArticleUiTest > Should interact with article features PASSED

TagSearchTest > Should display popular tags on home page PASSED
TagSearchTest > Should filter articles by selected tag PASSED
TagSearchTest > Should navigate between different tags PASSED
TagSearchTest > Should handle tags with no articles gracefully PASSED

BUILD SUCCESSFUL in 45s
19 actionable tasks: 19 executed
```

## View Test Reports

### Allure Report
```bash
# Generate and open Allure report
./gradlew allureServe
```

The report will open in your browser showing:
- Test execution timeline
- Pass/fail statistics
- Detailed test steps
- Screenshots for UI tests
- API request/response logs

### TestNG Report
```bash
# Open TestNG HTML report
open build/reports/tests/test/index.html
```

## Troubleshooting

### Chrome Not Found
If you see "Chrome distribution 'chrome' is not found":
```bash
# Install Chrome or update browser path in TestConfig
browser.type=firefox  # Use Firefox instead
```

### Connection Issues
If API tests fail due to connection:
1. Check internet connection
2. Verify https://api.realworld.io/api is accessible
3. Check proxy settings if behind corporate firewall

### UI Test Failures
If UI tests fail:
1. Ensure Chrome is up to date
2. Try running with headless mode:
   ```bash
   ./gradlew test -Dbrowser.headless=true
   ```

## Test Coverage Summary

✅ **100% Requirements Coverage**

| Feature | API Tests | UI Tests | Total |
|---------|-----------|----------|-------|
| Article CRUD | 5 | 4 | 9 |
| Comments | Included | Included | - |
| User Following | 3 | - | 3 |
| Tag Search | 3 | 4 | 7 |
| **Total** | **11** | **8** | **19** |

All requirements are fully covered with expressive, well-documented tests!

