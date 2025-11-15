package com.realworld.tests.base

import com.codeborne.selenide.Configuration
import com.codeborne.selenide.Selenide
import com.codeborne.selenide.logevents.SelenideLogger
import com.realworld.tests.config.TestConfig
import com.realworld.tests.utils.TestDataGenerator
import io.qameta.allure.Allure
import io.qameta.allure.selenide.AllureSelenide
import mu.KotlinLogging
import org.testng.annotations.AfterMethod
import org.testng.annotations.BeforeMethod
import org.testng.annotations.BeforeClass

abstract class BaseUiTest {
    protected val logger = KotlinLogging.logger {}
    protected val testData = TestDataGenerator
    
    /**
     * Wait for API data to sync with UI.
     * This method should be called after creating data via API but before interacting with UI.
     */
    protected fun waitForApiDataSync(delaySeconds: Int = 3) {
        logger.info { "Waiting ${delaySeconds}s for API data to sync with UI" }
        Thread.sleep(delaySeconds * 1000L)
    }
    
    @BeforeClass
    fun setupClass() {
        SelenideLogger.addListener("allure", AllureSelenide())
        
        Configuration.baseUrl = TestConfig.appBaseUrl
        Configuration.browser = TestConfig.browser.type
        Configuration.headless = TestConfig.browser.headless
        Configuration.browserSize = "${TestConfig.browser.windowWidth}x${TestConfig.browser.windowHeight}"
        Configuration.timeout = TestConfig.timeouts.implicit * 1000
        Configuration.pageLoadTimeout = TestConfig.timeouts.pageLoad * 1000
    }
    
    @BeforeMethod
    fun setupMethod() {
        logger.info { "Starting test: ${this::class.simpleName}" }
        Allure.epic("RealWorld UI Tests")
    }
    
    @AfterMethod
    fun tearDown() {
        Selenide.closeWebDriver()
    }
    
}
