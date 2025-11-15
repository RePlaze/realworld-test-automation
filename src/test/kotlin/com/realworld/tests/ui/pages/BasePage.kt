package com.realworld.tests.ui.pages

import com.codeborne.selenide.Selenide
import com.realworld.tests.config.TestConfig
import io.qameta.allure.Step
import mu.KotlinLogging

abstract class BasePage(val path: String) {
    protected val logger = KotlinLogging.logger {}
    
    val url: String
        get() = "${TestConfig.appBaseUrl}/#$path"
    
    @Step("Open {this.path} page")
    open fun open(): BasePage {
        logger.info { "Opening page: $url" }
        Selenide.open(url)
        waitForPageLoad()
        return this
    }
    
    protected open fun waitForPageLoad() {
        // Override in subclasses if needed
    }
    
    fun atPage(): Boolean {
        val currentUrl = Selenide.webdriver().driver().url()
        return currentUrl.contains(path)
    }
}
