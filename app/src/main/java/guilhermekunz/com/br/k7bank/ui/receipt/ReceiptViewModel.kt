package guilhermekunz.com.br.k7bank.ui.receipt

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import guilhermekunz.com.br.k7bank.api.response.DetailStatementResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementItem
import guilhermekunz.com.br.k7bank.repository.Repository
import kotlinx.coroutines.launch

class ReceiptViewModel(val repository: Repository) : ViewModel() {

    var loadingStateLiveDate = MutableLiveData<State>()

    private val _statementDetailResponse = MutableLiveData<DetailStatementResponse?>()
    val statementDetailResponse = _statementDetailResponse as LiveData<DetailStatementResponse>

    private val _statementDetailError = MutableLiveData<Unit>()
    val statementDetailError = _statementDetailError as LiveData<Unit>

    fun statementDetail(id: String) = viewModelScope.launch {
        loadingStateLiveDate.value = State.LOADING
        try {
            val response = repository.getMyStatementDetail(id)
            _statementDetailResponse.value = response
            loadingStateLiveDate.value = State.LOADING_FINISHED
        } catch (e: Throwable) {
            Log.e("Data", e.message.toString())
            _statementDetailError.value = Unit
        }
    }

    enum class State {
        LOADING, LOADING_FINISHED
    }

}