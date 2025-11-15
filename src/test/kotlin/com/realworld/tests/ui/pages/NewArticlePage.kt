package com.realworld.tests.ui.pages

import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide.`$`
import io.qameta.allure.Step

class NewArticlePage : BasePage("/editor") {
    
    private val titleInput = `$`("input[placeholder='Article Title']")
    private val descriptionInput = `$`("input[placeholder=\"What's this article about?\"]")
    private val bodyTextarea = `$`("textarea[placeholder='Write your article (in markdown)']")
    private val tagsInput = `$`("input[placeholder='Enter tags']")
    private val publishButton = `$`("button[type='submit']")
    private val errorMessages = `$`(".error-messages")
    
    override fun waitForPageLoad() {
        titleInput.shouldBe(visible)
    }
    
    @Step("Create article with title: {title}")
    fun createArticle(
        title: String,
        description: String,
        body: String,
        tags: List<String> = emptyList()
    ): ArticlePage {
        logger.info { "Creating article: $title" }
        
        fillArticleForm(title, description, body, tags)
        publishButton.shouldBe(enabled).click()
        
        return ArticlePage()
    }
    
    @Step("Fill article form")
    fun fillArticleForm(
        title: String,
        description: String,
        body: String,
        tags: List<String>
    ): NewArticlePage {
        titleInput.shouldBe(visible).setValue(title)
        descriptionInput.shouldBe(visible).setValue(description)
        bodyTextarea.shouldBe(visible).setValue(body)
        
        tags.forEach { tag ->
            tagsInput.shouldBe(visible)
                .setValue(tag)
                .pressEnter()
        }
        
        return this
    }
    
    fun isPublishButtonEnabled(): Boolean {
        return publishButton.isEnabled
    }
    
    fun getErrorMessage(): String {
        return errorMessages.shouldBe(visible).text()
    }
}
