package guilhermekunz.com.br.k7bank.utils.dialog

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import guilhermekunz.com.br.k7bank.R

data class DialogModel(
    var title: String,
    var content: String,
    var button: DialogButton
)

data class DialogButton(
    var titleButton: String,
    var action: () -> Unit
)

class GenericDialog(context: Context) : Dialog(context, R.style.RoundedCornersDialog) {

    init {
        setContentView(R.layout.generic_dialog)
    }

    fun setupDialog(model: DialogModel) {
        findViewById<TextView>(R.id.generic_dialog_title).text = model.title
        findViewById<TextView>(R.id.generic_dialog_content).text = model.content
        findViewById<MaterialButton>(R.id.generic_dialog_button).apply {
            text = model.button.titleButton
            setOnClickListener {
                model.button.action.invoke()
            }
        }
    }

}