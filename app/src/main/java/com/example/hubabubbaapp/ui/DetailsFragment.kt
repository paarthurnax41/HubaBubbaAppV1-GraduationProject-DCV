package com.example.hubabubbaapp.ui

import android.os.Bundle
import android.view.*
import android.widget.MediaController
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hubabubbaapp.R
import com.example.hubabubbaapp.data.room.FavoriteEntity
import com.example.hubabubbaapp.databinding.FragmentDetailsBinding
import com.example.hubabubbaapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {
    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<DetailsFragmentArgs>()
    private val mainViewModel: MainViewModel by viewModels()
    private var favoriteSaved = false
    private lateinit var menuItem: MenuItem
    private var savedFavoriteId = 0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)

        binding.typeTextView.text = args.result.type
        binding.acceesibilityTextView.text = args.result.accessibility.toString()
        binding.participantTextView.text = args.result.participants.toString()
        binding.priceTextView.text = args.result.price.toString()
        setHasOptionsMenu(true)

        binding.videoStartButton.setOnClickListener {
            binding.videoView.visibility = View.VISIBLE
            val mediaController = MediaController(context)
            mediaController.setAnchorView(binding.videoView)
            binding.videoView.setVideoPath("android.resource://" + context?.packageName + "/" + R.raw.rickrolled)
            binding.videoView.setMediaController(mediaController)
            binding.videoView.requestFocus()
            binding.videoView.start()
            showSnackbar("RICKROLLED")
        }
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.details_menu, menu)
        menuItem = menu.findItem(R.id.saveFavoriteMenu)
        checkIfSaved(menuItem)
        return
    }

    private fun checkIfSaved(menuItem: MenuItem) {

        mainViewModel.readFavoriteActivitys.observe(this, { favoritesEntity ->
            for (savedFavorite in favoritesEntity) {
                if (savedFavorite.result.key == args.result.key) {
                    changeMenuColor(menuItem, R.color.yellow)
                    savedFavoriteId = savedFavorite.id
                    favoriteSaved = true

                }
            }
        })
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val action = DetailsFragmentDirections.actionDetailsFragmentToHomeFragment()
            findNavController().navigate(action)
        } else if (item.itemId == R.id.saveFavoriteMenu && !favoriteSaved) {
            saveToFavorites(item)
        } else if (item.itemId == R.id.saveFavoriteMenu && favoriteSaved) {
            removeFromFavorites(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorites(item: MenuItem) {

        val favoriteEntity =
            FavoriteEntity(
                0,
                args.result
            )
        mainViewModel.insertFavoriteActivity(favoriteEntity)
        changeMenuColor(item, R.color.yellow)
        showSnackbar("Activity Saved to Favorites ! ")
        favoriteSaved = true
    }

    private fun removeFromFavorites(item: MenuItem) {
        val favoritesEntity =
            FavoriteEntity(
                savedFavoriteId,
                args.result
            )
        mainViewModel.deleteFavoriteActivity(favoritesEntity)
        changeMenuColor(item, R.color.white)
        showSnackbar("Removed From Favorites")
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT

        ).setAction("Understand") {}.show()
    }

    private fun changeMenuColor(item: MenuItem, color: Int) {

        item.icon.setTint(ContextCompat.getColor(requireContext(), color))
    }

    override fun onDestroy() {
        super.onDestroy()
        changeMenuColor(menuItem, R.color.white)
        _binding = null
    }
}