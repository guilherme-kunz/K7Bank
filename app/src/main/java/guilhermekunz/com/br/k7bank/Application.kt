package guilhermekunz.com.br.k7bank

import android.app.Application
import guilhermekunz.com.br.k7bank.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin


class Application: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@Application)
            koin.loadModules(listOf(
                dataModule
            ))
        }
    }


}