package com.fduhole.danxinative.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import com.fduhole.danxinative.base.feature.FudanDailyFeature

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private suspend fun buildFeatures() {
        val fudanDailyFeature = FudanDailyFeature()
        _uiState.emit(HomeUiState(listOf(fudanDailyFeature)))
    }

    suspend fun ensureFeatureBuilt() {
        if (_uiState.value.features.isEmpty()) {
            buildFeatures()
        }
    }

    fun initModel(featureCallback: () -> Unit) {
        viewModelScope.launch {
            ensureFeatureBuilt()
            for (feature in _uiState.value.features) {
                feature.initFeature(featureCallback, viewModelScope)
            }
        }
    }
}