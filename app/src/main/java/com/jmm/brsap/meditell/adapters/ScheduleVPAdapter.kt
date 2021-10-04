package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.LayoutCurrentDateScheduleBinding
import com.jmm.brsap.meditell.model.*

class ScheduleVPAdapter(val currentDateAreaVisitInterface: CurrentDateAreaVisitAdapter.CurrentDateAreaVisitInterface) :
    RecyclerView.Adapter<ScheduleVPAdapter.ScheduleVPViewHolder>() {


    private val mList = mutableListOf<Schedule>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScheduleVPViewHolder {
        return ScheduleVPViewHolder(
            LayoutCurrentDateScheduleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ScheduleVPViewHolder, position: Int) {
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

    inner class ScheduleVPViewHolder(
        val binding: LayoutCurrentDateScheduleBinding

    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {

            }


        }

        fun bind(item: Schedule) {
            binding.apply {
                val currentDateAreaVisitAdapter =
                    CurrentDateAreaVisitAdapter(currentDateAreaVisitInterface)
                rvCurrentDateSchedule.apply {
                    setHasFixedSize(true)
                    layoutManager = LinearLayoutManager(context)
                    adapter = currentDateAreaVisitAdapter
                }

                currentDateAreaVisitAdapter.setAreaList(item.scheduleAreas)
            }
        }
    }

    interface ScheduleVPInterface {

    }


}