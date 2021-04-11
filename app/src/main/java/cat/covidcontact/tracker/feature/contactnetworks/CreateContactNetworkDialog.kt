package cat.covidcontact.tracker.feature.contactnetworks

import android.content.Context
import androidx.appcompat.app.AlertDialog
import cat.covidcontact.tracker.R
import cat.covidcontact.tracker.databinding.DialogCreateContactNetworkBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

object CreateContactNetworkDialog {
    private lateinit var dialog: AlertDialog

    fun showDialog(
        context: Context,
        binding: DialogCreateContactNetworkBinding
    ) {
        val builder = MaterialAlertDialogBuilder(context, R.style.dialog)
            .setTitle(R.string.create_contact_network_title)
            .setView(binding.root)

        dialog = builder.create()
        dialog.show()
    }

    fun dismissDialog() {
        if (::dialog.isInitialized) {
            dialog.dismiss()
        }
    }
}
