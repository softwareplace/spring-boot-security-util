package com.softwareplace.springsecurity.service

interface ScopeService {
    fun findScopes(ids: List<Long>): List<Pair<String, List<String>>>
    fun findScope(role: String): Pair<String, List<String>>

    companion object {
        fun List<Pair<String, List<String>>>.asSingSet(): Set<String> {
            val userScopes = mutableSetOf<String>()
            forEach { userScopes.addAll(it.second) }
            return userScopes
        }
    }
}
