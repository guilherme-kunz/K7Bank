package guilhermekunz.com.br.k7bank.api.model

import java.lang.Exception

object ApiError {
    data class GenericException(override val message: String? = null) : Exception()
}