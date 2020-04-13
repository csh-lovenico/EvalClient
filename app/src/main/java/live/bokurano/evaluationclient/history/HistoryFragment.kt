package live.bokurano.evaluationclient.history

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.databinding.HistoryFragmentBinding


class HistoryFragment : Fragment() {

    companion object {
        fun newInstance() =
            HistoryFragment()
    }

    private lateinit var viewModel: HistoryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<HistoryFragmentBinding>(
            inflater,
            R.layout.history_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.eval_history_title)
        return inflater.inflate(R.layout.history_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(HistoryViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
