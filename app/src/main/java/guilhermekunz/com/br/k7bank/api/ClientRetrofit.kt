package guilhermekunz.com.br.k7bank.api

import android.content.Context
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import guilhermekunz.com.br.k7bank.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ClientRetrofit {
    companion object {
        fun create(context: Context?): BankApi {
            val logger = HttpLoggingInterceptor()
            logger.level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
            val client = OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(logger)
                .build()

            val K7_BANK_URL = "https://bank-statement-bff.herokuapp.com/"

            return Retrofit.Builder()
                .baseUrl(K7_BANK_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .build()
                .create(BankApi::class.java)
        }
    }
}