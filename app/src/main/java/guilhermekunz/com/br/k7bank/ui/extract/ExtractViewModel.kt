package guilhermekunz.com.br.k7bank.ui.extract

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementItem
import guilhermekunz.com.br.k7bank.api.response.MyStatementResponse
import guilhermekunz.com.br.k7bank.repository.Repository
import kotlinx.coroutines.launch

class ExtractViewModel(val repository: Repository)  : ViewModel() {

    private val _balanceResponse = MutableLiveData<MyBalanceResponse?>()
    val balanceResponse = _balanceResponse as LiveData<MyBalanceResponse?>

    private val _statementResponse = MutableLiveData<MyStatementResponse?>()
    val statementResponse = _statementResponse as LiveData<MyStatementResponse?>

    private val _balanceError = MutableLiveData<Unit>()
    val balanceError = _balanceError as LiveData<Unit>

    private val _statementError = MutableLiveData<Unit>()
    val statementError = _statementError as LiveData<Unit>

    var loadingStateLiveDate = MutableLiveData<State>()

    fun getMyBalance() = viewModelScope.launch {
        loadingStateLiveDate.value = State.LOADING
        try {
            val response = repository.getMyBalance()
            _balanceResponse.postValue(response)
            loadingStateLiveDate.value = State.LOADING_FINISHED
        } catch (e: Throwable) {
            Log.e("Data", e.message.toString())
            _balanceError.value = Unit
        }
    }

    fun getMyStatement(limit: String, offset: String) = viewModelScope.launch {
        loadingStateLiveDate.value = State.LOADING
        try {
            val response = repository.getMyStatement(limit, offset)
            if (response != null){
                _statementResponse.value = response
            }
            loadingStateLiveDate.value = State.LOADING_FINISHED
        } catch (e: Throwable) {
            Log.e("Data", e.message.toString())
            _statementError.value = Unit
        }
    }

    enum class State {
        LOADING, LOADING_FINISHED
    }

}