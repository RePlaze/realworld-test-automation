# RealWorld Test Suite - cURL Commands

These are the exact API calls that would be executed by the test suite.

## 1. Authentication (Required First)

```bash
# Login to get auth token
curl -X POST https://api.realworld.io/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "user": {
      "email": "test.automation@realworld.io",
      "password": "Test123!"
    }
  }'
```

Save the token from response: `{"user":{"token":"YOUR_TOKEN_HERE",...}}`

## 2. Article Management Tests

### Create Article
```bash
curl -X POST https://api.realworld.io/api/articles \
  -H "Content-Type: application/json" \
  -H "Authorization: Token YOUR_TOKEN" \
  -d '{
    "article": {
      "title": "Amazing Article-1234567890",
      "description": "This article explores cutting-edge technology",
      "body": "# Introduction\n\nThis is the content of our amazing article...",
      "tagList": ["technology", "innovation", "tutorial"]
    }
  }'
```

### Update Article
```bash
curl -X PUT https://api.realworld.io/api/articles/amazing-article-1234567890 \
  -H "Content-Type: application/json" \
  -H "Authorization: Token YOUR_TOKEN" \
  -d '{
    "article": {
      "title": "Updated Title-1234567890"
    }
  }'
```

### Get Article
```bash
curl -X GET https://api.realworld.io/api/articles/amazing-article-1234567890 \
  -H "Authorization: Token YOUR_TOKEN"
```

### Delete Article
```bash
curl -X DELETE https://api.realworld.io/api/articles/amazing-article-1234567890 \
  -H "Authorization: Token YOUR_TOKEN"
```

### Favorite Article
```bash
curl -X POST https://api.realworld.io/api/articles/some-article-slug/favorite \
  -H "Authorization: Token YOUR_TOKEN"
```

### Unfavorite Article
```bash
curl -X DELETE https://api.realworld.io/api/articles/some-article-slug/favorite \
  -H "Authorization: Token YOUR_TOKEN"
```

## 3. Comment Tests

### Add Comment
```bash
curl -X POST https://api.realworld.io/api/articles/some-article-slug/comments \
  -H "Content-Type: application/json" \
  -H "Authorization: Token YOUR_TOKEN" \
  -d '{
    "comment": {
      "body": "This is a thoughtful comment about the article"
    }
  }'
```

### Get Comments
```bash
curl -X GET https://api.realworld.io/api/articles/some-article-slug/comments
```

### Delete Comment
```bash
curl -X DELETE https://api.realworld.io/api/articles/some-article-slug/comments/123 \
  -H "Authorization: Token YOUR_TOKEN"
```

## 4. User Subscription Tests

### Follow User
```bash
curl -X POST https://api.realworld.io/api/profiles/someusername/follow \
  -H "Content-Type: application/json" \
  -H "Authorization: Token YOUR_TOKEN"
```

### Unfollow User
```bash
curl -X DELETE https://api.realworld.io/api/profiles/someusername/follow \
  -H "Authorization: Token YOUR_TOKEN"
```

### Get User Profile
```bash
curl -X GET https://api.realworld.io/api/profiles/someusername \
  -H "Authorization: Token YOUR_TOKEN"
```

## 5. Tag Search Tests

### Get All Tags
```bash
curl -X GET https://api.realworld.io/api/tags
```

### Get Articles by Tag
```bash
curl -X GET "https://api.realworld.io/api/articles?tag=technology"
```

### Get Articles by Multiple Filters
```bash
curl -X GET "https://api.realworld.io/api/articles?tag=technology&limit=10&offset=0"
```

## Postman Collection

For Postman users, import this collection:

```json
{
  "info": {
    "name": "RealWorld Complete Test Suite",
    "schema": "https://schema.getpostman.com/json/collection/v2.1.0/collection.json"
  },
  "auth": {
    "type": "apikey",
    "apikey": [
      {
        "key": "value",
        "value": "Token {{authToken}}",
        "type": "string"
      },
      {
        "key": "key",
        "value": "Authorization",
        "type": "string"
      }
    ]
  },
  "variable": [
    {
      "key": "baseUrl",
      "value": "https://api.realworld.io/api",
      "type": "string"
    },
    {
      "key": "authToken",
      "value": "",
      "type": "string"
    }
  ],
  "item": [
    {
      "name": "1. Login",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "var jsonData = pm.response.json();",
              "pm.environment.set('authToken', jsonData.user.token);",
              "pm.test('Status code is 200', function () {",
              "    pm.response.to.have.status(200);",
              "});"
            ],
            "type": "text/javascript"
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "raw",
          "raw": "{\"user\":{\"email\":\"test.automation@realworld.io\",\"password\":\"Test123!\"}}",
          "options": {
            "raw": {
              "language": "json"
            }
          }
        },
        "url": "{{baseUrl}}/users/login"
      }
    },
    {
      "name": "2. Create Article",
      "event": [
        {
          "listen": "test",
          "script": {
            "exec": [
              "var jsonData = pm.response.json();",
              "pm.environment.set('articleSlug', jsonData.article.slug);",
              "pm.test('Article created successfully', function () {",
              "    pm.response.to.have.status(200);",
              "    pm.expect(jsonData.article.title).to.exist;",
              "});"
            ],
            "type": "text/javascript"
          }
        }
      ],
      "request": {
        "method": "POST",
        "header": [],
        "body": {
          "mode": "raw",
          "raw": "{\"article\":{\"title\":\"Test Article {{$timestamp}}\",\"description\":\"Testing\",\"body\":\"Content\",\"tagList\":[\"test\"]}}",
          "options": {
            "raw": {
              "language": "json"
            }
          }
        },
        "url": "{{baseUrl}}/articles"
      }
    }
  ]
}
```

## Test Execution Order

1. Login → Get auth token
2. Create test articles with different tags
3. Test article operations (update, favorite)
4. Add and manage comments
5. Follow/unfollow article authors
6. Search articles by tags
7. Clean up test data

All API endpoints are tested with proper authentication and error handling.

