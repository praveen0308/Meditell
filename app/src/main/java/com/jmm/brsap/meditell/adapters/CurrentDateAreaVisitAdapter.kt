package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.LayoutCurrentDateScheduleBinding
import com.jmm.brsap.meditell.databinding.LayoutCurrentDayLocationVisitBinding
import com.jmm.brsap.meditell.databinding.TemplateAreaListItemBinding
import com.jmm.brsap.meditell.model.*

class CurrentDateAreaVisitAdapter(private val mListener: CurrentDateAreaVisitInterface) :
    RecyclerView.Adapter<CurrentDateAreaVisitAdapter.CurrentDateAreaVisitViewHolder>() {


    private val mList = mutableListOf<Area>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CurrentDateAreaVisitViewHolder {
        return CurrentDateAreaVisitViewHolder(
            LayoutCurrentDayLocationVisitBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: CurrentDateAreaVisitViewHolder, position: Int) {
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

    inner class CurrentDateAreaVisitViewHolder(
        val binding: LayoutCurrentDayLocationVisitBinding,
        private val mListener: CurrentDateAreaVisitInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnVisitLocation.setOnClickListener {
                mListener.onItemClick(mList[adapterPosition])
            }


        }

        fun bind(item: Area) {
            binding.apply {
                tvTitle.text  = item.addressInfo
            }
        }
    }

    interface CurrentDateAreaVisitInterface {
        fun onItemClick(item:Area)
    }


}