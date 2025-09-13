package io.dontsayboj.spindler.data

import io.dontsayboj.spindler.Gedcom
import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.Individual
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SpindlerRepository {
    private val _individuals = MutableStateFlow<List<Individual>>(emptyList())
    private val _families = MutableStateFlow<List<Family>>(emptyList())
    private val isDataLoaded: Boolean get() = _individuals.value.isNotEmpty() || _families.value.isNotEmpty()

    val individuals: StateFlow<List<Individual>> = _individuals.asStateFlow()
    val families: StateFlow<List<Family>> = _families.asStateFlow()

    suspend fun loadData(path: String = "files/sample.ged") {
        if (isDataLoaded) return
        val (_, index) = Gedcom.parseFile(path)
        _individuals.value = index.individuals.values.toList()
        _families.value = index.families.values.toList()
    }

    fun getIndividual(id: String?): Individual? {
        return individuals.value.find { it.id == id }
    }

    fun getFamily(id: String?): Family? {
        return families.value.find { it.id == id }
    }
}
