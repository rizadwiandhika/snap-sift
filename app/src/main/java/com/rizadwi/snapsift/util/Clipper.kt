package com.rizadwi.snapsift.util

import javax.inject.Inject

class Clipper @Inject constructor() {
    fun clip(text: String, maxLength: Int): String {
        if (text.length <= maxLength) {
            return text
        }

        val truncated = text.slice(IntRange(0, maxLength - 1 - SUFFIX.length))
        return truncated + SUFFIX
    }

    companion object {
        const val SUFFIX = "..."
    }
}