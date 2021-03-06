package tiger.spike

import android.app.Application
import android.content.Context

class MapApp : Application() {

    companion object {
        lateinit var appContext: Context
    }

    override fun onCreate() {
        super.onCreate()
        appContext = this
    }
}