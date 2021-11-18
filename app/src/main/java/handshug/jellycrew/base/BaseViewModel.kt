package handshug.jellycrew.base

import android.content.Context
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import handshug.jellycrew.utils.ResponseCode.SUCCESS
import kotlinx.coroutines.*
import org.koin.core.context.GlobalContext
import java.util.regex.Pattern

open class BaseViewModel(private val baseApi: BaseApi?) : ViewModel() {
    companion object {
        // 최소 8 자, 최소 하나의 문자, 하나의 숫자 및 하나의 특수 문자
        const val REGEX_PATTERN_PASSWORD = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[\$@\$!%*#?&])[A-Za-z\\d\$@\$!%*#?&]{8,}\$"
        const val REGEX_PATTERN_PHONE_NUMBER = "^01(?:0|1|[6-9])[0-9]{10,12}\$"
        const val REGEX_PATTERN_EMAIL = "^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}\$"
        const val REGEX_PATTERN_NICK_NAME =  "(^[a-zA-Z0-9가-힣]{2,10}$)"
        const val REGEX_PATTERN_AUTH_CODE = "\\d{6}"
        const val REGEX_PATTERN_ZIP_CODE = "\\d{5}"

        // 한글 또는 영문
        const val REGEX_PATTERN_TEXT = ".*[a-zA-Z가-힣].*"
        // 영문자
        const val REGEX_PATTERN_ALPHABET = ".*[a-zA-Z].*"
        // 숫자 또는 특수문자
        const val REGEX_PATTERN_NUMBER = ".*[0-9].*"
        // 특수문자
        const val REGEX_PATTERN_SPECIAL_CHARACTERS = ".*[^\\w\\s].*"

        // 영어, 숫자, 특수문자 모두 포함 = "(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[^\w\s]).*"

        const val PROGRESS_SHOW = 777
        const val PROGRESS_HIDE = 778
    }

    val scope: Job
        get() {
            return viewModelScope.launch(BaseCoroutine.exceptionHandler) { }
        }

    // 액티비티 호출을 위한 Observable
    private val _viewEvent = MutableLiveData<Event<Any>>()
    val viewEvent: LiveData<Event<Any>>
        get() = _viewEvent

    fun viewEvent(content: Any) {
        _viewEvent.value = Event(content)
    }

    private val _toastMessage: MutableLiveData<String> = MutableLiveData()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun toast(msg: String) {
        _toastMessage.value = msg
    }

    val context: Context = GlobalContext.get().koin.get()

    private val _hideKeyboard: MutableLiveData<View> = MutableLiveData()
    val hideKeyboard: LiveData<View>
        get() = _hideKeyboard

    fun hideKeyboard(view: View) {
        viewModelScope
        _hideKeyboard.value = view
    }

    // Dispatchers 선언
    protected val ioDispatchers = BaseCoroutine.ioDispatchers
    protected val uiDispatchers = BaseCoroutine.uiDispatchers
    protected val exceptionHandler = BaseCoroutine.exceptionHandler

    // ViewModel 에서 자주 사용하는 함수들
    fun checkSuccess(resultCode: Int): Boolean {
        return resultCode == SUCCESS
    }

    fun regexPattern(pattern: String, input: String): Boolean {
        return Pattern.compile(pattern).matcher(input).matches()
    }

    fun regexPatternSearch(pattern: String, input: String): String? {
        val matcher = Pattern.compile(pattern).matcher(input)

        return if (matcher.find()) {
            matcher.group(0)
        } else {
            null
        }
    }
}