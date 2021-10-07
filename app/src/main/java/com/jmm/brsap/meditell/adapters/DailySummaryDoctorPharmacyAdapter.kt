package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.databinding.LayoutCurrentDayLocationVisitBinding
import com.jmm.brsap.meditell.databinding.TemplateCallRecordingDoctorBinding
import com.jmm.brsap.meditell.databinding.TemplateCallRecordingPharmacyBinding
import com.jmm.brsap.meditell.model.*

class DailySummaryDoctorPharmacyAdapter(private val dailySummaryInterface: DailySummaryInterface) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private val mList = mutableListOf<InteractionModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        return if(viewType==0)DoctorSummaryViewHolder(
            TemplateCallRecordingDoctorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),dailySummaryInterface
        ) else PharmacySummaryViewHolder(
                TemplateCallRecordingPharmacyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                ),dailySummaryInterface
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position)==0){
            (holder as DoctorSummaryViewHolder).bind(mList[position])
        }else (holder as PharmacySummaryViewHolder).bind(mList[position])

    }

    override fun getItemViewType(position: Int): Int {
        return if (mList[position].interactionWasWith=="doctor") 0 else 1
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setAreaList(mList: List<InteractionModel>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }

    inner class DoctorSummaryViewHolder(
        val binding: TemplateCallRecordingDoctorBinding,
        private val mListener:DailySummaryInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnVisitLocation.setOnClickListener {
                mListener.onSummaryClick(mList[adapterPosition])
            }


        }

        fun bind(item: InteractionModel) {
            binding.apply {
                val doctor = item.interactedWithModel as Doctor
                tvTitle.text = doctor.name
                btnVisitLocation.text = "View Summary"
            }
        }
    }

    inner class PharmacySummaryViewHolder(
        val binding: TemplateCallRecordingPharmacyBinding,
        private val mListener:DailySummaryInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnVisitLocation.setOnClickListener {
                mListener.onSummaryClick(mList[adapterPosition])
            }


        }

        fun bind(item: InteractionModel) {
            binding.apply {
                val pharmacy = item.interactedWithModel as Pharmacy

                tvTitle.text = pharmacy.pharmacyName
                tvAddress.text = pharmacy.address
                btnVisitLocation.text = "View Summary"
            }
        }
    }

    interface DailySummaryInterface {
        fun onSummaryClick(item: InteractionModel)
    }


}