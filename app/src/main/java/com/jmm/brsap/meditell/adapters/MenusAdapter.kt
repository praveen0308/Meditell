package com.jmm.brsap.meditell.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.jmm.brsap.meditell.databinding.TemplateMenuBinding
import com.jmm.brsap.meditell.model.ModelMenu

class MenusAdapter(private val mListener: MenusInterface) :
    RecyclerView.Adapter<MenusAdapter.MenusViewHolder>() {


    private val mList = mutableListOf<ModelMenu>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenusViewHolder {
        return MenusViewHolder(
            TemplateMenuBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ), mListener
        )
    }

    override fun onBindViewHolder(holder: MenusViewHolder, position: Int) {
        holder.bind(mList[position])
    }

    override fun getItemCount(): Int {
        return mList.size
    }

    fun setModelMenuList(mList: List<ModelMenu>) {
        this.mList.clear()
        this.mList.addAll(mList)
        notifyDataSetChanged()
    }

    inner class MenusViewHolder(
        val binding: TemplateMenuBinding,
        private val mListener: MenusInterface
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            itemView.setOnClickListener {
                mListener.onMenuClick(adapterPosition)
            }


        }

        fun bind(item: ModelMenu) {
            binding.apply {
                tvIcon.setImageResource(item.imageUrl)
                tvTitle.text = item.title
            }
        }
    }

    interface MenusInterface {
        fun onMenuClick(menuId:Int)
    }


}