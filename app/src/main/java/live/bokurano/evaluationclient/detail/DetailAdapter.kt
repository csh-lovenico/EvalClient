package live.bokurano.evaluationclient.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.RecyclerView
import live.bokurano.evaluationclient.databinding.DetailItemBinding

class DetailAdapter(private val dataset: List<WithScore>, private val viewModel: DetailViewModel) :
    RecyclerView.Adapter<DetailAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        return holder.bind(dataset[position], viewModel)
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    class ViewHolder private constructor(private val binding: DetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: WithScore, viewModel: DetailViewModel) {
            binding.itemDesc.text = item.desc
            binding.itemTitle.text = item.title
            binding.rateBar.progress = item.score
            binding.rateText.text = item.score.toString()
            binding.rateBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    viewModel.rateToEdit[item.id] = progress
                    binding.rateText.text = progress.toString()
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {
                }

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                }
            })
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DetailItemBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }
}