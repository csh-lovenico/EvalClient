package live.bokurano.evaluationclient.login

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.navigation.NavigationView
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.databinding.LoginFragmentBinding
import live.bokurano.evaluationclient.network.LoginUser


class LoginFragment : Fragment() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this).get(LoginViewModel::class.java)
    }

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
            viewModel.login(
                LoginUser(
                    binding.usernameInput.text.toString(),
                    binding.passwordInput.text.toString()
                )
            )
            // Hide the keyboard.
            val inputMethodManager =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
        }

        viewModel.showErrorToast.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(context, getString(R.string.network_error_text), Toast.LENGTH_LONG)
                    .show()
                viewModel.showErrorToastComplete()
                viewModel.loginComplete()
            }
        })

        viewModel.showIncorrectToast.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                Toast.makeText(
                    context,
                    getString(R.string.incorrect_password_text),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.showIncorrectToastComplete()
                viewModel.loginComplete()
            }
        })

        viewModel.isLoggingIn.observe(viewLifecycleOwner, Observer {
            binding.loginButton.isEnabled = !it
            binding.loginButton.text = when (it) {
                true -> getString(R.string.logging_in_text)
                false -> getString(R.string.login_button)
            }
        })

        viewModel.navigateToMain.observe(viewLifecycleOwner, Observer {
            if (it == true) {
                findNavController().navigate(
                    LoginFragmentDirections.actionLoginFragmentToOverviewFragment(
                        true
                    )
                )
                Toast.makeText(context, getString(R.string.login_success_text), Toast.LENGTH_LONG).show()
                activity!!.findViewById<NavigationView>(R.id.navView).menu.clear()
                activity!!.findViewById<NavigationView>(R.id.navView).inflateMenu(R.menu.main_menu)
                viewModel.navigateComplete()
            }
        })

        viewModel.response.observe(viewLifecycleOwner, Observer {
            if (it != null) {
                val editor = activity!!.getSharedPreferences("user", Context.MODE_PRIVATE).edit()
                editor.putString("jwtToken", it.jwtToken)
                editor.putString("userId", it.userId)
                editor.putString("userRole", it.userRole)
                editor.apply()
                viewModel.clearCache()
            }
        })

        return binding.root
    }
}
