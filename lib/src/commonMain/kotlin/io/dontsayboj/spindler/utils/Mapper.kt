package io.dontsayboj.spindler.utils

interface Mapper<in Input, out Output> {
    suspend operator fun invoke(input: Input): Output
}