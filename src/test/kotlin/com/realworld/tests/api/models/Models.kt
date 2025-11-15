package com.realworld.tests.api.models

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

// auth payloads
data class LoginPayload(
    val email: String,
    val password: String
)

data class RegisterPayload(
    val username: String,
    val email: String,
    val password: String
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class UserResponse(
    val user: User
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class User(
    val email: String,
    val token: String,
    val username: String,
    val bio: String? = null,
    val image: String? = null
)

// article stuff
data class ArticleData(
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String> = emptyList()
) {
    // for update operations
    data class Update(
        val title: String? = null,
        val description: String? = null,
        val body: String? = null
    )
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class ArticleResponse(
    val article: Article
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class ArticlesResponse(
    val articles: List<Article>,
    val articlesCount: Int
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Article(
    val slug: String,
    val title: String,
    val description: String,
    val body: String,
    val tagList: List<String>,
    val createdAt: String,
    val updatedAt: String,
    val favorited: Boolean = false,
    val favoritesCount: Int = 0,
    val author: Author
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class Author(
    val username: String,
    val bio: String? = null,
    val image: String? = null,
    val following: Boolean = false
)

// comments
@JsonIgnoreProperties(ignoreUnknown = true) 
data class Comment(
    val id: Int,
    val createdAt: String,
    val updatedAt: String,
    val body: String,
    val author: Author
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentResponse(
    val comment: Comment
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class CommentsResponse(
    val comments: List<Comment>
)

// misc
@JsonIgnoreProperties(ignoreUnknown = true)
data class TagsResponse(
    val tags: List<String>
)
