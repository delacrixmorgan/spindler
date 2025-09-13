package io.dontsayboj.spindler.data.utils

interface Mapper<in Input, out Output> {
    operator suspend fun invoke(input: Input): Output
}