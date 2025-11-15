package com.realworld.tests.ui.pages

import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.`$$`
import io.qameta.allure.Step

class ArticlePage : BasePage("/article") {
    
    private val articleTitle = `$`(".banner h1")
    private val articleBody = `$`(".article-content")
    private val articleTags = `$$`(".tag-list .tag-pill")
    private val editButton = `$`("a.btn-outline-secondary")
    private val deleteButton = `$`("button.btn-outline-danger")
    private val favoriteButton = `$`("button.btn-primary, button.btn-outline-primary")
    private val followButton = `$`("button.btn-sm")
    private val commentTextarea = `$`("textarea[placeholder='Write a comment...']")
    private val postCommentButton = `$`("button[type='submit'].btn-primary")
    private val commentCards = `$$`(".comment-card")
    private val deleteCommentButtons = `$$`(".comment-card .mod-options .ion-trash-a")
    
    override fun waitForPageLoad() {
        articleTitle.shouldBe(visible)
    }
    
    @Step("Get article title")
    fun getTitle(): String {
        return articleTitle.shouldBe(visible).text()
    }
    
    @Step("Get article body")
    fun getBody(): String {
        return articleBody.shouldBe(visible).text()
    }
    
    @Step("Get article tags")
    fun getTags(): List<String> {
        return articleTags.texts()
    }
    
    @Step("Click edit article")
    fun clickEdit(): EditArticlePage {
        logger.info { "Clicking edit article" }
        editButton.shouldBe(visible).click()
        return EditArticlePage()
    }
    
    @Step("Delete article")
    fun deleteArticle(): HomePage {
        logger.info { "Deleting article" }
        deleteButton.shouldBe(visible).click()
        return HomePage()
    }
    
    @Step("Toggle favorite")
    fun toggleFavorite(): ArticlePage {
        logger.info { "Toggling favorite" }
        favoriteButton.shouldBe(visible).click()
        return this
    }
    
    @Step("Check if article is favorited")
    fun isFavorited(): Boolean {
        return favoriteButton
            .shouldBe(visible)
            .has(cssClass("btn-primary"))
    }
    
    @Step("Toggle follow author")
    fun toggleFollowAuthor(): ArticlePage {
        logger.info { "Toggling follow author" }
        followButton.shouldBe(visible).click()
        return this
    }
    
    @Step("Add comment: {comment}")
    fun addComment(comment: String): ArticlePage {
        logger.info { "Adding comment: $comment" }
        commentTextarea.shouldBe(visible).setValue(comment)
        postCommentButton.shouldBe(enabled).click()
        
        // Wait for comment to appear
        commentCards.shouldHave(CollectionCondition.sizeGreaterThan(0))
        return this
    }
    
    @Step("Get all comments")
    fun getComments(): List<String> {
        return commentCards
            .shouldBe(CollectionCondition.sizeGreaterThan(0))
            .map { it.`$`(".comment-text").text() }
    }
    
    @Step("Delete comment containing: {text}")
    fun deleteComment(text: String): ArticlePage {
        logger.info { "Deleting comment containing: $text" }
        
        val commentIndex = commentCards
            .indexOfFirst { it.`$`(".comment-text").text().contains(text) }
        
        if (commentIndex >= 0) {
            deleteCommentButtons[commentIndex].shouldBe(visible).click()
        }
        
        return this
    }
    
    @Step("Get comment count")
    fun getCommentCount(): Int {
        return commentCards.size()
    }
}
