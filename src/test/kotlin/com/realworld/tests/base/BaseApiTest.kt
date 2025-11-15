package com.realworld.tests.base

import com.realworld.tests.api.client.ApiClient
import com.realworld.tests.utils.TestDataGenerator
import io.qameta.allure.Allure
import mu.KotlinLogging
import org.testng.annotations.BeforeMethod

abstract class BaseApiTest {
    protected val logger = KotlinLogging.logger {}
    protected val api = ApiClient()
    protected val testData = TestDataGenerator
    
    @BeforeMethod
    fun baseSetup() {
        logger.info { "Starting test: ${this::class.simpleName}" }
        Allure.epic("RealWorld API Tests")
    }
    
}
