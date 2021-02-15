package com.example.hubabubbaapp.ui

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hubabubbaapp.R
import com.example.hubabubbaapp.binding.adapter.FavoriteActivityAdapter
import com.example.hubabubbaapp.databinding.FragmentFavoritesBinding
import com.example.hubabubbaapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class FavoritesFragment : Fragment() {

    private val rAdapter: FavoriteActivityAdapter by lazy {
        FavoriteActivityAdapter(
            requireActivity(),
            mainViewModel
        )
    }
    private val mainViewModel: MainViewModel by viewModels()

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private var shareFavorite = "No Favorites"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)

        binding.lifecycleOwner = this
        binding.mainViewModel = mainViewModel
        binding.rAdapter = rAdapter


        setHasOptionsMenu(true)

        setupRecyclerView(binding.favoritesRecyclerView)


        //  INSTEAD OF SETTING THE DATA IN FRAGMENT AND OBSERVING HERE , I SET THE WITH DATA BINDING IN XML WITH BINDINGADAPTER AND OBSERVE THE DATA

//        mainViewModel.readFavoriteActivitys.observe(viewLifecycleOwner, { favoritesEntity ->
//            rAdapter.setData(favoritesEntity)
//        })
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_fragment_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.deleteAll_FavoriteActivitys) {
            mainViewModel.deleteAllFavoriteActivity()
            showSnackbar("All Favorites Removed")
        }

        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.adapter = rAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        rAdapter.clearContextualActionMode()

    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            binding.root,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Understand") {}.show()
    }

}