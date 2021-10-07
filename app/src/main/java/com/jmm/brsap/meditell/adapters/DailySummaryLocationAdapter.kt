package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.databinding.LayoutCurrentDayLocationVisitBinding
import com.jmm.brsap.meditell.model.*

class DailySummaryLocationAdapter(val dailySummaryInterface: DailySummaryInterface) :
    RecyclerView.Adapter<DailySummaryLocationAdapter.SelectedAreaViewHolder>() {


    private val mList = mutableListOf<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedAreaViewHolder {
        return SelectedAreaViewHolder(
            LayoutCurrentDayLocationVisitBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),dailySummaryInterface
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
        val binding: LayoutCurrentDayLocationVisitBinding,
        val mListener:DailySummaryInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnVisitLocation.setOnClickListener {
                mListener.onSummaryClick(mList[adapterPosition])
            }


        }

        fun bind(item: Area) {
            binding.apply {
                tvTitle.text = item.addressInfo
                btnVisitLocation.text = "View Summary"
            }
        }
    }

    interface DailySummaryInterface {
        fun onSummaryClick(item: Area)
    }


}