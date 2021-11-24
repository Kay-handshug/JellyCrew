package handshug.jellycrew.utils

import android.content.Context
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import org.koin.core.context.GlobalContext.get
import retrofit2.HttpException
import retrofit2.Retrofit
import handshug.jellycrew.utils.ResponseCode.INTERNAL_SERVER_ERROR

object ErrorHandler {

    val context: Context = get().koin.get()

    fun errorHandle(throwable: Throwable): String {
        when (throwable) {
            is NetworkConnectionInterceptor.NoConnectivityException -> {
                toast("popupCheckNetworkStatus")
            }
            is NetworkConnectionInterceptor.OutOfMemoryException -> {
                toast("errorFailUpload")
            }
            is HttpException -> {
                val errorResponse = getErrorResponse(throwable)
                val resultCode = errorResponse?.code ?: ""
                when (resultCode) {
                    INTERNAL_SERVER_ERROR -> toast("500 Error")
                }

                return resultCode
            }
        }

        return ""
    }

    private fun getErrorResponse(throwable: Throwable): ErrorResponse? {
        val httpException = throwable as HttpException
        val errorBody = httpException.response()?.errorBody()!!
        val retrofit: Retrofit = get().koin.get()
        val converter = retrofit.responseBodyConverter<ErrorResponse>(
            ErrorResponse::class.java, ErrorResponse::class.java.annotations
        )

        return converter.convert(errorBody)
    }

    private fun toast(message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    data class ErrorResponse(
        @SerializedName("code") val code: String,
        @SerializedName("message") val message: String,
        @SerializedName("data") val data: Any?
    )
}