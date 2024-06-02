package com.example.vashopify.fragments.settings

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.vashopify.R
import com.example.vashopify.data.OrderStatus
import com.example.vashopify.data.getOrderStatus
import com.example.vashopify.databinding.OrderItemBinding
import com.example.vashopify.fragments.settings.data.OrderDataResponseItem
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdersAdapter(private val context: Context, private val onItemClickListener: (OrderDataResponseItem) -> Unit):
    RecyclerView.Adapter<OrdersAdapter.OrderViewHolder>() {

//    private var datalist: List<ProductResponse> = emptyList()
    //    @SuppressLint("NotifyDataSetChanged")


    inner  class OrderViewHolder(private val binding: OrderItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun formatDate(inputDate: String, inputFormat: String, outputFormat: String): String {
            val inputDateFormat = SimpleDateFormat(inputFormat, Locale.getDefault())
            val outputDateFormat = SimpleDateFormat(outputFormat, Locale.getDefault())

            try {
                val date: Date = inputDateFormat.parse(inputDate) ?: throw ParseException("Invalid date format", 0)
                return outputDateFormat.format(date)
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return ""
        }
        @SuppressLint("SetTextI18n")
        fun bind(orderResponse: OrderDataResponseItem) {
            binding.apply {
                val originalDate = orderResponse.createdAt
                val originalFormat = "yyyy-MM-dd'T'HH:mm:ss.SSSX"
                val desiredFormat = "dd MMM yyyy HH:mm:ss"

                val formattedDate = formatDate(originalDate, originalFormat, desiredFormat)
//                println("Formatted Date: $formattedDate")
                tvOrderId.text = orderResponse.orderId
                tvOrderDate.text = formattedDate

//                val resources = itemView.resources
                val colorDrawable = when (getOrderStatus(orderResponse.orderStatus)){
                    is OrderStatus.Ordered ->{
                        ColorDrawable(ContextCompat.getColor(context,R.color.g_orange_yellow))
                    }
                    is OrderStatus.Confirmed ->{
                        ColorDrawable(ContextCompat.getColor(context,R.color.g_green))
                    }
                    is OrderStatus.Shipped ->{
                        ColorDrawable(ContextCompat.getColor(context,R.color.g_green))
                    }
                    is OrderStatus.Delivered ->{
                        ColorDrawable(ContextCompat.getColor(context,R.color.g_green))
                    }
                    is OrderStatus.Canceled ->{
                        ColorDrawable(ContextCompat.getColor(context,R.color.g_red))
                    }
                    is OrderStatus.Returned ->{
                        ColorDrawable(ContextCompat.getColor(context,R.color.g_red))
                    }
                }

                imageOrderState.setImageDrawable(colorDrawable)

            }
             itemView.setOnClickListener {
                 itemView.setOnClickListener {
                     // Call the lambda expression when an item is clicked
                     onItemClickListener.invoke(orderResponse)
                 }

             }
//            itemView.setOnClickListener {
//                val fragment = ProductDetailsFragment()
//                // Pass data to the fragment using arguments
//                val bundle = Bundle()
////                bundle.putString("id", productResponse._id.toString())
////                bundle.putString("brandName", productResponse.brandName)
////                bundle.putString("name", productResponse.name)
////                bundle.putString("description", productResponse.description)
////                bundle.putString("images", productResponse.images)
////                bundle.putString("quantity", productResponse.quantity)
////                bundle.putInt("mrpPrice", productResponse.mrpPrice.toInt())
////                bundle.putInt("ratePrice", productResponse.ratePrice.toInt())
////                bundle.putString("isAdded", productResponse.isAdded.toString())
//
//                fragment.arguments = bundle
//
//                // Begin the transaction
//                val transaction: FragmentTransaction = fragmentManager.beginTransaction()
//                // Replace the existing fragment with the new one
//                transaction.replace(R.id.flSearch, fragment)
//                // Add the transaction to the back stack (optional)
//                transaction.addToBackStack(null)
//                // Commit the transaction
//                transaction.commit()
//            }
        }


    }

    private val diffCallback = object : DiffUtil.ItemCallback<OrderDataResponseItem>() {
        override fun areItemsTheSame(oldItem: OrderDataResponseItem, newItem: OrderDataResponseItem): Boolean {
            return oldItem._id == newItem._id
        }

        override fun areContentsTheSame(oldItem: OrderDataResponseItem, newItem: OrderDataResponseItem): Boolean {
            return oldItem == newItem
        }

    }

    val differ = AsyncListDiffer(this, diffCallback)
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderViewHolder {
        return OrderViewHolder(
            OrderItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {
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
    fun setData(newList: List<OrderDataResponseItem>) {
        differ.submitList(newList)
    }
//  var onClick :((OrderDataResponseItem) -> Unit)? = null
}

//interface  onItemClickListener{
//    fun onItemClick(itemData: OrderDataResponseItem){
//
//    }
//}