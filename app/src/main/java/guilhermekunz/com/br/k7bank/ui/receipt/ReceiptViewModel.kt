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

    var myStatementItem: MyStatementItem? = null

    private val _statementDetailResponse = MutableLiveData<DetailStatementResponse>()
    val statementDetailResponse = _statementDetailResponse as LiveData<DetailStatementResponse>

    fun statementDetail(id: String) = viewModelScope.launch {
        try {
            val response = repository.getMyStatementDetail(id)
            _statementDetailResponse.value = response!!
        } catch (e: Throwable) {
            Log.e("Data", e.message.toString())
        }
    }

}