package com.realworld.tests.api

import com.realworld.tests.base.BaseApiTest
import io.qameta.allure.Description
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.testng.annotations.BeforeClass
import org.testng.annotations.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

@Feature("Tag Management")
class TagApiTest : BaseApiTest() {
    
    @BeforeClass
    fun authenticateUser() {
        api.authenticate()
    }
    
    @Test
    @Story("Get Tags")
    @Description("""
        Given the system has articles with tags
        When I request all available tags
        Then I should receive a list of unique tags
        And the list should contain commonly used tags
    """)
    fun `Should retrieve all available tags`() {
        // When: Get all tags
        val tags = api.getTags()
        
        // Then: Verify tags are returned
        assertTrue(tags.isNotEmpty(), "Tags list should not be empty")
        logger.info { "Found ${tags.size} tags: ${tags.take(10).joinToString()}" }
        
        // Verify tags are unique
        assertEquals(tags.size, tags.distinct().size, "All tags should be unique")
    }
    
    @Test
    @Story("Filter Articles by Tag")
    @Description("""
        Given I create articles with specific tags
        When I filter articles by a tag
        Then I should only receive articles containing that tag
    """)
    fun `Should filter articles by tag correctly`() {
        // Given: Create articles with specific tags
        val uniqueTag = testData.generateUnique("unique-tag")
        val commonTag = "testing"
        
        // Create multiple articles with our unique tag
        val article1 = api.createArticle(
            testData.uniqueTitle("Tagged Article 1"),
            testData.randomDescription(),
            testData.customBody("Tag Testing"),
            listOf(uniqueTag, commonTag)
        )
        
        val article2 = api.createArticle(
            testData.uniqueTitle("Tagged Article 2"),
            testData.randomDescription(),
            testData.customBody("Tag Verification"),
            listOf(uniqueTag, "kotlin")
        )
        
        // Create an article without our unique tag
        val articleWithoutTag = api.createArticle(
            testData.uniqueTitle("Different Article"),
            testData.randomDescription(),
            testData.randomBody(),
            testData.specificTags("other", "unrelated")
        )
        
        // When: Get articles by our unique tag
        val articlesResponse = api.getArticlesByTag(uniqueTag)
        
        // Then: Verify only articles with the tag are returned
        val articleSlugs = articlesResponse.articles.map { it.slug }
        assertTrue(articleSlugs.contains(article1.slug), "Should contain first article with tag")
        assertTrue(articleSlugs.contains(article2.slug), "Should contain second article with tag")
        assertTrue(!articleSlugs.contains(articleWithoutTag.slug), "Should not contain article without tag")
        
        // Verify all returned articles have the requested tag
        articlesResponse.articles.forEach { article ->
            assertTrue(
                article.tagList.contains(uniqueTag), 
                "Article '${article.title}' should contain tag '$uniqueTag'"
            )
        }
    }
    
    @Test
    @Story("Tag Usage in Articles")
    @Description("""
        Given I create an article with multiple tags
        When the article is created
        Then all tags should be properly associated
        And tags should be available in the global tag list
    """)
    fun `Should properly associate tags with articles`() {
        // Given: Define unique tags
        val uniqueTags = listOf(
            testData.generateUnique("tag"),
            testData.generateUnique("another-tag"),
            testData.generateUnique("third-tag")
        )
        
        // When: Create article with these tags
        val article = api.createArticle(
            testData.uniqueTitle("Multi-Tagged Article"),
            testData.randomDescription(),
            testData.customBody("Tag Management"),
            uniqueTags
        )
        
        // Then: Verify all tags are associated with the article
        assertEquals(uniqueTags.sorted(), article.tagList.sorted(), "All tags should be saved with article")
        
        // When: Get the article
        val retrievedArticle = api.getArticle(article.slug)
        
        // Then: Verify tags persist
        assertEquals(uniqueTags.sorted(), retrievedArticle.tagList.sorted(), "Tags should persist when retrieving article")
        
        // Verify each tag can be used to find the article
        uniqueTags.forEach { tag ->
            val articlesByTag = api.getArticlesByTag(tag)
            assertTrue(
                articlesByTag.articles.any { it.slug == article.slug },
                "Article should be findable by tag '$tag'"
            )
        }
    }
}
