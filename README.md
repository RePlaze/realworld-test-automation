## 🏗️ Architecture


```
src/test/kotlin/com/realworld/tests/
├── api/              # API test layer
│   ├── client/       # RestAssured client
│   └── models/       # API request/response models
├── base/             # Base test classes
├── config/           # Configuration management
├── ui/               # UI test layer
│   └── pages/        # Page Object Model
└── utils/            # Helper utilities
```

## 🛠️ Technology Stack

- **Kotlin** - Primary programming language
- **TestNG** - Test framework
- **Selenide** - UI automation (Selenium wrapper)
- **RestAssured** - API testing
- **Allure** - Test reporting
- **Gradle** - Build tool

## 🚀 Quick Start

### Prerequisites
- JDK 17+
- Chrome browser
- Gradle 8.0+

### Running Tests

```bash
# Run all tests
./gradlew test

# Run specific test suite
./gradlew test --tests "*.ArticleApiTest"

# Run with specific tag
./gradlew test -Dgroups="api"
```

### Generate Allure Report

```bash
# Generate and open report
./gradlew allureReport
./gradlew allureServe
```

## 📋 Test Coverage

### API Tests
- ✅ Article CRUD operations
- ✅ Article favoriting
- ✅ Comment management
- ✅ Tag retrieval and filtering
- ✅ User authentication

### UI Tests
- ✅ Article creation via UI
- ✅ Article editing
- ✅ Article deletion
- ✅ Article interactions (favorite, comment)
- ✅ Tag navigation and filtering

## 🎯 Key Features

### Expressive Test Design
Tests are written to read like documentation:

```kotlin
@Test
@Description("""
    Given I am authenticated as a valid user
    When I create a new article with valid data
    Then the article should be created successfully
    And all provided data should be saved correctly
""")
fun `Should create article with all required fields`()
```

### Clean Page Object Model
Fluent interface design for readable test flows:

```kotlin
homePage
    .clickNewArticle()
    .createArticle(title, description, body, tags)
    .verifyArticleDisplayed()
```

### Comprehensive Logging
All actions are logged with appropriate detail levels for debugging.

### Parallel Execution
Tests can run in parallel for faster feedback (configurable in testng.xml).

## 🔧 Configuration

Test configuration is managed via `test.properties`:
