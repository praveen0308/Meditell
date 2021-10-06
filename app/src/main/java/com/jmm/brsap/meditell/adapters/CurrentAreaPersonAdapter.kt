package com.jmm.brsap.meditell.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateDoctorRowBinding
import com.jmm.brsap.meditell.databinding.TemplatePharmacyRowBinding
import com.jmm.brsap.meditell.model.*

class CurrentAreaPersonAdapter(private val mListener: CurrentAreaPersonInterface) :
    RecyclerView.Adapter<CurrentAreaPersonAdapter.CurrentAreaPersonViewHolder>() {


    private val mList = mutableListOf<Any>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrentAreaPersonViewHolder {
        return CurrentAreaPersonViewHolder(
            TemplatePharmacyRowBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: CurrentAreaPersonViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setDoctorList(mList: List<Any>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }

    inner class CurrentAreaPersonViewHolder(
        val binding: TemplatePharmacyRowBinding,
        private val mListener: CurrentAreaPersonInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                mListener.onRowClick(mList[adapterPosition])
            }


        }

        fun bind(item: Any) {
            binding.apply {
                if (item is Doctor){
                    tvName.text = item.name
                    tvAddress.isVisible = false
                    imageView3.setImageResource(R.drawable.doctor_1)
                }
                else if (item is Pharmacy){
                    tvName.text = item.pharmacyName
                    tvAddress.isVisible = true
                    tvAddress.text = item.pharmacyName
                    imageView3.setImageResource(R.drawable.pharmacy_1)
                }
            }
        }
    }

    interface CurrentAreaPersonInterface {
        fun onRowClick(item: Any)
    }


}