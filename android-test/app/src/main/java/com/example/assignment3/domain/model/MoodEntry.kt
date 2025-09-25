package com.example.assignment3.domain.model

import java.util.UUID

data class MoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val timestampMillis: Long,   // System.currentTimeMillis()
    val emoji: String,           // e.g., "😊"
    val note: String = ""
)
