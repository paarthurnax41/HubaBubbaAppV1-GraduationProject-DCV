package com.example.hubabubbaapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.example.hubabubbaapp.databinding.HomeBottomSheetBinding
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_MAXPRICE
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_MINPRICE
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_PARTICIPANT
import com.example.hubabubbaapp.util.Constants.Companion.DEFAULT_TYPE
import com.example.hubabubbaapp.viewmodels.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import es.dmoral.toasty.Toasty
import java.util.*


class HomeBottomSheet : BottomSheetDialogFragment() {
    private var _binding: HomeBottomSheetBinding? = null
    private val binding get() = _binding!!
    private lateinit var homeViewModel: HomeViewModel
    private var typeChip = DEFAULT_TYPE
    private var typeChipId = 0
    private var minPriceSeekbarValue = DEFAULT_MINPRICE
    private var minPriceSeekbarId = 0
    private var participantCount = DEFAULT_PARTICIPANT
    private var participantCountId = 0
    private var maxPriceSeekbarValue = DEFAULT_MAXPRICE
    private var maxPriceSeekbarId = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeViewModel = ViewModelProvider(requireActivity()).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = HomeBottomSheetBinding.inflate(inflater, container, false)

        homeViewModel.readTypePriceAndParticipant.asLiveData()
            .observe(viewLifecycleOwner, { value ->
                typeChip = value.selectedType
                participantCount = value.selectedParticipant
                minPriceSeekbarValue = value.selectedMinPrice
                maxPriceSeekbarValue = value.selectedMaxPrice
                updateChip(value.selectedTypeId, binding.activityTypeChipGroup)
                updateSeekBarParticipant(value.selectedParticipantId, binding.seekBarParticipant)
                updateSeekBarPrice(value.selectedMinPriceId, binding.seekBarMinPrice)
                updateSeekBarPrice(value.selectedMaxPriceId, binding.seekBarMaxPrice)
            })

        binding.activityTypeChipGroup.setOnCheckedChangeListener { group, checkedChipId ->
            val chip = group.findViewById<Chip>(checkedChipId)
            val selectedType = chip.text.toString().toLowerCase(Locale.ROOT)
            typeChip = selectedType
            typeChipId = checkedChipId
        }

        binding.seekBarParticipant.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                binding.participantCountTextView.text = progress.toString()
                participantCountId = progress
                participantCount = progress
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {


            }
        })

        //                                      SEEKBARS

        binding.seekBarMinPrice.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                minPriceSeekbarValue = progress / 100.0
                binding.minPriceTextView.text = progress.toString()
                minPriceSeekbarId = progress

            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {


            }
        })
        binding.seekBarMaxPrice.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {

                maxPriceSeekbarValue = progress / 100.0
                binding.maxPriceTextView.text = progress.toString()
                maxPriceSeekbarId = progress

            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {


            }
        })




        binding.applyButton.setOnClickListener {
            if (maxPriceSeekbarValue > minPriceSeekbarValue) {
                homeViewModel.saveTypePriceAndParticipant(
                    typeChip,
                    typeChipId,
                    minPriceSeekbarValue,
                    minPriceSeekbarId,
                    participantCount,
                    participantCountId,
                    maxPriceSeekbarValue,
                    maxPriceSeekbarId
                )

                val action =
                    HomeBottomSheetDirections.actionHomeBottomSheetFragmentToHomeFragment(true)
                findNavController().navigate(action)

            } else {
                Toasty.error(
                    requireContext(),
                    "Min Price cant be higher then Max",
                    Toast.LENGTH_LONG
                ).show()


            }
        }

        return binding.root
    }

    private fun updateSeekBarPrice(progressPrice: Int, seekBar: SeekBar) {
        seekBar.progress = progressPrice
    }

    private fun updateSeekBarParticipant(progressParticipant: Int, seekBar: SeekBar) {

        seekBar.progress = progressParticipant


    }

    private fun updateChip(chipId: Int, chipGroup: ChipGroup) {

        if (chipId != 0) {
            try {
                chipGroup.findViewById<Chip>(chipId).isChecked = true
            } catch (e: Exception) {
                Log.d("HomeBottomSheet", e.message.toString())
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}

