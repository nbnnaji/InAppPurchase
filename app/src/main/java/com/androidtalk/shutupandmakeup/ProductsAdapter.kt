package com.androidtalk.shutupandmakeup

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.appcompat.widget.AppCompatTextView
import androidx.recyclerview.widget.RecyclerView
import com.android.billingclient.api.SkuDetails

class ProductsAdapter( private val list: List<SkuDetails>, val context: Context, private val onProductClicked: (SkuDetails) -> Unit ) : RecyclerView.Adapter<ProductsAdapter.ViewHolder>() {


    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val linearLayout = LayoutInflater.from(context).inflate(R.layout.product_item, parent, false) as LinearLayout
        val viewHolder = ViewHolder(linearLayout)
        linearLayout.setOnClickListener { onProductClicked(list[viewHolder.adapterPosition]) }
        return viewHolder
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
         holder.text.text = list[position].sku + " - " + list[position].price


    }

    class ViewHolder( view: LinearLayout) : RecyclerView.ViewHolder(view) {
        val text: AppCompatTextView = view.findViewById(R.id.textGrid)

    }
}
