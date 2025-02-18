package com.ag.projects.sebha.presentation.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ag.projects.sebha.data.local.AzkarEntity
import com.ag.projects.sebha.domain.usecase.delete.DeleteAzkarUseCase
import com.ag.projects.sebha.domain.usecase.get.GetAzkarUseCase
import com.ag.projects.sebha.domain.usecase.increment.IncrementAzkarCountUseCase
import com.ag.projects.sebha.domain.usecase.insert.InsertAzkarUseCase
import com.ag.projects.sebha.domain.usecase.reset.ResetAzkarToZeroUseCase
import com.ag.projects.sebha.util.Result
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeScreenViewModel(
    private val insertAzkarUseCase: InsertAzkarUseCase,
    private val getAzkarUseCase: GetAzkarUseCase,
    private val incrementAzkarCountUseCase: IncrementAzkarCountUseCase,
    private val deleteAzkarUseCase: DeleteAzkarUseCase,
    private val resetAzkarToZeroUseCase: ResetAzkarToZeroUseCase
) : ViewModel() {

    private val _azkar = MutableStateFlow<Result<List<AzkarEntity>>>(Result.Loading)
    val azkar = _azkar
        .onStart {
            getAzkar()
        }.stateIn(
            viewModelScope,
            SharingStarted.Lazily,
            Result.Loading
        )

    fun insertAzkar(azkar: String, count: Int) {
        viewModelScope.launch {
            try {
                insertAzkarUseCase.insertAzkar(
                    AzkarEntity(
                        count = count,
                        azkar = azkar
                    )
                )
            } catch (e: Exception) {
                Result.Error(
                    e.message.toString(),
                    e
                )
            }
        }
    }

    fun incrementAzkarCount(id: Int) {
        viewModelScope.launch {
            incrementAzkarCountUseCase.incrementAzkarCount(id)
//            getAzkar()
        }
    }

    fun resetAzkarToZero(id: Int) {
        viewModelScope.launch {
            try {
                resetAzkarToZeroUseCase.resetAzkarToZero(id)
            } catch (_: Exception) {

            }
        }
    }

    fun getAzkar() {
        viewModelScope.launch {
            try {
                val azkar = getAzkarUseCase.getAzkar()
                _azkar.emit(
                    Result.Success(azkar)
                )
            } catch (e: Exception) {
                _azkar.emit(
                    Result.Error(
                        e.message.toString(),
                        e
                    )
                )
            }
        }
    }

    fun deleteAzkar(id: Int) {
        viewModelScope.launch {
            deleteAzkarUseCase.deleteAzkar(id)
        }
    }
}