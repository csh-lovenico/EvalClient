package live.bokurano.evaluationclient.teacher

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import live.bokurano.evaluationclient.databinding.TeacherItemBinding
import live.bokurano.evaluationclient.network.WebStat

class TeacherAdapter : ListAdapter<WebStat, TeacherAdapter.ViewHolder>(TeacherDiffCallback()) {
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: TeacherItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WebStat) {
            binding.stat = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = TeacherItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}

class TeacherDiffCallback : DiffUtil.ItemCallback<WebStat>() {
    override fun areItemsTheSame(oldItem: WebStat, newItem: WebStat): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: WebStat, newItem: WebStat): Boolean {
        return oldItem == newItem
    }
}