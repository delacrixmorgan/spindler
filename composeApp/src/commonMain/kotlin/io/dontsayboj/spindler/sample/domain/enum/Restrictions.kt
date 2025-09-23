package io.dontsayboj.spindler.sample.domain.enum

enum class Restrictions {
    CONFIDENTIAL,
    LOCKED,
    PRIVACY,
    NONE;

    companion object Companion {
        val Default = NONE
    }
}