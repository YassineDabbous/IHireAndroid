package tn.core.model.net.custom

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import tn.core.presentation.base.BaseActivity
import java.io.IOException
import java.util.concurrent.TimeUnit

class NetInterceptor(private val isOnline: Boolean) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        request = if (isOnline)
            request.newBuilder().header("Cache-Control", "public, max-age=" + 60).build()
        else
            request.newBuilder().header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7).build()
        return chain.proceed(request)
    }
}
class CacheableInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val request:Request = chain.request()
        val cacheable = request.header("Cacheable")
        if (cacheable!=null){
            val maxStale = Integer.parseInt(cacheable)
            BaseActivity.log("Cacheable for: "+maxStale+" seconds")
            val response = chain.proceed(chain.request())
            val cacheControl = CacheControl.Builder()
                    .maxAge(maxStale, TimeUnit.SECONDS)
                    .build()
            return response.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .removeHeader("Pragma")
                    .build()
        }
        else return chain.proceed(chain.request());
    }
}

class OnlineCacheInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val response = chain.proceed(chain.request())
        val cacheControl = CacheControl.Builder()
                .maxAge(30, TimeUnit.SECONDS)
                .build()

        return response.newBuilder()
                .header("Cache-Control", cacheControl.toString())
                .removeHeader("Pragma")
                .build()
    }
}

class OfflineCacheInterceptor(private val isOnline: Boolean) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        var request = chain.request()
        BaseActivity.log("is it Online? "+isOnline)
        if (!isOnline) {
            BaseActivity.log("is Not Online! use cache of "+(3600*24)+" seconds ago")
            val cacheControl = CacheControl.Builder()
                    .maxStale((3600*24), TimeUnit.SECONDS)
                    .build()
            request = request.newBuilder()
                    .header("Cache-Control", cacheControl.toString())
                    .removeHeader("Pragma")
                    .build()
        }
        return chain.proceed(request)
    }
}