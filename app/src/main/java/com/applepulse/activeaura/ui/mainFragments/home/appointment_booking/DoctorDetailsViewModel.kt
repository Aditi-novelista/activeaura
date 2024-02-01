package com.applepulse.activeaura.ui.mainFragments.home.appointment_booking

import android.app.Application
import com.applepulse.activeaura.base.BaseViewModel
import com.applepulse.activeaura.model.User

class DoctorDetailsViewModel(application: Application) : BaseViewModel(application) {

    private var doctor: User = User()

    fun initDoctor(doctorDetails: User) {
        doctor = doctorDetails
    }

    fun getDoctor(): User {
        return doctor
    }

}
