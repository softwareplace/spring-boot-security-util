package com.softwareplace.exception

class IllegalConstraintsException(message: String?, val errors: Map<String, List<String>>) : Exception(message)
