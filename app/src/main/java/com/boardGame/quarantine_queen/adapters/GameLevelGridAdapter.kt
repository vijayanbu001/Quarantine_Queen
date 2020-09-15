package com.boardGame.quarantine_queen.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import androidx.databinding.DataBindingUtil
import com.boardGame.quarantine_queen.R
import com.boardGame.quarantine_queen.databinding.GameLevelGridItemBinding

class GameLevelGridAdapter(private val imageList: List<Pair<Int, Int>>) : BaseAdapter() {
    private lateinit var binding: GameLevelGridItemBinding

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        binding = DataBindingUtil.inflate(LayoutInflater.from(parent?.context),
            R.layout.game_level_grid_item,
            parent,
            false)
        val imageView:ImageView = binding.itemImage
        imageView.setImageResource(getItem(position).second )
        return binding.root
    }

    override fun getItem(position: Int): Pair<Int, Int> {
        return imageList.get(position)
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getCount(): Int {
        return imageList.size
    }

}
