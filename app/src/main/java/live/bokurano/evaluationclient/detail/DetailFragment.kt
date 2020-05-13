package live.bokurano.evaluationclient.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.database.EvaluationDatabase
import live.bokurano.evaluationclient.databinding.DetailFragmentBinding
import timber.log.Timber


class DetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.detail_title)
        val binding = DataBindingUtil.inflate<DetailFragmentBinding>(
            inflater,
            R.layout.detail_fragment,
            container,
            false
        )
        val application = requireNotNull(this.activity).application
        val arguments = DetailFragmentArgs.fromBundle(requireArguments())
        val dataSource = EvaluationDatabase.getInstance(application).evaluationDao
        val viewModelFactory = DetailViewModelFactory(dataSource, arguments.evaluationId)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        binding.evalTotalSeekbar.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                binding.evalTotalScore.text = progress.toString()
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        binding.saveButton.setOnClickListener {
            viewModel.validateForm(
                binding.evalTotalSeekbar.progress,
                binding.evalComment.text.toString()
            )
        }

        viewModel.validateFail.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, "保存失败：评分需为1-5分，评论需多于10个字符", Toast.LENGTH_LONG).show()
                viewModel.validateComplete()
            }
        })

        viewModel.validateSuccess.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, "保存成功", Toast.LENGTH_LONG).show()
                viewModel.validateComplete()
            }
        })

        viewModel.navigateBack.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(DetailFragmentDirections.actionDetailFragmentToOverviewFragment())
                viewModel.doneNavigating()
            }
        })

        return binding.root
    }
}
