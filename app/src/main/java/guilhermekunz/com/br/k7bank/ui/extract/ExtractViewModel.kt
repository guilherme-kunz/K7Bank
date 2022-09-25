package guilhermekunz.com.br.k7bank.ui.extract

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementResponse
import guilhermekunz.com.br.k7bank.api.response.NetworkResponse
import guilhermekunz.com.br.k7bank.repository.Repository
import kotlinx.coroutines.launch

class ExtractViewModel(val repository: Repository) : ViewModel() {

    private val _balanceResponse = MutableLiveData<MyBalanceResponse?>()
    val balanceResponse: LiveData<MyBalanceResponse?> = _balanceResponse

    private val _statementResponse = MutableLiveData<MyStatementResponse?>()
    val statementResponse: LiveData<MyStatementResponse?> = _statementResponse

    private val _balanceError = MutableLiveData<Unit>()
    val balanceError = _balanceError as LiveData<Unit>

    private val _statementError = MutableLiveData<Unit>()
    val statementError = _statementError as LiveData<Unit>

    var loadingStateLiveDate = MutableLiveData<State>()

    fun getMyBalance() = viewModelScope.launch {
        loadingStateLiveDate.value = State.LOADING
        when (val response = repository.getMyBalance()) {
            is NetworkResponse.Failed -> {
                _balanceError.value = Unit
            }
            is NetworkResponse.Success -> {
                _balanceResponse.value = response.data
            }
        }
        loadingStateLiveDate.value = State.LOADING_FINISHED
    }

    fun getMyStatement(limit: String, offset: String) = viewModelScope.launch {
        loadingStateLiveDate.value = State.LOADING
        when (val response = repository.getMyStatement(limit, offset)) {
            is NetworkResponse.Failed -> {
                _statementError.value = Unit
            }
            is NetworkResponse.Success -> {
                _statementResponse.value = response.data
            }
        }
        loadingStateLiveDate.value = State.LOADING_FINISHED
    }

    enum class State {
        LOADING, LOADING_FINISHED
    }

}