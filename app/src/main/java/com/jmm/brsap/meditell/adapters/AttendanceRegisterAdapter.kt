package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateAttendanceItemBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.convertDMY2STD


class AttendanceRegisterAdapter(private val mListener: ScheduleInterface) :
    RecyclerView.Adapter<AttendanceRegisterAdapter.ScheduleViewHolder>() {


    private val mList = mutableListOf<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleViewHolder {
        return ScheduleViewHolder(
            TemplateAttendanceItemBinding.inflate(
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
        val binding: TemplateAttendanceItemBinding,
        private val mListener: ScheduleInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

            }


        }

        fun bind(item: Schedule) {
            binding.apply {
//                tvScheduleDate.text = convertEpochTimeToDate(item.date!!.seconds)
                tvScheduleDate.text = convertDMY2STD(item.date!!)
               /* val areas = StringBuilder()
                for (area in item.scheduleAreas){
                    areas.append(area.second).append(",")
                }*/
//                tvAreas.text = areas.toString()
                val selectedAreaAdapter = AreaListAdapter()
                rvAreas.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = selectedAreaAdapter
                }
                selectedAreaAdapter.setAreaList(item.scheduleAreas)

                if(item.dayStatus==0){
                    tvStatus.apply {
                        text = "Attended"
                        setTextColor(ContextCompat.getColor(context,R.color.colorPositive))
                    }
                }else{
                    tvStatus.apply {
                        text = "Missed"
                        setTextColor(ContextCompat.getColor(context,R.color.colorPrimary))
                    }
                }
            }
        }
    }

    interface ScheduleInterface {

    }


}