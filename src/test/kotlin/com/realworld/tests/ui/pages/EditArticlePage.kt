package com.realworld.tests.ui.pages

import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide.`$`
import io.qameta.allure.Step

class EditArticlePage : BasePage("/editor") {
    
    private val titleInput = `$`("input[placeholder='Article Title']")
    private val descriptionInput = `$`("input[placeholder=\"What's this article about?\"]")
    private val bodyTextarea = `$`("textarea[placeholder='Write your article (in markdown)']")
    private val tagsInput = `$`("input[placeholder='Enter tags']")
    private val publishButton = `$`("button[type='submit']")
    
    override fun waitForPageLoad() {
        titleInput.shouldBe(visible)
        // Wait for values to be loaded
        titleInput.shouldNotBe(empty)
    }
    
    @Step("Update article")
    fun updateArticle(
        title: String? = null,
        description: String? = null,
        body: String? = null
    ): ArticlePage {
        logger.info { "Updating article" }
        
        title?.let {
            titleInput.shouldBe(visible).clear()
            titleInput.setValue(it)
        }
        
        description?.let {
            descriptionInput.shouldBe(visible).clear()
            descriptionInput.setValue(it)
        }
        
        body?.let {
            bodyTextarea.shouldBe(visible).clear()
            bodyTextarea.setValue(it)
        }
        
        publishButton.shouldBe(enabled).click()
        
        return ArticlePage()
    }
    
    @Step("Get current title")
    fun getCurrentTitle(): String {
        return titleInput.shouldBe(visible).value ?: ""
    }
    
    @Step("Get current description")
    fun getCurrentDescription(): String {
        return descriptionInput.shouldBe(visible).value ?: ""
    }
    
    @Step("Get current body")
    fun getCurrentBody(): String {
        return bodyTextarea.shouldBe(visible).value ?: ""
    }
}
