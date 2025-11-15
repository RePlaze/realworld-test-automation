package com.realworld.tests.ui.pages

import com.codeborne.selenide.CollectionCondition
import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide.`$`
import com.codeborne.selenide.Selenide.`$$`
import com.codeborne.selenide.Selenide.sleep
import io.qameta.allure.Step
import java.time.Duration

class HomePage : BasePage("/") {
    
    private val globalFeedTab = `$`("a[href='#/'].nav-link")
    private val yourFeedTab = `$`("a[href='#/'].nav-link")
    private val popularTags = `$`(".sidebar .tag-list")
    private val tagLinks = `$$`(".sidebar .tag-list .tag-pill")
    private val articlePreviews = `$$`(".article-preview")
    private val newArticleLink = `$`("a[href='#/editor']")
    private val userMenuLink = `$`(".nav-link[href^='#/@']")
    
    override fun waitForPageLoad() {
        // Wait for the main content area to load
        `$`(".container").shouldBe(visible)
        // Wait for articles to load
        try {
            articlePreviews.shouldBe(CollectionCondition.sizeGreaterThan(0), Duration.ofSeconds(10))
        } catch (e: Exception) {
            logger.warn { "No articles found on page load, which may be expected" }
        }
    }
    
    @Step("Refresh page and wait for content")
    fun refreshAndWaitForContent(): HomePage {
        logger.info { "Refreshing page and waiting for content to load" }
        open()
        // Give extra time for content to appear after refresh
        sleep(2000)
        return this
    }
    
    @Step("Click on 'New Article' link")
    fun clickNewArticle(): NewArticlePage {
        logger.info { "Clicking on New Article link" }
        // Try multiple selectors to find the New Article link
        val newArticleSelector = try {
            `$`("a[href*='editor']").shouldBe(visible)
        } catch (e: Exception) {
            try {
                `$$`(".nav-link").find(text("New Article")).shouldBe(visible)
            } catch (e2: Exception) {
                `$$`("a").find(text("New Post")).shouldBe(visible)
            }
        }
        newArticleSelector.click()
        return NewArticlePage()
    }
    
    @Step("Wait for tag to appear in sidebar: {tag}")
    fun waitForTagToAppear(tag: String, timeoutSeconds: Int = 15): Boolean {
        logger.info { "Waiting for tag '$tag' to appear in sidebar" }
        var attempts = 0
        val maxAttempts = timeoutSeconds / 2
        
        while (attempts < maxAttempts) {
            try {
                // Refresh tags by reopening the page
                if (attempts > 0) {
                    logger.info { "Refreshing page to check for tag '$tag' (attempt ${attempts + 1})" }
                    refreshAndWaitForContent()
                }
                
                val visibleTags = getVisibleTags()
                if (visibleTags.contains(tag)) {
                    logger.info { "Tag '$tag' found in sidebar after ${attempts + 1} attempts" }
                    return true
                }
                
                logger.info { "Tag '$tag' not found. Available tags: ${visibleTags.take(10)}" }
                sleep(2000)
                attempts++
            } catch (e: Exception) {
                logger.warn { "Error checking for tag '$tag': ${e.message}" }
                sleep(2000)
                attempts++
            }
        }
        
        logger.error { "Tag '$tag' not found after $attempts attempts" }
        return false
    }
    
    @Step("Click on tag: {tag}")
    fun clickOnTag(tag: String): HomePage {
        logger.info { "Clicking on tag: $tag" }
        
        // First wait for the tag to appear
        if (!waitForTagToAppear(tag)) {
            throw AssertionError("Tag '$tag' not found in sidebar after waiting")
        }
        
        tagLinks.find(text(tag)).shouldBe(visible, Duration.ofSeconds(10)).click()
        
        // Wait for the page to update with filtered content
        sleep(1000)
        
        // Verify tag is active
        try {
            `$`(".nav-link.active").shouldHave(text(tag), Duration.ofSeconds(5))
        } catch (e: Exception) {
            logger.warn { "Active tag indicator not found, but continuing" }
        }
        
        return this
    }
    
    @Step("Get all visible tags")
    fun getVisibleTags(): List<String> {
        return try {
            // Ensure the tags section is loaded
            popularTags.shouldBe(visible, Duration.ofSeconds(10))
            
            val tags = tagLinks
                .shouldBe(CollectionCondition.sizeGreaterThan(0), Duration.ofSeconds(10))
                .map { it.text().trim() }
                .filter { it.isNotEmpty() }
            
            logger.info { "Found ${tags.size} visible tags" }
            tags
        } catch (e: Exception) {
            logger.warn { "No tags found or error getting tags: ${e.message}" }
            emptyList()
        }
    }
    
    @Step("Wait for article to appear: {title}")
    fun waitForArticleToAppear(title: String, timeoutSeconds: Int = 15): Boolean {
        logger.info { "Waiting for article '$title' to appear" }
        var attempts = 0
        val maxAttempts = timeoutSeconds / 3 // Reduce attempts but be more thorough
        
        while (attempts < maxAttempts) {
            try {
                if (attempts > 0) {
                    logger.info { "Refreshing page to check for article '$title' (attempt ${attempts + 1})" }
                    refreshAndWaitForContent()
                }
                
                val articles = getAllArticleTitles()
                if (articles.any { it.contains(title) }) {
                    logger.info { "Article '$title' found after ${attempts + 1} attempts" }
                    return true
                }
                
                logger.info { "Article '$title' not found in main feed. Available articles: ${articles.take(5)}" }
                
                // Try different approaches to find the article
                if (attempts > 1) {
                    // Try filtering by tags if the article has popular tags
                    val popularTags = getVisibleTags()
                    for (tag in popularTags.take(3)) { // Try first few popular tags
                        try {
                            logger.info { "Checking tag '$tag' for article '$title'" }
                            // Click on the tag and get articles (avoid recursive call)
                            clickOnTag(tag)
                            sleep(1000)
                            val tagArticles = getAllArticleTitles()
                            if (tagArticles.any { it.contains(title) }) {
                                logger.info { "Article '$title' found in tag '$tag'" }
                                return true
                            }
                        } catch (e: Exception) {
                            logger.warn { "Error checking tag '$tag': ${e.message}" }
                        }
                    }
                }
                
                sleep(3000) // Longer wait between attempts
                attempts++
            } catch (e: Exception) {
                logger.warn { "Error checking for article '$title': ${e.message}" }
                sleep(3000)
                attempts++
            }
        }
        
        logger.error { "Article '$title' not found after $attempts attempts in main feed or popular tags" }
        return false
    }
    
    @Step("Get all article titles")
    fun getAllArticleTitles(): List<String> {
        return try {
            articlePreviews
                .shouldBe(CollectionCondition.sizeGreaterThan(0), Duration.ofSeconds(10))
                .map { 
                    try {
                        it.`$`("h1").text().trim()
                    } catch (e: Exception) {
                        it.`$`(".preview-link h1").text().trim()
                    }
                }
                .filter { it.isNotEmpty() }
        } catch (e: Exception) {
            logger.warn { "No articles found or error getting article titles: ${e.message}" }
            emptyList()
        }
    }
    
    @Step("Get article count")
    fun getArticleCount(): Int {
        return try {
            articlePreviews.size()
        } catch (e: Exception) {
            logger.warn { "Error getting article count: ${e.message}" }
            0
        }
    }
    
    @Step("Click on article with title: {title}")
    fun clickOnArticle(title: String): ArticlePage {
        logger.info { "Clicking on article: $title" }
        
        // First wait for the article to appear
        if (!waitForArticleToAppear(title)) {
            throw AssertionError("Article '$title' not found after waiting")
        }
        
        try {
            articlePreviews
                .find(text(title))
                .shouldBe(visible, Duration.ofSeconds(10))
                .`$`("h1")
                .click()
        } catch (e: Exception) {
            // Try alternative selector
            articlePreviews
                .find(text(title))
                .shouldBe(visible, Duration.ofSeconds(10))
                .`$`(".preview-link h1")
                .click()
        }
        
        return ArticlePage()
    }
    
    @Step("Is user logged in")
    fun isUserLoggedIn(): Boolean {
        return try {
            userMenuLink.isDisplayed
        } catch (e: Exception) {
            false
        }
    }
    
    @Step("Get articles by tag")
    fun getArticlesByTag(tag: String): List<String> {
        clickOnTag(tag)
        
        // Wait for filtered content to load
        sleep(1000)
        
        return try {
            getAllArticleTitles()
        } catch (e: Exception) {
            logger.warn { "No articles found for tag '$tag': ${e.message}" }
            emptyList()
        }
    }
}
