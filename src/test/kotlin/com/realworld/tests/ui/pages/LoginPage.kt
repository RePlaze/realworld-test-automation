package com.realworld.tests.ui.pages

import com.codeborne.selenide.Condition.*
import com.codeborne.selenide.Selenide.`$`
import io.qameta.allure.Step

class LoginPage : BasePage("/login") {
    
    private val emailInput = `$`("input[placeholder='Email']")
    private val passwordInput = `$`("input[placeholder='Password']")
    private val signInButton = `$`("button[type='submit']")
    private val errorMessages = `$`(".error-messages")
    
    override fun waitForPageLoad() {
        emailInput.shouldBe(visible)
    }
    
    @Step("Login with email: {email}")
    fun login(email: String, password: String): HomePage {
        logger.info { "Logging in with email: $email" }
        
        emailInput.shouldBe(visible).setValue(email)
        passwordInput.shouldBe(visible).setValue(password)
        signInButton.shouldBe(enabled).click()
        
        return HomePage()
    }
    
    fun getErrorMessage(): String {
        return errorMessages.shouldBe(visible).text()
    }
    
    fun isErrorDisplayed(): Boolean {
        return errorMessages.isDisplayed
    }
}
