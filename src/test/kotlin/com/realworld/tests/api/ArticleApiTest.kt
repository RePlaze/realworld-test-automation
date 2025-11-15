package com.realworld.tests.api

import com.realworld.tests.base.BaseApiTest
import io.qameta.allure.Description
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@Feature("Article Management")
class ArticleApiTest : BaseApiTest() {
    
    @BeforeClass
    fun authenticateUser() {
        api.authenticate()
    }
    
    @Test
    @Story("Create Article")
    @Description("""
        Given I am authenticated as a valid user
        When I create a new article with valid data
        Then the article should be created successfully
        And all provided data should be saved correctly
    """)
    fun `Should create article with all required fields`() {
        // Given: Prepare unique article data
        val title = testData.uniqueTitle("Amazing Article")
        val description = testData.randomDescription()
        val body = testData.customBody("Technology Innovation")
        val tags = testData.specificTags("technology", "innovation", "tutorial")
        
        // When: Create the article
        val article = api.createArticle(title, description, body, tags)
        
        // Then: Verify article was created with correct data
        assertEquals(title, article.title, "Article title should match")
        assertEquals(description, article.description, "Article description should match")
        assertEquals(body, article.body, "Article body should match")
        assertEquals(tags, article.tagList, "Article tags should match")
        assertNotNull(article.slug, "Article should have a slug")
        assertNotNull(article.createdAt, "Article should have creation timestamp")
    }
    
    @Test
    @Story("Update Article")
    @Description("""
        Given I have created an article
        When I update specific fields of the article
        Then only the updated fields should change
        And other fields should remain unchanged
    """)
    fun `Should update article preserving unchanged fields`() {
        // Given: Create an article to update
        val originalTitle = testData.uniqueTitle("Original Title")
        val originalDescription = testData.randomDescription()
        val originalBody = testData.randomBody()
        val article = api.createArticle(originalTitle, originalDescription, originalBody)
        
        // When: Update only the title
        val updatedTitle = testData.uniqueTitle("Updated Title")
        val updatedArticle = api.updateArticle(article.slug, title = updatedTitle)
        
        // Then: Verify only title changed
        assertEquals(updatedTitle, updatedArticle.title, "Title should be updated")
        assertEquals(originalDescription, updatedArticle.description, "Description should remain unchanged")
        assertEquals(originalBody, updatedArticle.body, "Body should remain unchanged")
        // Note: Some RealWorld implementations regenerate slug when title changes
        // This is acceptable behavior, so we don't enforce slug immutability
        // assertEquals(article.slug, updatedArticle.slug, "Slug should remain unchanged")
    }
    
    @Test
    @Story("Delete Article")
    @Description("""
        Given I have created an article
        When I delete the article
        Then the article should be removed from the system
        And subsequent requests for this article should fail
    """)
    fun `Should delete article permanently`() {
        // Given: Create an article to delete
        val title = testData.uniqueTitle("Article to Delete")
        val article = api.createArticle(title, testData.randomDescription(), testData.randomBody())
        
        // When: Delete the article
        val deleteResponse = api.deleteArticle(article.slug)
        
        // Then: Verify deletion was successful
        assertEquals(200, deleteResponse.statusCode, "Delete should return 200 OK")
        
        // And: Verify article no longer exists
        try {
            api.getArticle(article.slug)
            assertTrue(false, "Getting deleted article should fail")
        } catch (e: AssertionError) {
            // Expected - article should not be found
            assertTrue(e.message?.contains("404") == true, "Should return 404 for deleted article")
        }
    }
    
    @Test
    @Story("Favorite Article")
    @Description("""
        Given I have created an article
        When I favorite the article
        Then the article should be marked as favorited
        And the favorite count should increase
    """)
    fun `Should favorite and unfavorite article`() {
        // Given: Create an article
        val article = api.createArticle(
            testData.uniqueTitle("Interesting Article"),
            testData.randomDescription(),
            testData.customBody("Interesting Content")
        )
        
        // When: Favorite the article
        val favoritedArticle = api.favoriteArticle(article.slug)
        
        // Then: Verify article is favorited
        assertTrue(favoritedArticle.favorited, "Article should be favorited")
        assertEquals(1, favoritedArticle.favoritesCount, "Favorite count should be 1")
        
        // When: Unfavorite the article
        val unfavoritedArticle = api.unfavoriteArticle(article.slug)
        
        // Then: Verify article is no longer favorited
        assertTrue(!unfavoritedArticle.favorited, "Article should not be favorited")
        assertEquals(0, unfavoritedArticle.favoritesCount, "Favorite count should be 0")
    }
    
    @Test
    @Story("Article Comments")
    @Description("""
        Given I have created an article
        When I add comments to the article
        Then the comments should be visible on the article
        And I should be able to delete my own comments
    """)
    fun `Should manage article comments`() {
        // Given: Create an article
        val article = api.createArticle(
            testData.uniqueTitle("Article for Comments"),
            testData.randomDescription(),
            testData.customBody("Discussion Topics")
        )
        
        // When: Add a comment
        val commentText = testData.randomComment()
        val comment = api.createComment(article.slug, commentText)
        
        // Then: Verify comment was created
        assertEquals(commentText, comment.body, "Comment text should match")
        assertNotNull(comment.id, "Comment should have an ID")
        assertNotNull(comment.author, "Comment should have an author")
        
        // When: Get all comments
        val comments = api.getComments(article.slug)
        
        // Then: Verify our comment is in the list
        assertTrue(comments.any { it.id == comment.id }, "Comment should be in the list")
        
        // When: Delete the comment
        val deleteResponse = api.deleteComment(article.slug, comment.id)
        
        // Then: Verify deletion was successful
        assertEquals(200, deleteResponse.statusCode, "Delete comment should return 200")
        
        // And: Verify comment is removed
        val remainingComments = api.getComments(article.slug)
        assertTrue(remainingComments.none { it.id == comment.id }, "Comment should be deleted")
    }
}
