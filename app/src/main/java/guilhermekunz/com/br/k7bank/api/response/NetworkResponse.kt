package guilhermekunz.com.br.k7bank.api.response

sealed class NetworkResponse<out T> {

    data class Success<T>(val data: T) : NetworkResponse<T>()
    data class Failed(val error: Exception) : NetworkResponse<Nothing>()

}