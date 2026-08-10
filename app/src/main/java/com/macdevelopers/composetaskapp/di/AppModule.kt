package com.macdevelopers.composetaskapp.di

import com.macdevelopers.composetaskapp.ui.screens.home.HomeViewModel
import com.macdevelopers.composetaskapp.ui.screens.login.LoginViewModel
import com.macdevelopers.composetaskapp.ui.screens.signup.SignupViewModel
import com.macdevelopers.composetaskapp.ui.screens.vendorProfile.VendorProfileViewModel
import org.koin.core.module.dsl.viewModel

import org.koin.dsl.module

val appModule = module {
    viewModel { LoginViewModel(get()) }
    viewModel { SignupViewModel(get()) }
    viewModel { HomeViewModel(get(), get(), get(), get()) }
    viewModel { VendorProfileViewModel(get(),get()) }
}
