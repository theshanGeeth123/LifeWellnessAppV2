package com.example.assignment3.domain.model

import java.util.UUID

data class Habit(
    val id: String = UUID.randomUUID().toString(),
    var title: String
)
