package handshug.jellycrew.home.viewModel

import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import handshug.jellycrew.base.BaseViewModel
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_01
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_02
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_03
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_04
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_05
import handshug.jellycrew.home.model.HomeApi
import handshug.jellycrew.home.view.payType
import handshug.jellycrew.utils.Log
import kotlinx.coroutines.launch
import kr.co.bootpay.Bootpay
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.enums.UX
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser

class HomeViewModel(private val homeApi: HomeApi) : BaseViewModel(homeApi) {
    private val _selectedTabPosition = MutableLiveData<Int>()
    val selectedTabPosition: LiveData<Int>
        get() = _selectedTabPosition

    fun startPayment01() = viewEvent(START_PAYMENT_01)
    fun startPayment02() = viewEvent(START_PAYMENT_02)
    fun startPayment03() = viewEvent(START_PAYMENT_03)
    fun startPayment04() = viewEvent(START_PAYMENT_04)
    fun startPayment05() = viewEvent(START_PAYMENT_05)

    fun goBootpayRequest(fragment: FragmentManager, type: payType) {

        viewModelScope.launch(ioDispatchers) {
            val bootUser = BootUser().setPhone("010-1234-5678")
            val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3))

            val stuck = 1 //재고 있음

            val bootPay = Bootpay.init(fragment)

            try {
                bootPay
                    .setApplicationId("616e6b217b5ba4f7e3529b99") // 해당 프로젝트(안드로이드)의 application id 값
                    .setContext(context)
                    .setBootUser(bootUser)
                    .setBootExtra(bootExtra)
                    .setUX(UX.PG_DIALOG)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
                    .setName("JellyCrew basic") // 결제할 상품명
                    .setOrderId("1234") // 결제 고유번호expire_month
                    .setPrice(100) // 결제할 금액
                    .addItem("스티커", 1, "ITEM_CODE_01", 10) // 주문정보에 담길 상품정보, 통계를 위해 사용
                    .addItem("머그컵", 1, "ITEM_CODE_02", 20, "생필품", "주방", "컵") // 주문정보에 담길 상품정보, 통계를 위해 사용
                    .onConfirm { message ->
                        if (0 < stuck) Bootpay.confirm(message); // 재고가 있을 경우.
                        else Bootpay.removePaymentWindow(); // 재고가 없어 중간에 결제창을 닫고 싶을 경우
                        Log.msg("#bootpay confirm : $message")
                    }
                    .onDone { message ->
                        Log.msg("#bootpay done : $message")
                    }
                    .onReady { message ->
                        Log.msg("#bootpay ready : $message")
                    }
                    .onCancel { message ->
                        Log.msg("#bootpay cancel : $message")
                    }
                    .onError{ message ->
                        Log.msg("#bootpay error : $message")
                    }
                    .onClose { message ->
                        Log.msg("#bootpay close : $message")
                    }

                if(type != payType.TOTALPAY) {
                    bootPay
                        .setPG(PG.KCP)
                        .setMethod(
                            when (type) {
                                payType.KAKAO -> Method.KAKAO
                                else -> Method.CARD
                            }
                        )
                }

                bootPay.request()
            } catch (e: Exception) {
                Log.msg("#bootpay catch error : ${e.toString()}")
            }
        }
    }
}