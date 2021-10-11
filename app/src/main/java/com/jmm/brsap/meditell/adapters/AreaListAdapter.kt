package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateAreaListItemBinding
import com.jmm.brsap.meditell.model.*

class AreaListAdapter() :
    RecyclerView.Adapter<AreaListAdapter.AreaListViewHolder>() {


    private val mList = mutableListOf<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AreaListViewHolder {
        return AreaListViewHolder(
            TemplateAreaListItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AreaListViewHolder, position: Int) {
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

    inner class AreaListViewHolder(
        val binding: TemplateAreaListItemBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        init {

        }

        fun bind(item: Area) {
            binding.apply {
                tvTitle.text = item.addressInfo
                divider70.isVisible = !item.isLast
                imgClose.isVisible = false
            }
        }
    }


}