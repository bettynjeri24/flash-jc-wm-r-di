package com.ekenya.rnd.tijara.adapters.layoutAdapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ekenya.rnd.tijara.R
import com.ekenya.rnd.tijara.databinding.ItemAboutUsBinding
import com.ekenya.rnd.tijara.network.model.AboutUsItem


class AboutUsAdapter(private val aboutUsList: List<AboutUsItem>) :
    RecyclerView.Adapter<AboutUsAdapter.AboutUsVHolder>() {


    inner class AboutUsVHolder(private val binding: ItemAboutUsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(aboutUsItem: AboutUsItem,v: View) {
            binding.tvAboutUsTitle.text = aboutUsItem.title
            binding.tvAboutUsContent.text = aboutUsItem.content

            binding.tvAboutUsTitle.setOnClickListener {
                aboutUsList[0].visibleByDefault = false
                aboutUsList[1].visibleByDefault = false
                aboutUsItem.visibleByDefault = true
                notifyDataSetChanged()
            }

            if(aboutUsItem.visibleByDefault)
            {
                binding.tvAboutUsContent.visibility = View.VISIBLE
                binding.viewContent.visibility = View.VISIBLE
                val img: Drawable = ContextCompat.getDrawable(v.context, R.drawable.minus_circled)!!
                binding.tvAboutUsTitle.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null)
            }
            else{

                binding.tvAboutUsContent.visibility = View.GONE
                binding.viewContent.visibility = View.GONE
                val img: Drawable = ContextCompat.getDrawable(v.context, R.drawable.plus_circled)!!
                binding.tvAboutUsTitle.setCompoundDrawablesWithIntrinsicBounds(img,null,null,null)
            }


        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AboutUsVHolder {
        return AboutUsVHolder(
            ItemAboutUsBinding.inflate(
                LayoutInflater.from(parent.context)
            )
        )
    }

    override fun getItemCount(): Int {
        return aboutUsList.size
    }

    override fun onBindViewHolder(holder: AboutUsVHolder, position: Int) {
        val aboutUsItem: AboutUsItem = aboutUsList[position]
        holder.bind(aboutUsItem,holder.itemView)

    }

}