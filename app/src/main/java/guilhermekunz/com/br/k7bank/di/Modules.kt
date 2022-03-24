package guilhermekunz.com.br.k7bank.di

import guilhermekunz.com.br.k7bank.api.ClientRetrofit
import guilhermekunz.com.br.k7bank.repository.RepositoryImpl
import guilhermekunz.com.br.k7bank.ui.extract.ExtractViewModel
import guilhermekunz.com.br.k7bank.ui.receipt.ReceiptViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module(override = true){
    viewModel { ExtractViewModel(repository = get()) }
    viewModel { ReceiptViewModel(repository = get()) }
}


val repositoryModule = module(override = true) {
    single<RepositoryImpl> {
        RepositoryImpl(api = get())
    }
}

val dataModule = module{
    single {
        ClientRetrofit.create(androidContext())
    }
}