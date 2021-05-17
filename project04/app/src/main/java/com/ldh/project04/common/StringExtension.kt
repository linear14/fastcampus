package com.ldh.project04

import java.lang.NumberFormatException

fun String.isNumber(): Boolean =
    try {
        this.toBigInteger()
        true
    } catch(e: NumberFormatException) {
        false
    }
