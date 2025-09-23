package io.dontsayboj.spindler.sample.data

import io.dontsayboj.spindler.domain.model.Family
import io.dontsayboj.spindler.domain.model.Individual
import io.dontsayboj.spindler.sample.data.local.SpindlerLocalDataSource
import io.dontsayboj.spindler.sample.data.remote.RemoteDataSourceException
import io.dontsayboj.spindler.sample.data.remote.SpindlerRemoteDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object SpindlerRepository {
    private val _individuals = MutableStateFlow<List<Individual>>(emptyList())
    private val _families = MutableStateFlow<List<Family>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val isDataLoaded: Boolean get() = _individuals.value.isNotEmpty() || _families.value.isNotEmpty()

    val individuals: StateFlow<List<Individual>> = _individuals.asStateFlow()
    val families: StateFlow<List<Family>> = _families.asStateFlow()
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    val error: StateFlow<String?> = _error.asStateFlow()

    /**
     * Load data from local source (existing behavior)
     */
    suspend fun getLocalData() {
        if (isDataLoaded) return

        _isLoading.value = true
        _error.value = null

        try {
            val index = SpindlerLocalDataSource.getData()
            _individuals.value = index.individuals.values.toList()
            _families.value = index.families.values.toList()
        } catch (e: Exception) {
            _error.value = "Failed to load local data: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    suspend fun loadRemoteData(url: String, headers: Map<String, String>? = null) {
        _isLoading.value = true
        _error.value = null

        try {
            val index = SpindlerRemoteDataSource.loadData(url, headers)
            _individuals.value = index.individuals.values.toList()
            _families.value = index.families.values.toList()
        } catch (e: RemoteDataSourceException) {
            _error.value = "Failed to load remote data: ${e.message}"
        } catch (e: Exception) {
            _error.value = "Unexpected error loading remote data: ${e.message}"
        } finally {
            _isLoading.value = false
        }
    }

    fun getIndividual(id: String?): Individual? {
        return individuals.value.find { it.id == id }
    }

    fun getFamily(id: String?): Family? {
        return families.value.find { it.id == id }
    }

    fun clearData() {
        _individuals.value = emptyList()
        _families.value = emptyList()
        _error.value = null
    }

    fun clearError() {
        _error.value = null
    }
}
