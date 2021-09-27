package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateChooseAreaItemBinding
import com.jmm.brsap.meditell.model.*

class ChooseAreaAdapter(private val mListener: ChooseAreaInterface) :
    RecyclerView.Adapter<ChooseAreaAdapter.ChooseAreaViewHolder>() {


    private val mList = mutableListOf<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseAreaViewHolder {
        return ChooseAreaViewHolder(
            TemplateChooseAreaItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: ChooseAreaViewHolder, position: Int) {
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

    inner class ChooseAreaViewHolder(
        val binding: TemplateChooseAreaItemBinding,
        private val mListener: ChooseAreaInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

            }


        }

        fun bind(item: Area) {
            binding.apply {
                checkBox.text = item.name
            }
        }
    }

    interface ChooseAreaInterface {

    }


}