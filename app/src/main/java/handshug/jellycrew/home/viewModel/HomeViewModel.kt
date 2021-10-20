package handshug.jellycrew.home.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import handshug.jellycrew.base.BaseViewModel
import handshug.jellycrew.home.HomeContract.Companion.START_CARD_PAYMENT
import handshug.jellycrew.home.model.HomeApi

class HomeViewModel(private val homeApi: HomeApi) : BaseViewModel(homeApi) {
    private val _selectedTabPosition = MutableLiveData<Int>()
    val selectedTabPosition: LiveData<Int>
        get() = _selectedTabPosition

    fun startCardPayment() = viewEvent(START_CARD_PAYMENT)
}