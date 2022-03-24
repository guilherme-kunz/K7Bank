package guilhermekunz.com.br.k7bank.di

import guilhermekunz.com.br.k7bank.api.ClientRetrofit
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val dataModule = module{
    single {
        ClientRetrofit.create(androidContext())
    }
}