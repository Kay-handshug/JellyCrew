package handshug.jellycrew.home.view

import android.os.Bundle
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingFragment
import handshug.jellycrew.databinding.FragmentHomeBinding
import handshug.jellycrew.home.HomeContract.Companion.OPEN_DRAWER
import handshug.jellycrew.home.HomeContract.Companion.START_CARD_PAYMENT
import handshug.jellycrew.main.view.MainActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel
import handshug.jellycrew.home.view.dialog.HomeDialog
import handshug.jellycrew.home.viewModel.HomeViewModel
import handshug.jellycrew.utils.Log
import kr.co.bootpay.Bootpay
import kr.co.bootpay.enums.Method
import kr.co.bootpay.enums.PG
import kr.co.bootpay.enums.UX
import kr.co.bootpay.model.BootExtra
import kr.co.bootpay.model.BootUser

class HomeFragment: BindingFragment<FragmentHomeBinding>() {
    override fun getLayoutResId() = R.layout.fragment_home

    companion object {
        fun newInstance() = HomeFragment()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        val viewModel: HomeViewModel = getViewModel()
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        val dialog = HomeDialog(requireActivity(), viewModel)

        viewModel.viewEvent.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { event ->
                when (event) {
                    OPEN_DRAWER -> {
                        (activity as MainActivity).openDrawer()
                    }
                    START_CARD_PAYMENT -> {
                        goBootpayRequest()
                    }
                }
            }
        })
    }

    private fun goBootpayRequest() {
        val bootUser = BootUser().setPhone("010-1234-5678")
        val bootExtra = BootExtra().setQuotas(intArrayOf(0, 2, 3))

        val stuck = 1 //재고 있음

        Bootpay.init(activity)
            .setApplicationId("616e6b217b5ba4f7e3529b99") // 해당 프로젝트(안드로이드)의 application id 값
            .setContext(context)
            .setBootUser(bootUser)
            .setBootExtra(bootExtra)
            .setUX(UX.PG_DIALOG)
            .setPG(PG.KCP)
            .setMethod(Method.CARD)
//                .setUserPhone("010-1234-5678") // 구매자 전화번호
            .setName("맥북프로's 임다") // 결제할 상품명
            .setOrderId("1234") // 결제 고유번호expire_month
            .setPrice(10000) // 결제할 금액
            .addItem("마우's 스", 1, "ITEM_CODE_MOUSE", 100) // 주문정보에 담길 상품정보, 통계를 위해 사용
            .addItem("키보드", 1, "ITEM_CODE_KEYBOARD", 200, "패션", "여성상의", "블라우스") // 주문정보에 담길 상품정보, 통계를 위해 사용
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
            .request()
    }
}
