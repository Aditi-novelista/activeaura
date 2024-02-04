package com.applepulse.activeaura.ui.mainFragments.appointments

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applepulse.activeaura.base.ViewModelFactory
import com.applepulse.activeaura.databinding.FragmentPatientQueueBinding
import com.applepulse.activeaura.model.DoctorAppointment
import com.applepulse.activeaura.ui.adapter.PatientQueueAdapter
import java.text.SimpleDateFormat


class PatientQueueFragment : Fragment() {

    private var _binding: FragmentPatientQueueBinding? = null
    private val binding get() = _binding!!
    private val patientQueueViewModel by viewModels<PatientQueueViewModel> { ViewModelFactory() }
    private val args: PatientQueueFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var patientQueueAdapter: PatientQueueAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPatientQueueBinding.inflate(inflater, container, false)

        initObservers()
        initViews()

        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initViews() {
        binding.run {
            recyclerView = binding.appointmentRecyclerview
            recyclerView.layoutManager = LinearLayoutManager(requireContext())
            recyclerView.setHasFixedSize(true)
            patientQueueAdapter = PatientQueueAdapter(args.doctorUserID) {

            }
            recyclerView.adapter = patientQueueAdapter
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun getAppointmentDateTime(date: String?, time: String): String {
        val format = SimpleDateFormat("dd-MM-yyyy HH:mm")
        val appointmentDateTime = date + " " + time.substring(0, 5)
        return format.parse(appointmentDateTime)?.toString() ?: ""
    }


    private fun initObservers() {
        patientQueueViewModel.run {
            setPassedData(
                args.doctorUserID,
                args.selectedDate,
                args.hideSelectedDate
            )
            doctorUserID.observe(viewLifecycleOwner) {
                getPatientsListFromFirebase()
                binding.selectedDateText.text = "Selected Date: ${selectedDate.value}"
            }
            patientsListLiveData.observe(viewLifecycleOwner) {
                patientQueueAdapter.setData(it as ArrayList<DoctorAppointment>)
            }
        }
    }

}