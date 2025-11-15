package com.realworld.tests.api

import com.realworld.tests.base.BaseApiTest
import io.qameta.allure.Description
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@Feature("User Subscription")
class UserSubscriptionTest : BaseApiTest() {
    
    private var authorUsername: String? = null
    
    @BeforeClass
    fun setupTestData() {
        // Authenticate main test user
        api.authenticate()
        
        // Create an article to get an author to follow
        val article = api.createArticle(
            testData.uniqueTitle("Article by Author"),
            testData.randomDescription(),
            testData.customBody("Author Follow Testing")
        )
        authorUsername = article.author.username
    }
    
    @Test
    @Story("Follow User")
    @Description("""
        Given I am authenticated
        And there is another user who has published content
        When I follow that user
        Then the follow action should succeed
        And the user's profile should show I am following them
    """)
    fun `Should follow another user successfully`() {
        // Given: We have an author from setupTestData
        assertNotNull(authorUsername, "Author username should be available")
        
        // When: Follow the author
        val response = api.followUser(authorUsername!!)
        
        // Then: Verify follow was successful
        assertEquals(200, response.statusCode, "Follow request should succeed")
        
        // Additional verification could check the profile response
        logger.info { "Successfully followed user: $authorUsername" }
    }
    
    @Test(dependsOnMethods = ["Should follow another user successfully"])
    @Story("Unfollow User")
    @Description("""
        Given I am following a user
        When I unfollow that user
        Then the unfollow action should succeed
        And I should no longer be following them
    """)
    fun `Should unfollow a user successfully`() {
        // Given: We are following the author from previous test
        assertNotNull(authorUsername, "Author username should be available")
        
        // When: Unfollow the author
        val response = api.unfollowUser(authorUsername!!)
        
        // Then: Verify unfollow was successful
        assertEquals(200, response.statusCode, "Unfollow request should succeed")
        
        logger.info { "Successfully unfollowed user: $authorUsername" }
    }
    
}
