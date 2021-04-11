package cat.covidcontact.tracker.feature.contactnetworks

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AlertDialog
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.databinding.DialogCreateContactNetworkBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object CreateContactNetworkDialog {
    private var dialog: AlertDialog? = null

    fun showDialog(
        context: Context,
        binding: DialogCreateContactNetworkBinding
    ) {
        val builder = MaterialAlertDialogBuilder(context, R.style.dialog)
            .setTitle(R.string.create_contact_network_title)
            .setView(binding.root)
            .setCancelable(false)

        dialog = builder.create()
        dialog?.show()
    }

    fun dismissDialog() {
        if (dialog != null) {
            Log.i("Test", "dismissDialog: DISMISSED")
            dialog?.dismiss()
            dialog = null
        }
    }
}
