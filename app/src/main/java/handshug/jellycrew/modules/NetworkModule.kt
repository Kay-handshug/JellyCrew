package handshug.jellycrew.modules

import handshug.jellycrew.utils.NetworkConnectionInterceptor
import com.google.gson.GsonBuilder
import handshug.jellycrew.BuildConfig
import handshug.jellycrew.Preference
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

val networkModule = module {

    single { Cache(androidApplication().cacheDir, 10L * 1024 * 1024) }

    single { GsonBuilder().create() }

    single {
        OkHttpClient.Builder().apply {
            cache(get())
            connectTimeout(timeout = 60L, unit = TimeUnit.SECONDS)
            writeTimeout(timeout = 60L, unit = TimeUnit.SECONDS)
            readTimeout(timeout = 60L, unit = TimeUnit.SECONDS)
            retryOnConnectionFailure(true)
            addInterceptor(NetworkConnectionInterceptor(get()))
            addInterceptor(HttpLoggingInterceptor().apply {
                if (BuildConfig.DEBUG) level = HttpLoggingInterceptor.Level.BODY
            })
            addInterceptor{ chain ->
                val builder = chain.request().newBuilder()
                val loginKey = Preference.loginKey
                if (loginKey.isNotEmpty()) {
                    builder.addHeader("loginKey", loginKey)
                }

                chain.proceed(builder.build())
            }
        }.build()
    }

    single {
        val baseUrl: String = if(BuildConfig.DEBUG) {
            // 추후 QA서버로 변경
            "http://www.naver.com"
        } else {
            "http://www.naver.com"
        }

        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(get()))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .client(get())
            .build()
    }
}