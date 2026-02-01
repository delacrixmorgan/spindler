package com.dontsaybojio.spindler.sample.data.local

import com.dontsaybojio.spindler.domain.model.GedcomIndex
import com.dontsaybojio.spindler.mapper.GedcomIndexDtoToModelMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SpindlerLocalDataSource {
    private val path: String = "files/sample.ged"
    private val gedcomIndexDtoToModelMapper: GedcomIndexDtoToModelMapper by lazy { GedcomIndexDtoToModelMapper() }

    suspend fun getData(): GedcomIndex {
        // Android target unable to read sample/commonMain/file/sample.ged
//        val text = Res.readBytes(path).decodeToString()
        val text = mockedGEDSample

        return withContext(Dispatchers.Default) {
            gedcomIndexDtoToModelMapper(text)
        }
    }
}
