package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateChooseAreaItemBinding
import com.jmm.brsap.meditell.model.*

class ChooseAreaAdapter(private val mListener: ChooseAreaInterface) :
    RecyclerView.Adapter<ChooseAreaAdapter.ChooseAreaViewHolder>() {


    private val mList = mutableListOf<Area>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChooseAreaViewHolder {
        return ChooseAreaViewHolder(
            TemplateChooseAreaItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: ChooseAreaViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setAreaList(mList: List<Area>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }

    inner class ChooseAreaViewHolder(
        val binding: TemplateChooseAreaItemBinding,
        private val mListener: ChooseAreaInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                Toast.makeText(it.context, "clicked", Toast.LENGTH_SHORT).show()
                if (mList[adapterPosition].isSelected){
                    mList[adapterPosition].isSelected = false
                    mListener.removeOneSelection(mList[adapterPosition])
                }else{
                    mList[adapterPosition].isSelected = true
                    mListener.addNewSelection(mList[adapterPosition])
                }
                notifyDataSetChanged()
            }


        }

        fun bind(item: Area) {
            binding.apply {
                tvTitle.text = item.name

                if (item.isSelected) imgCheckbox.setImageResource(R.drawable.ic_baseline_check_box_24)
                else imgCheckbox.setImageResource(R.drawable.ic_baseline_crop_square_24)


            }
        }
    }

    interface ChooseAreaInterface {
        fun addNewSelection(area: Area)
        fun removeOneSelection(area: Area)
    }


}