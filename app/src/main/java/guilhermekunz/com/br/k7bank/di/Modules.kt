package guilhermekunz.com.br.k7bank.di

import guilhermekunz.com.br.k7bank.api.ClientRetrofit
import guilhermekunz.com.br.k7bank.repository.RepositoryImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val repositoryModule = module(override = true){
    single<RepositoryImpl> {
        RepositoryImpl(api = get())
    }

val dataModule = module{
    single {
        ClientRetrofit.create(androidContext())
    }
}