package handshug.jellycrew.main.viewModel

import handshug.jellycrew.base.BaseViewModel
import handshug.jellycrew.main.MainContract
import handshug.jellycrew.main.MainContract.Companion.ACTIVITY_MAIN
import handshug.jellycrew.main.MainContract.Companion.DRAWER_MENU_OPEN
import handshug.jellycrew.main.model.MainApi

class MainViewModel(private val mainApi: MainApi) : BaseViewModel(mainApi), MainContract {
//    private val _versionData: MutableLiveData<VersionData> = MutableLiveData()
//    val versionData: LiveData<VersionData>
//        get() = _versionData

    fun drawerMenuOpen() = viewEvent(DRAWER_MENU_OPEN)
    fun navigateToMain() = viewEvent(ACTIVITY_MAIN)

//    fun getVersion() {
//        viewModelScope.launch(exceptionHandler) {
//            mainApi.getVersion().apply {
//                if (this.resultCode == SUCCESS) {
//                    _versionData.value = this.data
//                }
//                else {
////                    viewEvent(FAIL_LOAD_VERSION)
//                }
//            }
//        }
//    }

//    private fun getFcmTokenParams(token: String): MutableMap<String, Any> {
//        return mutableMapOf<String, Any>().apply {
//            this["name"] = Build.MODEL
//            this["type"] = "AOS"
//            this["token"] = token
//        }
//    }
}