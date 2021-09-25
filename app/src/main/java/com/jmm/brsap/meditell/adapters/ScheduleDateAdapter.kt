package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateDateChipBinding
import com.jmm.brsap.meditell.model.*

class ScheduleDateAdapter(private val mListener: ScheduleDateInterface) :
    RecyclerView.Adapter<ScheduleDateAdapter.ScheduleDateViewHolder>() {


    private val mList = mutableListOf<ModelDay>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleDateViewHolder {
        return ScheduleDateViewHolder(
            TemplateDateChipBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: ScheduleDateViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setModelDayList(mList: List<ModelDay>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }

    inner class ScheduleDateViewHolder(
        val binding: TemplateDateChipBinding,
        private val mListener: ScheduleDateInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

            }


        }

        fun bind(item: ModelDay) {
            binding.apply {
                tvTextMonthName.text = item.monthName
                tvTextDate.text = item.dateValue
                tvTextDayName.text = item.dayName
                if (item.isActive){
                    bgLayoutDayChip.setCardBackgroundColor(ContextCompat.getColor(itemView.context,R.color.red_500))
                    tvTextMonthName.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                    tvTextDate.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                    tvTextDayName.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                }
            }
        }
    }

    interface ScheduleDateInterface {

    }


}