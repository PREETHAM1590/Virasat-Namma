package com.example.virasat

import android.app.Application
import coil.Coil
import coil.ImageLoader
import coil.disk.DiskCache
import coil.request.CachePolicy
import com.example.virasat.data.service.NetworkMonitor
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient

class VirasatApplication : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    lateinit var networkMonitor: NetworkMonitor
        private set

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        networkMonitor = NetworkMonitor(this)
        networkMonitor.register()

        val imageLoader = ImageLoader.Builder(this)
            .okHttpClient {
                OkHttpClient.Builder()
                    .followRedirects(true)
                    .followSslRedirects(true)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.05)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .build()
        Coil.setImageLoader(imageLoader)

        appScope.launch {
            com.google.firebase.analytics.FirebaseAnalytics.getInstance(this@VirasatApplication)
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        networkMonitor.unregister()
    }
}
