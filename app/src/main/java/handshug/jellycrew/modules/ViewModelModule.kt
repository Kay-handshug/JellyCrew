package handshug.jellycrew.modules

import handshug.jellycrew.home.viewModel.HomeViewModel
import handshug.jellycrew.main.viewModel.MainViewModel
import handshug.jellycrew.member.viewModel.MemberViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MainViewModel(get()) }

    viewModel { MemberViewModel(get()) }

    viewModel { HomeViewModel(get()) }
}