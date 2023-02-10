package com.example.demoapp.ui.productslist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.example.demoapp.R
import com.example.demoapp.data.ProductsModel

class ProductsAdapter :
    ListAdapter<ProductsModel, ProductsAdapter.ProductsVH>(DiffCallback()) {

    private var listener: ((ProductsModel) -> Unit)? = null
    private var products = mutableListOf<ProductsModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductsVH {
        return ProductsVH(
            LayoutInflater
                .from(parent.context)
                .inflate(R.layout.products_row, parent, false),
            listener
        )
    }

    override fun onBindViewHolder(holder: ProductsVH, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun addAll(productList: List<ProductsModel>) {
        val lastItem = itemCount
        products.addAll(productList)
        notifyItemRangeInserted(lastItem + 1, products.size)
    }

    fun setListener(listener: ((ProductsModel) -> Unit)) {
        this.listener = listener
    }

    class ProductsVH(
        itemView: View,
        private var listener: ((ProductsModel) -> Unit)? = null
    ) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: ProductsModel) = with(itemView) {
            val image = findViewById<ImageView>(R.id.image)
            val title = findViewById<TextView>(R.id.title)

            title.text = item.title
            image.load(item.thumbnail) {
                placeholder(R.drawable.baseline_error_24)
                transformations(CircleCropTransformation())
                error(R.drawable.baseline_error_24)
                scale(Scale.FIT)
            }

            setOnClickListener {
                listener?.invoke(item)
            }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductsModel>() {
        override fun areItemsTheSame(
            oldItem: ProductsModel,
            newItem: ProductsModel
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ProductsModel,
            newItem: ProductsModel
        ): Boolean {
            return oldItem.id == newItem.id &&
                    oldItem.title == newItem.title &&
                    oldItem.thumbnail == newItem.thumbnail
        }
    }
}