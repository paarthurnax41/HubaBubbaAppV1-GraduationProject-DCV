package com.example.hubabubbaapp.binding.adapter

import android.content.Intent
import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.hubabubbaapp.BoredDiffUtil
import com.example.hubabubbaapp.R
import com.example.hubabubbaapp.data.room.FavoriteEntity
import com.example.hubabubbaapp.databinding.FavoritesRowLayoutBinding
import com.example.hubabubbaapp.ui.FavoritesFragmentDirections
import com.example.hubabubbaapp.viewmodels.MainViewModel
import com.google.android.material.snackbar.Snackbar

class FavoriteActivityAdapter(
    private val requireActivity: FragmentActivity,
    private val mainViewModel: MainViewModel
) : RecyclerView.Adapter<FavoriteActivityAdapter.MyViewHolder>(), ActionMode.Callback {

    private var favoriteActivitys = emptyList<FavoriteEntity>()
    private var multiSelection = false

    private lateinit var mActionmode: ActionMode
    private lateinit var rootView: View
    private var selectedFavorites = arrayListOf<FavoriteEntity>()
    private var myViewHolders = arrayListOf<MyViewHolder>()

    class MyViewHolder(private val binding: FavoritesRowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(favoritesEntity: FavoriteEntity) {
            binding.favoritesEntity = favoritesEntity
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = FavoritesRowLayoutBinding.inflate(layoutInflater, parent, false)
                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        rootView = holder.itemView.rootView
        myViewHolders.add(holder)
        val currentFavorite = favoriteActivitys[position]
        holder.bind(currentFavorite)
        //                SINGLE CLICK LISTENER TO GO TO DETAILSFRAGMENT

        holder.itemView.findViewById<ConstraintLayout>(R.id.favorites_rowLayout_ConstraintLayout)
            .setOnClickListener {
                if (multiSelection) {
                    applySelection(holder, currentFavorite)
                } else {
                    val action =
                        FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(
                            currentFavorite.result
                        )
                    holder.itemView.findNavController().navigate(action)
                }

            }

        //                 LONG CLICK LISTENER FOR CONTEXTUAL ACTION MODE
        holder.itemView.findViewById<ConstraintLayout>(R.id.favorites_rowLayout_ConstraintLayout)
            .setOnLongClickListener {
                if (!multiSelection) {
                    multiSelection = true
                    requireActivity.startActionMode(this)
                    applySelection(holder, currentFavorite)
                    true
                } else {
                    multiSelection = false
                    false
                }


            }

    }

    //
    private fun applySelection(holder: MyViewHolder, currentFavorite: FavoriteEntity) {
        if (selectedFavorites.contains(currentFavorite)) {
            selectedFavorites.remove(currentFavorite)
            changeFavoriteStyle(holder, R.color.backgroundColorSecond)
            applyActionModeTitle()
        } else {
            selectedFavorites.add(currentFavorite)
            changeFavoriteStyle(holder, R.color.checkChipBackgroundColor)
            applyActionModeTitle()
        }
    }

    private fun changeFavoriteStyle(holder: MyViewHolder, backgroundColor: Int) {
        holder.itemView.findViewById<ConstraintLayout>(R.id.favoriteRowConstraintSecond)
            .setBackgroundColor(
                ContextCompat.getColor(requireActivity, backgroundColor)
            )
    }

    private fun applyActionModeTitle() {
        when (selectedFavorites.size) {
            0 -> {
                mActionmode.finish()
            }
            1 -> {
                mActionmode.title = "${selectedFavorites.size} item selected"
            }
            else -> {
                mActionmode.title = "${selectedFavorites.size} items selected"
            }

        }
    }

    override fun getItemCount(): Int {
        return favoriteActivitys.size
    }


    //                     CONTEXTUAL ACTION MODE METHODS
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {

        mode?.menuInflater?.inflate(R.menu.favorites_menu_contextual, menu)
        mActionmode = mode!!
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {

        return true
    }

    //           DELETING EVERY FAVORITE SELECTED
    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {

        if (item?.itemId == R.id.delete_favorite_activity_menu) {
            selectedFavorites.forEach {
                mainViewModel.deleteFavoriteActivity(it)
            }
            showSnackbar("${selectedFavorites.size} Favorites removed")
            multiSelection = false
            selectedFavorites.clear()
            mode?.finish()
        }

        //    SHARE FAVORITE ; I MANAGED TO ONLY SENT ONE FAVORITE SELECTED : TRIED TO SEND MULTIPLE ONES THAT ARE SELECTED IN ACTION MODE BUT COULDNT MANAGE YET
        if (item?.itemId == R.id.shareFavorite) {

            val intent = Intent().apply {
                this.action = Intent.ACTION_SEND
                this.putExtra(Intent.EXTRA_TEXT, selectedFavorites[0].result.activity)
                this.type = "text/plain"
            }
            startActivity(rootView.context, intent, null)
            multiSelection = false
            selectedFavorites.clear()
            mode?.finish()
        }
        return true
    }

    override fun onDestroyActionMode(mode: ActionMode?) {

        myViewHolders.forEach { holder ->
            changeFavoriteStyle(holder, R.color.backgroundColorSecond)
        }
        multiSelection = false
        selectedFavorites.clear()

    }


    //   INSTEAD OF NOTIFYDATASETCHANGED : IM USING DIFFUTIL AND UPDATING THE ADAPTER IN THIS WAY WITH NEW DATA
    fun setData(newFavoriteActivitys: List<FavoriteEntity>) {
        val boredDiffutil =
            BoredDiffUtil(favoriteActivitys, newFavoriteActivitys)
        val diffUtilResult = DiffUtil.calculateDiff(boredDiffutil)
        favoriteActivitys = newFavoriteActivitys
        diffUtilResult.dispatchUpdatesTo(this)
    }

    private fun showSnackbar(message: String) {
        Snackbar.make(
            rootView,
            message,
            Snackbar.LENGTH_SHORT
        ).setAction("Okay") {}.show()
    }

    //              CLOSES CONTEXTUAL ACTION MODE WHEN SWITCHING TO ANOTHER FRAGMENT AUTOMATICALLY (GOOGLED THE SOLUTION)

    fun clearContextualActionMode() {
        if (this::mActionmode.isInitialized) {
            mActionmode.finish()
        }
    }

}