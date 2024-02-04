package com.applepulse.activeaura.ui.mainFragments.home

import android.app.AlertDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.applepulse.activeaura.R
import com.applepulse.activeaura.adapter.DoctorListAdapter
import com.applepulse.activeaura.base.ViewModelFactory
import com.applepulse.activeaura.databinding.FragmentHomeBinding
import com.applepulse.activeaura.model.Doctor
import com.applepulse.activeaura.model.User
import com.applepulse.activeaura.utils.Logger
import com.applepulse.activeaura.utils.Utils.containsLetters
import com.applepulse.activeaura.utils.Utils.toStringWithoutSpaces
import com.google.android.material.chip.Chip
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel> { ViewModelFactory() }
    private val binding get() = _binding!!
    private lateinit var db: DatabaseReference
    private lateinit var recyclerView: RecyclerView
    private lateinit var doctorListAdapter: DoctorListAdapter
    private lateinit var sharedPreference: SharedPreferences


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initViews()
        initObservers()

        return binding.root
    }

    private fun initViews() {
        db = FirebaseDatabase.getInstance().reference
        sharedPreference = requireActivity().getSharedPreferences("UserData", Context.MODE_PRIVATE)

        binding.doctorData.addTextChangedListener {
            homeViewModel.searchDoctor(it.toString())
        }

        handleChipFilter()
        homeViewModel.getDataFromSharedPreference(sharedPreference)

        doctorListAdapter = DoctorListAdapter {
            val rating = homeViewModel.totalRating.value!!
            binding.doctorData.setText("") //search bar
            onDoctorCardClick(it, rating)
        }

        recyclerView = binding.doctorList
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = doctorListAdapter
    }

    private fun onDoctorCardClick(doctor: User, rating: Float) {
        val action = HomeFragmentDirections.actionHomeToDoctorDetailsFragment(doctor)
        findNavController().navigate(action)
    }

    private fun initObservers() {
        homeViewModel.run {
            getDoctorList()
            searchedDoctor.observe(viewLifecycleOwner) {
                handleDoctorSearch(it)
            }
            user.observe(viewLifecycleOwner) {
                Logger.debugLog("User Data: $it")
                binding.nameDisplay.text =
                    if (it.isDoctor == Doctor.IS_DOCTOR.toItemString()) "Dr. ${it.Name}" else it.Name
                getTotalRating()
            }
            doctorList.observe(viewLifecycleOwner) {
                if (it.isNotEmpty()) {
                    Logger.debugLog("Doctor List: $it")
                    doctorListAdapter.addItems(it)
                }
            }
        }
    }

    private fun handleChipFilter() {

        val listOfDoctorSpecialists = mutableListOf<String>()
        resources.getStringArray(R.array.all_specialists).forEach {
            listOfDoctorSpecialists.add(it)
        }

        //Create the chips dynamically from the filter response
        for ((count, filter) in listOfDoctorSpecialists.withIndex()) {
            val newChip = Chip(requireContext(), null, R.attr.FilterChips)
            newChip.text = filter
            newChip.id = count
            binding.chipGroup.addView(newChip)
        }

        binding.chipGroup.check(0)

        binding.chipGroup.setOnCheckedStateChangeListener { group, checkedId ->

            if (checkedId.isNotEmpty()) {
                val chip: Chip? = group.findViewById(checkedId[0])
                val selectedSpecialist = chip?.text.toString()

                if (selectedSpecialist.isNotEmpty()) {
                    if (selectedSpecialist == "All") {
                        doctorListAdapter.addItems(homeViewModel.doctorList.value!!)
                    } else {
                        val searchedList = homeViewModel.doctorList.value!!.filter {
                            it.Specialist!!.trim().lowercase().toStringWithoutSpaces()
                                .contains(
                                    selectedSpecialist.lowercase().trim().toStringWithoutSpaces(),
                                    true
                                )
                        }
                        doctorListAdapter.addItems(searchedList)
                    }
                } else {
                    doctorListAdapter.addItems(homeViewModel.doctorList.value!!)
                }
            }
        }
    }

    private fun handleDoctorSearch(searchedData: String) {
        if (searchedData.isNotEmpty()) {
            val searchedList = homeViewModel.doctorList.value!!.filter {
                containsLetters(it.Name!!, searchedData)
                        || containsLetters(it.Email!!, searchedData)
                        || containsLetters(it.Phone!!, searchedData)
            }
            doctorListAdapter.addItems(searchedList)
        } else {
            doctorListAdapter.addItems(homeViewModel.doctorList.value!!)
        }
    }
}
