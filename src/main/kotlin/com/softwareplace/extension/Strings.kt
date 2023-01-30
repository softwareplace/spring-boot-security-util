package com.softwareplace.extension


fun String.addAtStartAsCamelCase(value: String): String {
    val firstLetter = substring(0, 1).toUpperCase()
    return "$value$firstLetter${substring(1, length)}"
}
