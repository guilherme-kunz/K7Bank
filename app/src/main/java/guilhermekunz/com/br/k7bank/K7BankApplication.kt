package guilhermekunz.com.br.k7bank

import android.app.Application


class K7BankApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin
    }


}