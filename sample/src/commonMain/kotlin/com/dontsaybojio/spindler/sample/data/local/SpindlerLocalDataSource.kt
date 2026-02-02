package com.dontsaybojio.spindler.sample.data.local

import com.dontsaybojio.spindler.domain.model.GedcomIndex
import com.dontsaybojio.spindler.mapper.GedcomIndexDtoToModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import spindler.sample.generated.resources.Res

object SpindlerLocalDataSource {
    private val path: String = "files/sample.ged"
    private val gedcomIndexDtoToModelMapper: GedcomIndexDtoToModelMapper by lazy { GedcomIndexDtoToModelMapper() }

    suspend fun getData(): GedcomIndex {
        val text = Res.readBytes(path = path).decodeToString()
        return withContext(Dispatchers.Default) {
            gedcomIndexDtoToModelMapper(text)
        }
    }
}