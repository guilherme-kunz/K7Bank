package guilhermekunz.com.br.k7bank.ui.receipt

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import guilhermekunz.com.br.k7bank.api.response.DetailStatementResponse
import guilhermekunz.com.br.k7bank.api.response.NetworkResponse
import guilhermekunz.com.br.k7bank.repository.Repository
import kotlinx.coroutines.launch

class ReceiptViewModel(val repository: Repository) : ViewModel() {

    var loadingStateLiveDate = MutableLiveData<State>()

    private val _statementDetailResponse = MutableLiveData<DetailStatementResponse?>()
    val statementDetailResponse: LiveData<DetailStatementResponse?> = _statementDetailResponse

    private val _statementDetailError = MutableLiveData<Unit>()
    val statementDetailError = _statementDetailError as LiveData<Unit>

    fun statementDetail(id: String) = viewModelScope.launch {
        loadingStateLiveDate.value = State.LOADING
        when (val response = repository.getMyStatementDetail(id)) {
            is NetworkResponse.Failed -> {
                _statementDetailError.value = Unit
            }
            is NetworkResponse.Success -> {
                _statementDetailResponse.value = response.data
            }
        }
        loadingStateLiveDate.value = State.LOADING_FINISHED
    }


    enum class State {
        LOADING, LOADING_FINISHED
    }

}