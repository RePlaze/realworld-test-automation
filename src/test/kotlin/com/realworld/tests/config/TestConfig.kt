package com.realworld.tests.config

import java.util.Properties

object TestConfig {
    private val props = Properties().also { props ->
        val stream = javaClass.getResourceAsStream("/test.properties")
            ?: error("Can't find test.properties")
        props.load(stream)
    }

    val appBaseUrl = prop("app.base.url")
    val apiBaseUrl = prop("api.base.url")
    
    val testUser = TestUser(
        email = prop("test.user.email"),
        username = prop("test.user.username"), 
        password = prop("test.user.password")
    )
    
    val timeouts = Timeouts(
        implicit = prop("timeout.implicit").toLong(),
        explicit = prop("timeout.explicit").toLong(),
        pageLoad = prop("timeout.page.load").toLong()
    )
    
    val browser = BrowserConfig(
        type = prop("browser.type"),
        headless = prop("browser.headless").toBoolean(),
        windowWidth = prop("browser.window.width").toInt(),
        windowHeight = prop("browser.window.height").toInt()
    )
    
    private fun prop(key: String): String = 
        System.getProperty(key) ?: props.getProperty(key) 
        ?: error("Missing property: $key")
}

data class TestUser(
    val email: String,
    val username: String,
    val password: String
)

data class Timeouts(
    val implicit: Long,
    val explicit: Long,
    val pageLoad: Long
)

data class BrowserConfig(
    val type: String,
    val headless: Boolean,
    val windowWidth: Int,
    val windowHeight: Int
)
