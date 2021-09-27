package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateDateChipBinding
import com.jmm.brsap.meditell.model.*
import com.jmm.brsap.meditell.util.SDF_DMY_WITH_DASH
import com.jmm.brsap.meditell.util.getDayName
import com.jmm.brsap.meditell.util.getMonthName
import java.util.*

class ScheduleDateAdapter(private val mListener: ScheduleDateInterface) :
    RecyclerView.Adapter<ScheduleDateAdapter.ScheduleDateViewHolder>() {


    private val mList = mutableListOf<Schedule>()


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

    fun setModelDayList(mList: List<Schedule>) {
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

        fun bind(item: Schedule) {
            binding.apply {
                val date:Date = SDF_DMY_WITH_DASH.parse(item.date!!)!!
                val cal1 = Calendar.getInstance()
                cal1.setTime(date)
                tvTextMonthName.text = getMonthName(cal1.get(Calendar.MONTH)).take(3)
                tvTextDate.text = cal1.get(Calendar.DATE).toString()
                tvTextDayName.text = getDayName(cal1.get(Calendar.DAY_OF_WEEK)).take(3)
                if (item.isActive){
                    bgLayoutDayChip.setCardBackgroundColor(ContextCompat.getColor(itemView.context,R.color.red_500))
                    tvTextMonthName.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                    tvTextDate.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                    tvTextDayName.setTextColor(ContextCompat.getColor(itemView.context,R.color.white))
                }else{
                    bgLayoutDayChip.setCardBackgroundColor(ContextCompat.getColor(itemView.context,R.color.colorGrey))
                    tvTextMonthName.setTextColor(ContextCompat.getColor(itemView.context,R.color.colorTextPrimary))
                    tvTextDate.setTextColor(ContextCompat.getColor(itemView.context,R.color.colorTextPrimary))
                    tvTextDayName.setTextColor(ContextCompat.getColor(itemView.context,R.color.colorTextPrimary))
                }
            }
        }
    }

    interface ScheduleDateInterface {

    }


}