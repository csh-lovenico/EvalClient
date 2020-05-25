package live.bokurano.evaluationclient.history

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.databinding.HistoryFragmentBinding
import live.bokurano.evaluationclient.network.LoginResponse


class HistoryFragment : Fragment() {
    private val viewModel: HistoryViewModel by lazy {
        val sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val loginResponse = LoginResponse(
            sharedPreferences.getString("jwtToken", "undefined")!!,
            sharedPreferences.getString("userId", "undefined")!!,
            sharedPreferences.getString("userRole", "undefined")!!
        )
        val factory = HistoryViewModelFactory(loginResponse)
        ViewModelProvider(this,factory).get(HistoryViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.eval_history_title)
        val binding = DataBindingUtil.inflate<HistoryFragmentBinding>(
            inflater,
            R.layout.history_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        val adapter = HistoryAdapter()
        binding.historyList.adapter = adapter
        viewModel.historyList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })
        return binding.root
    }
}
