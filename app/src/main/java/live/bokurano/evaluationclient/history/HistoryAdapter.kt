package live.bokurano.evaluationclient.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import live.bokurano.evaluationclient.databinding.HistoryItemBinding
import live.bokurano.evaluationclient.network.WebEvaluation

class HistoryAdapter :
    ListAdapter<WebEvaluation, HistoryAdapter.ViewHolder>(HistoryDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: HistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WebEvaluation) {
            binding.evaluation = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = HistoryItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class HistoryDiffCallback : DiffUtil.ItemCallback<WebEvaluation>() {
    override fun areItemsTheSame(oldItem: WebEvaluation, newItem: WebEvaluation): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WebEvaluation, newItem: WebEvaluation): Boolean {
        return oldItem == newItem
    }
}