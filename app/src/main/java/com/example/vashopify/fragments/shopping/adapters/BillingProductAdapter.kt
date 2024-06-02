package com.example.vashopify.fragments.shopping.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vashopify.data.CartItemResponse
import com.example.vashopify.databinding.BillingRvItemBinding

class BillingProductAdapter(
    private val context: Context
) : RecyclerView.Adapter<BillingProductAdapter.BillingViewHolder>() {


    inner class BillingViewHolder(val binding: BillingRvItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(cartItemResponse: CartItemResponse) {
            binding.apply {

                tvProductCartName.text = cartItemResponse.productId.name
                tvProductCartPrice.text = "₹${cartItemResponse.productId.ratePrice!!.toInt()}"
                tvCartProductQuantity.text = "${cartItemResponse.quantity}"
                Toast.makeText(context, "${cartItemResponse.quantity}", Toast.LENGTH_SHORT).show()

            }
//            itemView.setOnClickListener {
//                val fragment = ProductDetailsFragment()
//                // Pass data to the fragment using arguments
//                val productResponse = cartItemResponse.productId
//                val bundle = Bundle()
//                bundle.putString("id", productResponse._id)
//                bundle.putString("brandName", productResponse.brandName)
//                bundle.putString("name", productResponse.name)
//                bundle.putString("description", productResponse.description)
//                bundle.putString("images", productResponse.images)
//                bundle.putString("quantity", productResponse.quantity)
//                bundle.putInt("mrpPrice", productResponse.mrpPrice.toInt())
//                bundle.putInt("ratePrice", productResponse.ratePrice.toInt())
//                bundle.putString("isAdded", productResponse.isAdded.toString())
//
//                fragment.arguments = bundle
//
//                // Begin the transaction
//                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//                // Replace the existing fragment with the new one
//                transaction.replace(R.id.flShopping, fragment)
//                // Add the transaction to the back stack (optional)
//                transaction.addToBackStack(null)
//                // Commit the transaction
//                transaction.commit()
//            }

        }
    }

    private val diffCallback = object : DiffUtil.ItemCallback<CartItemResponse>() {
        override fun areItemsTheSame(
            oldItem: CartItemResponse,
            newItem: CartItemResponse
        ): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(
            oldItem: CartItemResponse,
            newItem: CartItemResponse
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, diffCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillingViewHolder {
        return BillingViewHolder(
            BillingRvItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: BillingViewHolder, position: Int) {
        val product = differ.currentList[position]
        holder.bind(product)
//        holder.binding.imagePlus.setOnClickListener {
//            onPlusClick(position)
//
//        }
//        holder.binding.imagePlus.setOnClickListener {
//            onMinusClick(position)
//
//        }


    }

    fun setData(newList: List<CartItemResponse>) {
        differ.submitList(newList)
    }
}