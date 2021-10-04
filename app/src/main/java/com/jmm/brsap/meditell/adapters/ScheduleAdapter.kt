package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.databinding.TemplateScheduleItemBinding
import com.jmm.brsap.meditell.model.Schedule
import com.jmm.brsap.meditell.util.convertDMY2EMDY
import com.jmm.brsap.meditell.util.convertDMY2STD


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
                tvScheduleDate.text = convertDMY2STD(item.date!!)
               /* val areas = StringBuilder()
                for (area in item.scheduleAreas){
                    areas.append(area.second).append(",")
                }*/
//                tvAreas.text = areas.toString()
                val selectedAreaAdapter = SelectedAreaAdapter()
                rvAreas.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = selectedAreaAdapter
                }
                selectedAreaAdapter.setAreaList(item.scheduleAreas)
            }
        }
    }

    interface ScheduleInterface {

    }


}