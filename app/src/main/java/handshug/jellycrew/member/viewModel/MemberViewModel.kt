package handshug.jellycrew.member.viewModel

import handshug.jellycrew.base.BaseViewModel
import handshug.jellycrew.main.MainContract
import handshug.jellycrew.main.MainContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.main.MainContract.Companion.DRAWER_MENU_OPEN
import handshug.jellycrew.main.model.MainApi
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_KAKAO
import handshug.jellycrew.member.MemberContract.Companion.START_LOGIN_NAVER

class MemberViewModel(private val mainApi: MainApi) : BaseViewModel(mainApi), MainContract {

//    private val _versionData: MutableLiveData<VersionData> = MutableLiveData()
//    val versionData: LiveData<VersionData>
//        get() = _versionData

    fun navigateToMain() = viewEvent(ACTIVITY_MAIN)
    fun startLoginKakao() = viewEvent(START_LOGIN_KAKAO)
    fun startLoginNaver() = viewEvent(START_LOGIN_NAVER)

}