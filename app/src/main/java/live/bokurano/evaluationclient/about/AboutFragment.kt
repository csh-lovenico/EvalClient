package live.bokurano.evaluationclient.about

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import live.bokurano.evaluationclient.R
import live.bokurano.evaluationclient.database.EvaluationDatabase
import live.bokurano.evaluationclient.databinding.FragmentAboutBinding

/**
 * A simple [Fragment] subclass.
 */
class AboutFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.title = getString(R.string.about_title)
        // Inflate the layout for this fragment
        val binding = DataBindingUtil.inflate<FragmentAboutBinding>(
            inflater,
            R.layout.fragment_about,
            container,
            false
        )
        binding.clearCacheButton.setOnClickListener {
            ClearConfirmDialog().show(parentFragmentManager, "clearConfirm")
        }
        return binding.root
    }

    class ClearConfirmDialog : DialogFragment() {
        override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
            return activity?.let {
                // Use the Builder class for convenient dialog construction
                val builder = AlertDialog.Builder(it)
                builder.setMessage("是否清除缓存？\n将从系统中登出，\n未上传的评教将不会保存。")
                    .setPositiveButton(
                        getString(R.string.dialog_confirm)
                    ) { _, _ ->
                        activity!!.getSharedPreferences("user", Context.MODE_PRIVATE).edit().clear()
                            .apply()
                        CoroutineScope(Job() + Dispatchers.IO).launch {
                            EvaluationDatabase.getInstance(requireNotNull(activity).application).evaluationDao.clearAll()
                        }
                        findNavController().navigate(AboutFragmentDirections.actionAboutFragmentToOverviewFragment())
                    }
                    .setNegativeButton(
                        getString(R.string.dialog_cancel)
                    ) { _, _ ->
                        // User cancelled the dialog
                    }
                // Create the AlertDialog object and return it
                builder.create()
            } ?: throw IllegalStateException("Activity cannot be null")
        }
    }
}
