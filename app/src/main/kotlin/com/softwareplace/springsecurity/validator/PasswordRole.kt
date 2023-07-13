package com.softwareplace.springsecurity.validator

import com.softwareplace.springsecurity.validator.role.RuleBuilder
import org.passay.LengthRule
import org.passay.Rule

class PasswordRole : RuleBuilder {
    override fun lengthRule(): Rule {
        return LengthRule(6, 60)
    }
}
