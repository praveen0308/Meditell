package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateDateChipBinding
import com.jmm.brsap.meditell.databinding.TemplateSelectedDateBinding
import com.jmm.brsap.meditell.model.*
import com.jmm.brsap.meditell.util.SDF_DMY_WITH_DASH
import com.jmm.brsap.meditell.util.getDayName
import com.jmm.brsap.meditell.util.getMonthName
import java.util.*

class ScheduleDateAdapter(private val mListener: ScheduleDateInterface, private val mType:Int=2) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mList = mutableListOf<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (mType==1){
            SelectedDateViewHolder(
                TemplateSelectedDateBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), mListener
            )
        }else{
            ScheduleDateViewHolder(
                TemplateDateChipBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ), mListener
            )
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (mType==1){
            (holder as SelectedDateViewHolder).bind(mList[position])
        }
        else{
            (holder as ScheduleDateViewHolder).bind(mList[position])
        }
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
                mListener.onDayClick(adapterPosition,mList[adapterPosition])
            }

        }

        fun bind(item: Schedule) {
            binding.apply {
                val date:Date = SDF_DMY_WITH_DASH.parse(item.date!!)!!
                val cal1 = Calendar.getInstance()
                cal1.setTime(date)
                tvTextMonthName.text = getMonthName(cal1.get(Calendar.MONTH))
                tvTextDate.text = cal1.get(Calendar.DATE).toString()
                tvTextDayName.text = getDayName(cal1.get(Calendar.DAY_OF_WEEK))
                if (item.isActive){
                    bgLayoutDayChip.setStrokeColor(ContextCompat.getColor(itemView.context,R.color.colorPrimary))
                    bgLayoutDayChip.strokeWidth = 5
                }else{
                    bgLayoutDayChip.setStrokeColor(null)
                    bgLayoutDayChip.strokeWidth = 0
                }
            }
        }
    }


    inner class SelectedDateViewHolder(
        val binding: TemplateSelectedDateBinding,
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
                val myDate = "${cal1.get(Calendar.DATE)} ${getMonthName(cal1.get(Calendar.MONTH)).take(3)}"
                tvDate.text = myDate
                tvDay.text = getDayName(cal1.get(Calendar.DAY_OF_WEEK)).take(3)

            }
        }
    }

    interface ScheduleDateInterface {
        fun onDayClick(position: Int,item: Schedule)
    }


}