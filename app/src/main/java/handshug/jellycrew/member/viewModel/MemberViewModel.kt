package handshug.jellycrew.member.viewModel

import android.annotation.SuppressLint
import android.os.CountDownTimer
import androidx.appcompat.widget.AppCompatTextView
import handshug.jellycrew.base.BaseViewModel
import handshug.jellycrew.main.MainContract
import handshug.jellycrew.main.MainContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.main.model.MainApi
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_CLOSE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_TERMS
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_JOIN_PHONE
import handshug.jellycrew.member.MemberContract.Companion.ACTIVITY_PAST_ORDERS
import handshug.jellycrew.member.MemberContract.Companion.GET_COUNT_DOWN_TIMER
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_TOAST
import handshug.jellycrew.member.MemberContract.Companion.SHOW_DIALOG_USER_INFO_NOTI
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_FACEBOOK
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_KAKAO
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_NAVER

class MemberViewModel(private val mainApi: MainApi) : BaseViewModel(mainApi), MainContract {

//    private val _versionData: MutableLiveData<VersionData> = MutableLiveData()
//    val versionData: LiveData<VersionData>
//        get() = _versionData

    fun activityClose() = viewEvent(ACTIVITY_CLOSE)
    fun navigateToMain() = viewEvent(ACTIVITY_MAIN)
    fun navigateToJoinTerms() = viewEvent(ACTIVITY_JOIN_TERMS)
    fun navigateToJoinPhone() = viewEvent(ACTIVITY_JOIN_PHONE)
    fun navigateToPastOrders() = viewEvent(ACTIVITY_PAST_ORDERS)

    fun startLoginKakao() = viewEvent(START_LOGIN_KAKAO)
    fun startLoginNaver() = viewEvent(START_LOGIN_NAVER)
    fun startLoginFacebook() = viewEvent(START_LOGIN_FACEBOOK)

    fun showDialogUserInfoNoti() = viewEvent(SHOW_DIALOG_USER_INFO_NOTI)
    fun showDialogToast() = viewEvent(SHOW_DIALOG_TOAST)

    fun getCountDownTimer() = viewEvent(GET_COUNT_DOWN_TIMER)



    fun countDownTimer(textView: AppCompatTextView): CountDownTimer {
        return object: CountDownTimer(3 * 60 * 1000L, 1000L) {
            @SuppressLint("SetTextI18n")
            override fun onTick(millis: Long) {
                val min = convertTimeFormat(getMinutesFromMillis(millis))
                val sec = convertTimeFormat(getSecondsFromMillis(millis))

                textView.text = "$min:$sec"
            }

            override fun onFinish() {
                textView.text = "00:00"
            }
        }
    }

    private fun convertTimeFormat(time: Long) = if (time < 10) "0$time" else "$time"

    private fun getMinutesFromMillis(millis: Long) = millis / 1000 / 60

    private fun getSecondsFromMillis(millis: Long) = millis / 1000 % 60
}