package com.example.hubabubbaapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hubabubbaapp.R
import com.example.hubabubbaapp.databinding.FragmentHomeBinding
import com.example.hubabubbaapp.util.NetworkResult
import com.example.hubabubbaapp.viewmodels.HomeViewModel
import com.example.hubabubbaapp.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val args by navArgs<HomeFragmentArgs>()

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var mainViewModel: MainViewModel
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel = ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        binding.result = mainViewModel

        readDataBase()
        binding.generateActivityButtonHomeScreen.setOnClickListener {
            requestApiData()
        }

        binding.filterButtonHomeScreen.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_homeBottomSheetFragment)
        }

        return binding.root
    }

    private fun requestApiData() {
        Log.d("HomeFragment", "reqestApiCalled")
        mainViewModel.getActivitys(homeViewModel.applyQueries())
        mainViewModel.boredResponse.observe(viewLifecycleOwner, { response ->
            when (response) {
                is NetworkResult.Success -> {
                    response.data?.let { mainViewModel.boredResponse }
                    binding.executePendingBindings()
                }
                is NetworkResult.Error -> {
                    Log.d("ERRORREQUESTAPI", response.data.toString())
                    loadDataFromCache()
                    Toast.makeText(
                        requireContext(),
                        response.message.toString(),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        })


    }


    private fun readDataBase() {
        lifecycleScope.launch {
            Log.d("HomeFragment", "reqestDatabaseCalled")
            mainViewModel.readActivities.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty() && !args.backFromBottomSheet) {
                    binding.activityTextView.text = database[0].boredResult.activity
                }
            })
        }
    }

    private fun loadDataFromCache() {
        lifecycleScope.launch {
            mainViewModel.readActivities.observe(viewLifecycleOwner, { database ->
                if (database.isNotEmpty()) {
                    binding.activityTextView.text = database[0].boredResult.activity
                }
            })
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}