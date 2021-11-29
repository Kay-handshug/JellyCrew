package handshug.jellycrew.error

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.google.gson.annotations.SerializedName
import handshug.jellycrew.Preference
import handshug.jellycrew.R
import handshug.jellycrew.member.view.JoinActivity
import handshug.jellycrew.utils.ActivityUtil
import handshug.jellycrew.utils.NetworkConnectionInterceptor
import handshug.jellycrew.utils.ResponseCode.ERROR_CODE_2001
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
                val resultCode = errorResponse?.code?: ""
                val resultMsg = errorResponse?.message?: ""
                when (resultCode) {
                    INTERNAL_SERVER_ERROR -> toast("500 Error")
                    ERROR_CODE_2001 -> {
                        when (Preference.loginType) {
                            0 -> {
                                Intent(context, Error2001Activity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    context.startActivity(this)
                                }
                            }
                            1, 2, 3 -> {
                                Intent(context, JoinActivity::class.java).apply {
                                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    this.putExtra("isSocialJoin", true)
                                    context.startActivity(this)
                                }
                            }
                        }
                    }
                    else -> toast("Error : $resultCode :: $resultMsg")
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