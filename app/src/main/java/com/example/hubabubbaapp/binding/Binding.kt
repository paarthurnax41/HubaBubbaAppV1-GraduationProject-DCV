package com.example.hubabubbaapp.binding

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.hubabubbaapp.binding.adapter.FavoriteActivityAdapter
import com.example.hubabubbaapp.data.room.FavoriteEntity
import com.example.hubabubbaapp.model.BoredResult
import com.example.hubabubbaapp.ui.HomeFragmentDirections

class Binding {
    companion object {
        //      BINDING ADAPTER FOR HOMEFRAGMENT TO DETAILS FRAGMENT
        @BindingAdapter("onTextClickListener")
        @JvmStatic
        fun onTextClickListener(textView: TextView, result: BoredResult?) {
            textView.setOnClickListener {
                try {
                    val action =
                        result?.let { it1 ->
                            HomeFragmentDirections.actionHomeFragmentToDetailsFragment(
                                it1
                            )
                        }
                    if (action != null) {
                        textView.findNavController().navigate(action)
                    }
                } catch (e: Exception) {
                    Log.d("BindingAdapterExperimen", e.toString())
                }

            }

        }


        //  FIRST VALUE IN BINDING ADAPTER IS FOR favoritesEntity to show and hide the Views depending on if Database is empty or not
        //  SECOND VALUE IN BINDING ADAPTER IS FOR rAdapter to set the Data to the RecyclerView
        @BindingAdapter("viewVisibility", "setData", requireAll = false)
        @JvmStatic
        fun setDataAndViewVisibility(
            view: View,
            favoritesEntity: List<FavoriteEntity>?,
            rAdapter: FavoriteActivityAdapter?
        ) {

            if (favoritesEntity.isNullOrEmpty()) {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.VISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.VISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.INVISIBLE
                    }
                }
            } else {
                when (view) {
                    is ImageView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is TextView -> {
                        view.visibility = View.INVISIBLE
                    }
                    is RecyclerView -> {
                        view.visibility = View.VISIBLE
                        rAdapter?.setData(favoritesEntity)
                    }
                }

            }
        }
    }
}