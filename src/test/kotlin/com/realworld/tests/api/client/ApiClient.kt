package com.realworld.tests.api.client

import com.realworld.tests.api.models.*
import com.realworld.tests.config.TestConfig
import io.qameta.allure.restassured.AllureRestAssured
import io.restassured.RestAssured
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import mu.KotlinLogging

class ApiClient {
    private val logger = KotlinLogging.logger {}
    private var authToken: String? = null
    
    init {
        RestAssured.baseURI = TestConfig.apiBaseUrl
        RestAssured.filters(
            AllureRestAssured(),
            RequestLoggingFilter(),
            ResponseLoggingFilter()
        )
    }
    
    fun authenticate(email: String = TestConfig.testUser.email, 
                    password: String = TestConfig.testUser.password): User {
        return try {
            // Try to login first
            loginUser(email, password)
        } catch (e: AssertionError) {
            // If login fails, register the user and then login
            logger.info { "Login failed, attempting to register user: $email" }
            registerUser(TestConfig.testUser.username, email, password)
        }
    }
    
    private fun loginUser(email: String, password: String): User {
        val payload = mapOf(
            "user" to LoginPayload(email, password)
        )
        
        return Given {
            contentType(ContentType.JSON)
            body(payload)
        } When {
            post("/users/login")
        } Then {
            statusCode(200)
        } Extract {
            val userResponse = `as`(UserResponse::class.java)
            authToken = userResponse.user.token
            userResponse.user
        }
    }
    
    private fun registerUser(username: String, email: String, password: String): User {
        val payload = mapOf(
            "user" to RegisterPayload(username, email, password)
        )
        
        return Given {
            contentType(ContentType.JSON)
            body(payload)
        } When {
            post("/users")
        } Then {
            statusCode(201)
        } Extract {
            val userResponse = `as`(UserResponse::class.java)
            authToken = userResponse.user.token
            logger.info { "Successfully registered user: $email" }
            userResponse.user
        }
    }
    
    fun createArticle(title: String, 
                     description: String, 
                     body: String, 
                     tags: List<String> = emptyList()): Article {
        val payload = mapOf(
            "article" to ArticleData(title, description, body, tags)
        )
        
        return authenticatedRequest()
            .body(payload) When {
            post("/articles")
        } Then {
            statusCode(201)
        } Extract {
            `as`(ArticleResponse::class.java).article
        }
    }
    
    fun updateArticle(slug: String, 
                     title: String? = null, 
                     description: String? = null, 
                     body: String? = null): Article {
        val payload = mapOf(
            "article" to ArticleData.Update(title, description, body)
        )
        
        return authenticatedRequest()
            .body(payload) When {
            put("/articles/$slug")
        } Then {
            statusCode(200)
        } Extract {
            `as`(ArticleResponse::class.java).article
        }
    }
    
    fun deleteArticle(slug: String): Response {
        return authenticatedRequest() When {
            delete("/articles/$slug")
        } Then {
            statusCode(200)
        } Extract {
            response()
        }
    }
    
    fun getArticle(slug: String): Article {
        return optionalAuthRequest() When {
            get("/articles/$slug")
        } Then {
            statusCode(200)
        } Extract {
            `as`(ArticleResponse::class.java).article
        }
    }
    
    fun getArticlesByTag(tag: String): ArticlesResponse {
        return Given {
            contentType(ContentType.JSON)
            authToken?.let { header("Authorization", "Token $it") }
            queryParam("tag", tag)
        } When {
            get("/articles")
        } Then {
            statusCode(200)
        } Extract {
            `as`(ArticlesResponse::class.java)
        }
    }
    
    fun favoriteArticle(slug: String): Article {
        return authenticatedRequest() When {
            post("/articles/$slug/favorite")
        } Then {
            statusCode(200)
        } Extract {
            `as`(ArticleResponse::class.java).article
        }
    }
    
    fun unfavoriteArticle(slug: String): Article {
        return authenticatedRequest() When {
            delete("/articles/$slug/favorite")
        } Then {
            statusCode(200)
        } Extract {
            `as`(ArticleResponse::class.java).article
        }
    }
    
    fun createComment(slug: String, body: String): Comment {
        val payload = mapOf(
            "comment" to mapOf("body" to body)
        )
        
        return authenticatedRequest()
            .body(payload) When {
            post("/articles/$slug/comments")
        } Then {
            statusCode(201)
        } Extract {
            `as`(CommentResponse::class.java).comment
        }
    }
    
    fun getComments(slug: String): List<Comment> {
        return optionalAuthRequest() When {
            get("/articles/$slug/comments")
        } Then {
            statusCode(200)
        } Extract {
            `as`(CommentsResponse::class.java).comments
        }
    }
    
    fun deleteComment(slug: String, commentId: Int): Response {
        return authenticatedRequest() When {
            delete("/articles/$slug/comments/$commentId")
        } Then {
            statusCode(200)
        } Extract {
            response()
        }
    }
    
    fun followUser(username: String): Response {
        return authenticatedRequest() When {
            post("/profiles/$username/follow")
        } Then {
            statusCode(200)
        } Extract {
            response()
        }
    }
    
    fun unfollowUser(username: String): Response {
        return authenticatedRequest() When {
            delete("/profiles/$username/follow")
        } Then {
            statusCode(200)
        } Extract {
            response()
        }
    }
    
    fun getTags(): List<String> {
        return When {
            get("/tags")
        } Then {
            statusCode(200)
        } Extract {
            `as`(TagsResponse::class.java).tags
        }
    }
    
    private fun authenticatedRequest(): RequestSpecification {
        return Given {
            contentType(ContentType.JSON)
            authToken?.let { 
                header("Authorization", "Token $it") 
            } ?: throw IllegalStateException("Authentication required")
        }
    }
    
    private fun optionalAuthRequest(): RequestSpecification {
        val spec = Given {
            contentType(ContentType.JSON)
        }
        authToken?.let { token ->
            spec.header("Authorization", "Token $token")
        }
        return spec
    }
}
