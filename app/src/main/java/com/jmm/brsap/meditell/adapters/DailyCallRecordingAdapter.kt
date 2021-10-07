package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.databinding.TemplateCallRecordingItemBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.convertDMY2STD


class DailyCallRecordingAdapter(private val dailySummaryLocationInterface: DailySummaryLocationAdapter.DailySummaryInterface) :
    RecyclerView.Adapter<DailyCallRecordingAdapter.ScheduleViewHolder>() {


    private val mList = mutableListOf<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            TemplateCallRecordingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
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
        val binding: TemplateCallRecordingItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

            }


        }

        fun bind(item: Schedule) {
            binding.apply {
//                tvScheduleDate.text = convertEpochTimeToDate(item.date!!.seconds)
                tvTime.text = convertDMY2STD(item.date!!)
               /* val areas = StringBuilder()
                for (area in item.scheduleAreas){
                    areas.append(area.second).append(",")
                }*/
//                tvAreas.text = areas.toString()
                val selectedAreaAdapter = DailySummaryLocationAdapter(dailySummaryLocationInterface)
                rvSummaries.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = selectedAreaAdapter
                }

                item.scheduleAreas.forEach {
                    it.dateVisit = item.date
                }
                selectedAreaAdapter.setAreaList(item.scheduleAreas)
            }
        }
    }

}