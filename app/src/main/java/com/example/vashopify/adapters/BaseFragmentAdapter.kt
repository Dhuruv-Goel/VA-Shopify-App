package com.example.vashopify.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vashopify.R
import com.example.vashopify.data.CategoryResponse
import com.example.vashopify.data.ProductResponse
import com.example.vashopify.databinding.ProductRvItemBinding
import com.example.vashopify.databinding.ProductRvItemLargeBinding
import com.example.vashopify.fragments.shopping.ProductDetailsFragment

class BaseFragmentAdapter(private val context: Context,private val fragmentManager: FragmentManager):
    RecyclerView.Adapter<BaseFragmentAdapter.BaseFragmentViewHolder>() {

//    private var datalist: List<CategoryResponse> = emptyList()
    //    @SuppressLint("NotifyDataSetChanged")


   inner class BaseFragmentViewHolder(private val binding: ProductRvItemLargeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(productResponse: ProductResponse) {
            binding.apply {
//                Glide.with(itemView).load(product.images[0]).into(imageProductRvItem)
//                tvProductBrandName.text = productResponse.brandName
                tvName.text = productResponse.name
                tvValueMrp.text = "₹ ${productResponse.mrpPrice?.toInt()}"
                tvValueRate.text = "₹ ${productResponse.ratePrice?.toInt()}"

            }
            itemView.setOnClickListener {
                val fragment = ProductDetailsFragment()
                // Pass data to the fragment using arguments
                val bundle = Bundle()
                bundle.putString("id", productResponse._id.toString())
//                bundle.putBoolean("isAdded", productResponse.isAdded)
                bundle.putString("brandName", productResponse.brandName)
                bundle.putString("name", productResponse.name)
                bundle.putString("description", productResponse.description)
                bundle.putString("images", productResponse.images)
                bundle.putString("quantity", productResponse.quantity)
                productResponse.mrpPrice?.let { it1 -> bundle.putInt("mrpPrice", it1.toInt()) }
                productResponse.ratePrice?.let { it1 -> bundle.putInt("ratePrice", it1.toInt()) }
                bundle.putString("isAdded",productResponse.isAdded.toString())
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

    private val diffCallback = object : DiffUtil.ItemCallback<ProductResponse>() {
        override fun areItemsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: ProductResponse, newItem: ProductResponse): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseFragmentViewHolder {
        return BaseFragmentViewHolder(
            ProductRvItemLargeBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BaseFragmentViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
    }
    //    fun setData(newList: List<ProductResponse>) {
////        val diffUtilCallback = ProductResponseDiffCallback(datalist, newList)
////        val diffResult = androidx.recyclerview.widget.DiffUtil.calculateDiff(diffUtilCallback)
//
//        datalist = newList
//        notifyItemRangeChanged(0, datalist.size)
////        diffResult.dispatchUpdatesTo(this)
//    }
    fun setData(newList: List<ProductResponse>) {
        differ.submitList(newList)
    }






}