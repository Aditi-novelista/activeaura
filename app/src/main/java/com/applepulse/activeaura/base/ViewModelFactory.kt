package com.applepulse.activeaura.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.applepulse.activeaura.ui.auth.signInScreen.SignInViewModel
import com.applepulse.activeaura.ui.auth.signUpScreen.SecondScreen.SignUpSecondViewModel
import com.applepulse.activeaura.ui.auth.signUpScreen.firstScreen.SignUpFirstViewModel
import com.applepulse.activeaura.ui.mainFragments.appointments.MyAppointmentsViewModel
import com.applepulse.activeaura.ui.mainFragments.appointments.PatientQueueViewModel
import com.applepulse.activeaura.ui.mainFragments.home.HomeViewModel
import com.applepulse.activeaura.ui.mainFragments.home.appointment_booking.AppointmentBookingViewModel
import com.applepulse.activeaura.ui.mainFragments.home.appointment_booking.DoctorDetailsViewModel
import com.applepulse.activeaura.ui.mainFragments.settings.SettingsViewModel
import com.applepulse.activeaura.ui.mainFragments.settings.profile.ProfileViewModel

class ViewModelFactory : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T =
        with(modelClass) {
            // Get the Application object from extras
            val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
            when {
                isAssignableFrom(HomeViewModel::class.java) -> {
                    HomeViewModel(application)
                }
                isAssignableFrom(SignUpFirstViewModel::class.java) -> {
                    SignUpFirstViewModel(application)
                }
                isAssignableFrom(SignUpSecondViewModel::class.java) -> {
                    SignUpSecondViewModel(application)
                }
                isAssignableFrom(SignInViewModel::class.java) -> {
                    SignInViewModel(application)
                }
                isAssignableFrom(AppointmentBookingViewModel::class.java) -> {
                    AppointmentBookingViewModel(application)
                }
                isAssignableFrom(DoctorDetailsViewModel::class.java) -> {
                    DoctorDetailsViewModel(application)
                }
                isAssignableFrom(MyAppointmentsViewModel::class.java) -> {
                    MyAppointmentsViewModel(application)
                }
                isAssignableFrom(PatientQueueViewModel::class.java) -> {
                    PatientQueueViewModel(application)
                }
                isAssignableFrom(SettingsViewModel::class.java) -> {
                    SettingsViewModel(application)
                }
                isAssignableFrom(ProfileViewModel::class.java) -> {
                    ProfileViewModel(application)
                }

                else -> throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T
}