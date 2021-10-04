package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.R
import com.jmm.brsap.meditell.databinding.TemplateSquareChipBinding
import com.jmm.brsap.meditell.model.WidgetModel

class DashboardWidgetAdapter(private val mListener: DashboardWidgetInterface) :
    RecyclerView.Adapter<DashboardWidgetAdapter.DashboardWidgetViewHolder>() {


    private val mList = mutableListOf<WidgetModel>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DashboardWidgetViewHolder {
        return DashboardWidgetViewHolder(
            TemplateSquareChipBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: DashboardWidgetViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setWidgetModelList(mList: List<WidgetModel>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }

    inner class DashboardWidgetViewHolder(
        val binding: TemplateSquareChipBinding,
        private val mListener: DashboardWidgetInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                mListener.onItemClick(mList[adapterPosition])
            }


        }

        fun bind(item: WidgetModel) {
            binding.apply {
                ivIcon.setImageResource(item.imgUrl)
                tvTitle.text = item.title
            }
        }
    }

    interface DashboardWidgetInterface {
        fun onItemClick(item: WidgetModel)
    }


}