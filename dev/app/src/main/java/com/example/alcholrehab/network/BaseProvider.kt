package com.example.alcholrehab.network

import android.os.Build
import androidx.annotation.Keep
import okhttp3.CipherSuite
import okhttp3.ConnectionSpec
import okhttp3.OkHttpClient
import okhttp3.TlsVersion
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*

@Keep
class BaseProvider {
    companion object {

        private const val baseUrl = "https://e974-2600-8807-c0c0-d400-b8ed-fb0d-e130-d687.ngrok.io";

        private val retrofit by lazy {
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)

            if(Build.VERSION.SDK_INT == Build.VERSION_CODES.N){
                val spec = ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .tlsVersions(TlsVersion.TLS_1_2)
                    .cipherSuites(CipherSuite.TLS_DHE_RSA_WITH_AES_256_CBC_SHA)
                    .build()

                val client = OkHttpClient.Builder()
                    .connectionSpecs(Collections.singletonList(spec))
                    .addInterceptor(logging).build()

                Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }else{
                val client = OkHttpClient.Builder()
                    .addInterceptor(logging).build()

                Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            }
        }

        val api: ApiClient by lazy {
            retrofit.create(ApiClient::class.java)
        }

    }
}