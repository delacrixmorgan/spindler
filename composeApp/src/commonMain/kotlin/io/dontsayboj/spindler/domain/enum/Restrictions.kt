package io.dontsayboj.spindler.domain.enum

enum class Restrictions {
    CONFIDENTIAL,
    LOCKED,
    PRIVACY,
    NONE;

    companion object Companion {
        val Default = NONE
    }
}