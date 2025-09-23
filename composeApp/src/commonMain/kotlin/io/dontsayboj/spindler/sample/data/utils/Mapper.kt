package io.dontsayboj.spindler.sample.data.utils

interface Mapper<in Input, out Output> {
    suspend operator fun invoke(input: Input): Output
}