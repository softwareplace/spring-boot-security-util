package com.softwareplace.extension

import java.util.*


fun String.addAtStartAsCamelCase(value: String): String {
    val firstLetter = substring(0, 1).uppercase(Locale.getDefault())
    return "$value$firstLetter${substring(1, length)}"
}

fun String.asPathRegex(): Regex {
    val pattern = replaceFirst("/**", ".*")
        .replace("**", ".*")
    return Regex(pattern)
}
