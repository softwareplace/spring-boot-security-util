package com.softwareplace.springsecurity.exception

class IllegalConstraintsException(message: String?, val errors: Map<String, List<String>>) : Exception(message)
