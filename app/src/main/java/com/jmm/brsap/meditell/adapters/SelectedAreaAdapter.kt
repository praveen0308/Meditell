package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateAreaListItemBinding
import com.jmm.brsap.meditell.model.*

class SelectedAreaAdapter() :
    RecyclerView.Adapter<SelectedAreaAdapter.SelectedAreaViewHolder>() {


    private val mList = mutableListOf<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedAreaViewHolder {
        return SelectedAreaViewHolder(
            TemplateAreaListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: SelectedAreaViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setAreaList(mList: List<Area>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }

    inner class SelectedAreaViewHolder(
        val binding: TemplateAreaListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

            }


        }

        fun bind(item: Area) {
            binding.apply {
                tvTitle.text = item.addressInfo
                divider70.isVisible = !item.isLast
            }
        }
    }

    interface SelectedAreaInterface {

    }


}