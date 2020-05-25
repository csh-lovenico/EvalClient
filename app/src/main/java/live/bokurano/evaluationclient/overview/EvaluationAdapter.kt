package live.bokurano.evaluationclient.overview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import live.bokurano.evaluationclient.database.Evaluation
import live.bokurano.evaluationclient.databinding.OverviewItemBinding

class EvaluationAdapter(val clickListener: EvaluationListener) :
    ListAdapter<Evaluation, EvaluationAdapter.ViewHolder>(EvaluationDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: OverviewItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Evaluation, clickListener: EvaluationListener) {
            binding.evaluation = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = OverviewItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class EvaluationDiffCallback : DiffUtil.ItemCallback<Evaluation>() {
    override fun areItemsTheSame(oldItem: Evaluation, newItem: Evaluation): Boolean {
        return oldItem.evalId == newItem.evalId
    }

    override fun areContentsTheSame(oldItem: Evaluation, newItem: Evaluation): Boolean {
        return oldItem == newItem
    }
}

class EvaluationListener(val clickListener: (evalId: Long) -> Unit) {
    fun onClick(evaluation: Evaluation) = clickListener(evaluation.evalId)
}