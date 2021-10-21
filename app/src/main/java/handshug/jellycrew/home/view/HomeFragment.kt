package handshug.jellycrew.home.view

import android.os.Bundle
import handshug.jellycrew.R
import handshug.jellycrew.base.BindingFragment
import handshug.jellycrew.databinding.FragmentHomeBinding
import handshug.jellycrew.home.HomeContract.Companion.OPEN_DRAWER
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_01
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_02
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_03
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_04
import handshug.jellycrew.home.HomeContract.Companion.START_PAYMENT_05
import handshug.jellycrew.home.view.dialog.HomeDialog
import handshug.jellycrew.home.viewModel.HomeViewModel
import handshug.jellycrew.main.view.MainActivity
import org.koin.androidx.viewmodel.ext.android.getViewModel

enum class payType {
     CARD, KAKAO, TOTALPAY
 }

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
                    START_PAYMENT_01 -> viewModel.goBootpayRequest(childFragmentManager, payType.CARD)
                    START_PAYMENT_02 -> viewModel.goBootpayRequest(childFragmentManager, payType.KAKAO)
                    START_PAYMENT_03 -> viewModel.goBootpayRequest(childFragmentManager, payType.TOTALPAY)
                    START_PAYMENT_04 -> toast(getString(R.string.toast_payment_request_token))
                    START_PAYMENT_05 -> toast(getString(R.string.toast_payment_request_token))
                }
            }
        })
    }
}
