package com.realworld.tests.ui

import com.realworld.tests.api.client.ApiClient
import com.realworld.tests.base.BaseUiTest
import com.realworld.tests.config.TestConfig
import com.realworld.tests.ui.pages.*
import io.qameta.allure.Description
import io.qameta.allure.Feature
import io.qameta.allure.Story
import org.testng.annotations.BeforeMethod
import org.testng.annotations.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Feature("Article Management UI")
class ArticleUiTest : BaseUiTest() {
    
    private lateinit var loginPage: LoginPage
    private lateinit var homePage: HomePage
    private val api = ApiClient()
    
    @BeforeMethod
    fun loginBeforeTest() {
        // Ensure test user exists via API
        api.authenticate()
        
        // Login via UI
        loginPage = LoginPage()
        loginPage.open()
        homePage = loginPage.login(TestConfig.testUser.email, TestConfig.testUser.password)
    }
    
    
    
    
}
