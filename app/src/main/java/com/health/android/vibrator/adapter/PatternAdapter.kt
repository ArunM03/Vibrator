package com.health.android.vibrator.adapter

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.health.android.vibrator.R
import com.health.android.vibrator.data.PatternData
import com.health.android.vibrator.databinding.RvPatternBinding
import com.health.android.vibrator.others.Constants


class PatternAdapter(premiumStatus: Boolean) : RecyclerView.Adapter<PatternAdapter.PatternViewHolder>()  {
    
    class PatternViewHolder(val binding : RvPatternBinding) : RecyclerView.ViewHolder(binding.root)

    private var onItemClickListener: ((PatternData) -> Unit)? = null

    fun setOnItemClickListener(position: (PatternData) -> Unit) {
        onItemClickListener = position
    }
    

    private val diffCallback = object : DiffUtil.ItemCallback<PatternData>() {

        override fun areContentsTheSame(oldItem: PatternData, newItem: PatternData): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: PatternData, newItem: PatternData): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }

    }


    private val differ = AsyncListDiffer(this, diffCallback)

    var patternList : List<PatternData>
        get() = differ.currentList
        set(value) = differ.submitList(value)
    

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PatternViewHolder {
        val binding = RvPatternBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return  PatternViewHolder(binding)
    }


    override fun getItemCount(): Int {
        return patternList.size
    }
    

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: PatternViewHolder, position: Int) {
        val data = patternList[position]
        holder.itemView.apply {

            with(holder) {
                
                binding.ivPattern.setImageResource(data.patternImg)

                if( data.patternName == Constants.selectedPattern){

                    binding.ctPattern.setBackgroundColor(ContextCompat.getColor(context, R.color.main_color))
                    binding.ivPattern.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.white))

                   if( Constants.isRunning ){
                       val animation = AlphaAnimation(0.0f,1.0f)
                       animation.duration = 1000
                       animation.startOffset = 20
                       animation.repeatMode = Animation.REVERSE
                       animation.repeatCount = Animation.INFINITE
                       binding.ivPattern.startAnimation(animation)
                   }else{
                       binding.ivPattern.clearAnimation()
                   }

                }else{

                    binding.ctPattern.setBackgroundColor(ContextCompat.getColor(context, R.color.secondary_color))
                    binding.ivPattern.imageTintList = ColorStateList.valueOf(ContextCompat.getColor(context,R.color.black))
                    binding.ivPattern.clearAnimation()

                }


                if (data.isPremium){
                    binding.ivPremium.visibility = View.VISIBLE
                } else{
                    binding.ivPremium.visibility = View.GONE
                }
                
                binding.tvPatternname.text = data.patternName

            }

            setOnClickListener {

                onItemClickListener?.let {
                        click ->
                    click(data)
                }

            }
        }
    }


}