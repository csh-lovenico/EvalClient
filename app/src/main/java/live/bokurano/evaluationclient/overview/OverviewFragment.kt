package live.bokurano.evaluationclient.overview

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
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.database.EvaluationDatabase
import live.bokurano.evaluationclient.databinding.OverviewFragmentBinding
import live.bokurano.evaluationclient.network.LoginResponse
import live.bokurano.evaluationclient.teacher.TeacherAdapter
import timber.log.Timber


class OverviewFragment : Fragment() {

    private val viewModel: OverviewViewModel by lazy {
        val application = requireNotNull(this.activity).application
        val dataSource = EvaluationDatabase.getInstance(application).evaluationDao
        val sharedPreferences = requireActivity().getSharedPreferences("user", Context.MODE_PRIVATE)
        val loginResponse = LoginResponse(
            sharedPreferences.getString("jwtToken", "undefined")!!,
            sharedPreferences.getString("userId", "undefined")!!,
            sharedPreferences.getString("userRole", "undefined")!!
        )
        val factory = OverviewViewModelFactory(dataSource, application, loginResponse)
        ViewModelProvider(this, factory).get(OverviewViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.app_name)
        val binding = DataBindingUtil.inflate<OverviewFragmentBinding>(
            inflater,
            R.layout.overview_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this

        viewModel.checkLoginState()

        val adapter = EvaluationAdapter(EvaluationListener { evalId ->
            viewModel.onEvaluationClicked(evalId)
        })

        val teacherAdapter = TeacherAdapter()
        binding.statList.adapter = teacherAdapter

        binding.overviewList.adapter = adapter

        binding.viewModel = viewModel

        binding.overviewList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && binding.uploadButton.visibility == View.VISIBLE) {
                    binding.uploadButton.hide()
                } else if (dy < 0 && binding.uploadButton.visibility != View.VISIBLE) {
                    binding.uploadButton.show()
                }
            }
        })

        viewModel.statList.observe(viewLifecycleOwner, Observer {
            it?.let {
                teacherAdapter.submitList(it)
            }
        })

        viewModel.evaluationList.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        viewModel.loginSuccess.observe(viewLifecycleOwner, Observer {
//            binding.uploadButton.visibility = when (it) {
//                true -> View.VISIBLE
//                false -> View.GONE
//            }
//            binding.overviewStat.visibility = when (it) {
//                true -> View.VISIBLE
//                false -> View.GONE
//            }
//            binding.overviewDesc.visibility = when (it) {
//                true -> View.VISIBLE
//                false -> View.GONE
//            }
//            binding.overviewList.visibility = when (it) {
//                true -> View.VISIBLE
//                false -> View.GONE
//            }
            binding.studentContainer.visibility = when (it) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            if (it == true) {
                requireActivity().findViewById<NavigationView>(R.id.navView).menu.clear()
                requireActivity().findViewById<NavigationView>(R.id.navView)
                    .inflateMenu(R.menu.main_menu)
            }
        })

        viewModel.notLoggedIn.observe(viewLifecycleOwner, Observer {
//            binding.notLoggedInLogo.visibility = when (it) {
//                true -> View.VISIBLE
//                false -> View.GONE
//            }
//            binding.notLoggedInTitle.visibility = when (it) {
//                true -> View.VISIBLE
//                false -> View.GONE
//            }
//            binding.notLoggedInPrompt.visibility = when (it) {
//                true -> View.VISIBLE
//                false -> View.GONE
//            }
            binding.promptContainer.visibility = when (it) {
                true -> View.VISIBLE
                false -> View.GONE
            }
            if (it == true) {
                requireActivity().findViewById<NavigationView>(R.id.navView).menu.clear()
                requireActivity().findViewById<NavigationView>(R.id.navView)
                    .inflateMenu(R.menu.anonymous_menu)
            }
        })

        viewModel.credentialExpired.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.notLoggedInTitle.text = getString(R.string.credential_expired_title)
                viewModel.setStateComplete()
            }
        })

        viewModel.networkError.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                binding.notLoggedInLogo.setImageResource(R.drawable.ic_cloud_off_gray_96dp)
                binding.notLoggedInTitle.text = getString(R.string.network_error_text)
                binding.notLoggedInPrompt.text = getString(R.string.retry_login_prompt)
                viewModel.setStateComplete()
            }
        })

        viewModel.navigateToDetail.observe(viewLifecycleOwner, Observer {
            it?.let {
                findNavController().navigate(
                    OverviewFragmentDirections.actionOverviewFragmentToDetailFragment(
                        it
                    )
                )
                viewModel.onNavigateComplete()
            }
        })

//        viewModel.navigateToTeacher.observe(
//            viewLifecycleOwner, Observer {
//                it?.let {
//                    findNavController().navigate(OverviewFragmentDirections.actionOverviewFragmentToTeacherFragment())
//                    viewModel.onNavigateComplete()
//                }
//            }
//        )

        viewModel.studentMode.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.studentContainer.visibility = when (it) {
                    true -> View.VISIBLE
                    false -> View.GONE
                }
                binding.statList.visibility = when (it) {
                    true -> View.GONE
                    false -> View.VISIBLE
                }
                if (!it) {
                    requireActivity().findViewById<NavigationView>(R.id.navView).menu.clear()
                    requireActivity().findViewById<NavigationView>(R.id.navView)
                        .inflateMenu(R.menu.teacher_menu)
                }
            }
        })
        viewModel.unfinished.observe(viewLifecycleOwner, Observer {
            it?.let {
                binding.uploadButton.isEnabled = it.toInt() <= 0
            }
        })

        binding.uploadButton.setOnClickListener {
            Timber.i(viewModel.evaluationList.value.toString())
            viewModel.checkState()
        }

        viewModel.tooManyFullStar.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(
                    requireActivity().findViewById(R.id.list_container),
                    "上传失败：不符合评教条件",
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.setUploadStateComplete()
            }
        })

        viewModel.uploadSuccess.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(
                    requireActivity().findViewById(R.id.list_container),
                    "上传成功",
                    Snackbar.LENGTH_SHORT
                ).show()
                adapter.notifyDataSetChanged()
                viewModel.setUploadStateComplete()
            }

        })

        viewModel.uploadError.observe(viewLifecycleOwner, Observer {
            it?.let {
                Snackbar.make(
                    requireActivity().findViewById(R.id.list_container),
                    "上传失败：网络错误",
                    Snackbar.LENGTH_SHORT
                ).show()
                viewModel.setUploadStateComplete()
            }
        })
        return binding.root
    }
}
