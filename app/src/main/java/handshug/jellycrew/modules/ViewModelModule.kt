package handshug.jellycrew.modules

import handshug.jellycrew.home.viewModel.HomeViewModel
import handshug.jellycrew.main.viewModel.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel { MainViewModel(get()) } // Main

    viewModel { HomeViewModel(get()) }
}