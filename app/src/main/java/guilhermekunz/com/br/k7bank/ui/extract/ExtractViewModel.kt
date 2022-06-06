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

    var loadingStateLiveDate = MutableLiveData<State>()

    fun getMyBalance() = viewModelScope.launch {
        loadingStateLiveDate.value = State.LOADING
        try {
            val response = repository.getMyBalance()
            _balanceResponse.postValue(response)
            loadingStateLiveDate.value = State.LOADING_FINISHED
        } catch (e: Throwable) {
            Log.e("Data", e.message.toString())
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
        }
    }

    enum class State {
        LOADING, LOADING_FINISHED
    }

}