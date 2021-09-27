package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.databinding.TemplateScheduleItemBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.convertDMY2EMDY


class ScheduleAdapter(private val mListener: ScheduleInterface) :
    RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>() {


    private val mList = mutableListOf<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            TemplateScheduleItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: ScheduleViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setScheduleList(mList: List<Schedule>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }

    inner class ScheduleViewHolder(
        val binding: TemplateScheduleItemBinding,
        private val mListener: ScheduleInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

            }


        }

        fun bind(item: Schedule) {
            binding.apply {
//                tvScheduleDate.text = convertEpochTimeToDate(item.date!!.seconds)
                tvScheduleDate.text = convertDMY2EMDY(item.date!!)
                val areas = StringBuilder()
                for (area in item.areaVisits!!){
                    areas.append(area.value).append(",")
                }
                tvAreas.text = areas.toString()
            }
        }
    }

    interface ScheduleInterface {

    }


}