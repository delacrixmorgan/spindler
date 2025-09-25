package io.dontsayboj.spindler.sample.data.local

import io.dontsayboj.spindler.domain.model.GedcomIndex
import io.dontsayboj.spindler.mapper.GedcomIndexDtoToModelMapper
import spindler.composeapp.generated.resources.Res

object SpindlerLocalDataSource {
    private val path: String = "files/sample.ged"
    private val gedcomIndexDtoToModelMapper: GedcomIndexDtoToModelMapper by lazy { GedcomIndexDtoToModelMapper() }

    suspend fun getData(): GedcomIndex {
        val text = Res.readBytes(path = path).decodeToString()
        return gedcomIndexDtoToModelMapper(text)
    }
}