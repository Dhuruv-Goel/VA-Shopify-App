package com.example.vashopify.fragments.categories.ui

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vashopify.R
import com.example.vashopify.data.CategoryResponse
import com.example.vashopify.databinding.ItemCategoriesBinding
import com.example.vashopify.fragments.categories.BaseCategoryFragment

class CategoryAdapter(private val context: Context, private val fragmentManager: FragmentManager) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {


   inner class CategoryViewHolder(private val binding: ItemCategoriesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(categoryResponse: CategoryResponse) {
            binding.apply {
                  tvCat.text = categoryResponse.name
                  Glide.with(context)
                      .load(categoryResponse.icon)
                      .into(imgCat)

                root.setOnClickListener {
                    // Create a new instance of the fragment
                    val fragment = BaseCategoryFragment()
                    // Pass data to the fragment using arguments
                    val bundle = Bundle()
                    bundle.putString("categoryName", categoryResponse.name)
                    fragment.arguments = bundle

                    // Begin the transaction
                    val transaction: FragmentTransaction = fragmentManager.beginTransaction()
                    // Replace the existing fragment with the new one
                    transaction.replace(R.id.flShopping, fragment)
                    // Add the transaction to the back stack (optional)
                    transaction.addToBackStack(null)
                    // Commit the transaction
                    transaction.commit()
                }
            }
        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CategoryResponse>() {
        override fun areItemsTheSame(oldItem: CategoryResponse, newItem: CategoryResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: CategoryResponse,
            newItem: CategoryResponse
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemCategoriesBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = differ.currentList[position]
        holder.bind(category)
    }

    fun setData(newList: List<CategoryResponse>) {
        differ.submitList(newList)
    }

}