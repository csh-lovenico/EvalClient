package live.bokurano.evaluationclient.overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.databinding.OverviewFragmentBinding


class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() =
            OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<OverviewFragmentBinding>(
            inflater,
            R.layout.overview_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        val args = OverviewFragmentArgs.fromBundle(arguments!!)
        if (args.isLoggedIn) {
            binding.uploadButton.visibility = View.VISIBLE
            binding.overviewStat.visibility = View.VISIBLE
            binding.overviewDesc.visibility = View.VISIBLE
            binding.uploadButton.visibility = View.VISIBLE
            binding.notLoggedInLogo.visibility = View.GONE
            binding.notLoggedInTitle.visibility = View.GONE
            binding.notLoggedInPrompt.visibility = View.GONE
        } else {
            binding.uploadButton.visibility = View.GONE
            binding.overviewStat.visibility = View.GONE
            binding.uploadButton.visibility = View.GONE
            binding.overviewDesc.visibility = View.GONE
            binding.notLoggedInLogo.visibility = View.VISIBLE
            binding.notLoggedInTitle.visibility = View.VISIBLE
            binding.notLoggedInPrompt.visibility = View.VISIBLE
        }

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(OverviewViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
