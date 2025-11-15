package com.realworld.tests.utils

import io.qameta.allure.Allure
import io.qameta.allure.Attachment
import io.qameta.allure.model.Status

object AllureHelper {
    
    @Attachment(value = "{name}", type = "text/plain")
    fun attachText(name: String, content: String): String {
        return content
    }
    
    @Attachment(value = "{name}", type = "application/json")
    fun attachJson(name: String, json: String): String {
        return json
    }
    
    fun step(description: String, action: () -> Unit) {
        Allure.step(description, Allure.ThrowableRunnableVoid {
            try {
                action()
            } catch (e: Exception) {
                attachText("Error Details", e.stackTraceToString())
                throw e
            }
        })
    }
    
    fun <T> step(description: String, action: () -> T): T {
        return Allure.step(description, Allure.ThrowableRunnable<T> {
            try {
                action()
            } catch (e: Exception) {
                attachText("Error Details", e.stackTraceToString())
                throw e
            }
        })
    }
    
    fun addTestInfo(vararg pairs: Pair<String, String>) {
        pairs.forEach { (key, value) ->
            Allure.parameter(key, value)
        }
    }
    
    fun markTestStatus(status: Status, reason: String? = null) {
        Allure.getLifecycle().updateTestCase { testResult ->
            testResult.status = status
            reason?.let { testResult.statusDetails?.message = it }
        }
    }
}
