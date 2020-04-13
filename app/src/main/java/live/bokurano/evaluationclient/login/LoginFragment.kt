package live.bokurano.evaluationclient.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.databinding.LoginFragmentBinding
import live.bokurano.evaluationclient.network.LoginUser


class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<LoginFragmentBinding>(
            inflater,
            R.layout.login_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.login_title)
        binding.loginButton.setOnClickListener {
//            findNavController().navigate(
//                LoginFragmentDirections.actionLoginFragmentToOverviewFragment(
//                    true
//                )
//            )
//            activity!!.findViewById<NavigationView>(R.id.navView).menu.clear()
//            activity!!.findViewById<NavigationView>(R.id.navView).inflateMenu(R.menu.main_menu)
            viewModel.login(
                LoginUser(
                    binding.usernameInput.text.toString(),
                    binding.passwordInput.text.toString()
                )
            )
        }
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        // TODO: Use the ViewModel
    }

}
