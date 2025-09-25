package com.example.assignment3.domain.model

data class ProgressState(
    val done: Int,
    val total: Int
) {
    val percent: Int get() = if (total == 0) 0 else (done * 100 / total)
}
