package com.example.myapplication.ui.components

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.data.RecipeWithDetails

class RecipeAdapter(
    private val onRecipeClick: (Long) -> Unit,
    private val onFavoriteClick: (Int) -> Unit
) : ListAdapter<RecipeWithDetails, RecipeAdapter.RecipeViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recipe_card, parent, false)
        return RecipeViewHolder(view, onRecipeClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RecipeViewHolder(
        itemView: View,
        private val onRecipeClick: (Long) -> Unit,
        private val onFavoriteClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvRecipeTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvRecipeDescription)
        private val tvCategory: TextView = itemView.findViewById(R.id.tvCategory)
        private val tvPrepTime: TextView = itemView.findViewById(R.id.tvPrepTime)
        private val tvServings: TextView = itemView.findViewById(R.id.tvServings)
        private val btnFavorite: ImageButton = itemView.findViewById(R.id.btnFavorite)

        fun bind(item: RecipeWithDetails) {
            tvTitle.text = item.recipe.title
            tvDescription.text = item.recipe.description
            tvCategory.text = item.recipe.category.ifBlank { "General" }
            tvPrepTime.text = "${item.recipe.prepTimeMinutes} min"
            tvServings.text = "${item.recipe.servings} servings"

            val favoriteIcon = if (item.recipe.isFavorite) {
                R.drawable.ic_favorite_filled
            } else {
                R.drawable.ic_favorite_border
            }
            btnFavorite.setImageResource(favoriteIcon)

            itemView.setOnClickListener {
                onRecipeClick(item.recipe.id.toLong())
            }

            btnFavorite.setOnClickListener {
                onFavoriteClick(item.recipe.id)
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<RecipeWithDetails>() {
        override fun areItemsTheSame(oldItem: RecipeWithDetails, newItem: RecipeWithDetails): Boolean {
            return oldItem.recipe.id == newItem.recipe.id
        }

        override fun areContentsTheSame(oldItem: RecipeWithDetails, newItem: RecipeWithDetails): Boolean {
            return oldItem == newItem
        }
    }
}
