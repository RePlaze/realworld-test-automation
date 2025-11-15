package com.realworld.tests.utils

import kotlin.random.Random

object TestDataGenerator {
    
    private val titlePrefixes = listOf("Amazing", "Great", "New", "Updated", "Best", "Innovative", "Essential", "Ultimate")
    private val titleSuffixes = listOf("Article", "Post", "Tutorial", "Guide", "Handbook", "Overview", "Deep Dive")
    
    private val descriptions = listOf(
        "A comprehensive look at modern development practices",
        "Exploring the latest technology trends and innovations", 
        "Best practices every developer should know",
        "Quick guide for efficient development workflows",
        "Learn something new and valuable today",
        "Essential insights for professional growth",
        "Practical approaches to complex problems"
    )
    
    private val availableTags = listOf(
        "programming", "testing", "kotlin", "automation", "dev", "tutorial", 
        "tips", "coding", "backend", "frontend", "api", "ui", "ux", "devops"
    )
    
    private val commentBodies = listOf(
        "Great article! Very insightful.",
        "Thanks for sharing this valuable information",
        "Very helpful, learned something new",
        "Good stuff, well explained",
        "Nice explanation, clear and concise",
        "Excellent points, thanks for the details",
        "This is exactly what I was looking for"
    )
    
    fun generateUnique(prefix: String): String {
        return "$prefix-${System.currentTimeMillis()}-${Random.nextInt(1000, 9999)}"
    }
    
    fun randomTitle(): String = buildString {
        val prefix = titlePrefixes.random()
        val suffix = titleSuffixes.random()
        append("$prefix $suffix ${System.currentTimeMillis()}")
    }
    
    fun uniqueTitle(prefix: String = "Article"): String {
        return generateUnique(prefix)
    }
    
    fun randomDescription(): String = descriptions.random()
    
    fun randomBody(): String = """
# Introduction

This is auto-generated content for testing purposes. The article explores various aspects of modern software development.

## Key Points

- **Quality**: Focus on writing clean, maintainable code
- **Testing**: Comprehensive test coverage ensures reliability
- **Automation**: Streamline repetitive tasks for efficiency
- **Collaboration**: Work effectively with team members

## Conclusion

These practices help create better software and improve development workflows.

*Generated: ${System.currentTimeMillis()}*
    """.trimIndent()
    
    fun customBody(topic: String): String = """
# $topic

This article covers important aspects of $topic in modern software development.

## Overview

Understanding $topic is crucial for building robust applications.

## Best Practices

1. Follow established conventions
2. Write comprehensive tests
3. Document your work
4. Seek feedback from peers

*Generated: ${System.currentTimeMillis()}*
    """.trimIndent()
    
    fun randomTags(count: Int = 3): List<String> {
        require(count <= availableTags.size) { "Cannot request more tags than available" }
        return availableTags.shuffled().take(count)
    }
    
    fun specificTags(vararg tags: String): List<String> = tags.toList()
    
    fun randomComment(): String = commentBodies.random()
    
    fun randomEmail(): String = "user-${System.currentTimeMillis()}-${Random.nextInt(1000, 9999)}@example.com"
    
    fun randomUsername(): String = "user${System.currentTimeMillis()}${Random.nextInt(100, 999)}"
    
    fun randomPassword(): String = "TestPass${Random.nextInt(1000, 9999)}!"
}
