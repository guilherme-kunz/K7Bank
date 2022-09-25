package guilhermekunz.com.br.k7bank.ui.receipt

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import guilhermekunz.com.br.k7bank.R
import guilhermekunz.com.br.k7bank.api.response.DetailStatementResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementItem
import guilhermekunz.com.br.k7bank.databinding.FragmentReceiptBinding
import guilhermekunz.com.br.k7bank.utils.DateUtils
import guilhermekunz.com.br.k7bank.utils.dialog.DialogButton
import guilhermekunz.com.br.k7bank.utils.dialog.DialogModel
import guilhermekunz.com.br.k7bank.utils.dialog.GenericDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*


class ReceiptFragment : Fragment() {

    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ReceiptViewModel>()

    private val requestMultipleStoragePermissions = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[android.Manifest.permission.WRITE_EXTERNAL_STORAGE] == true || permissions[android.Manifest.permission.READ_EXTERNAL_STORAGE] == true ||
                    permissions[android.Manifest.permission.MANAGE_EXTERNAL_STORAGE] == true -> {
                sharedScreen()
            }
            else -> {
                handlerUserStoragePermissionDenial()
            }
        }
    }

    private val genericDialog by lazy { GenericDialog(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReceiptBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()
        getDetailStatement()
        initObserver()
        btnShare()
    }

    private fun onBackPressed() {
        binding.btnBack.setOnClickListener {
            activity?.onBackPressed()
        }
    }

    private fun getDetailStatement() {
        val myStatementItem = arguments?.getParcelable<MyStatementItem>("myStatementItem")
        if (myStatementItem != null) {
            viewModel.statementDetail(myStatementItem.id)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initObserver() {
        viewModel.statementDetailResponse.observe(viewLifecycleOwner) {
            if (it != null) {
                setData(it)
            }
        }
        viewModel.loadingStateLiveDate.observe(viewLifecycleOwner) {
            handleProgressBar(it)
        }
        viewModel.statementDetailError.observe(viewLifecycleOwner) {
            apiError()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun setData(detailStatementResponse: DetailStatementResponse) {
        binding.tvTypeOfMovement.text = detailStatementResponse.description
        binding.tvValueQuantity.text = "R$ " + detailStatementResponse.amount.toString() + ",00"
        if (detailStatementResponse.from.isNullOrBlank()) {
            binding.tvReceiverName.text = detailStatementResponse.to
        } else {
            binding.tvReceiverName.text = detailStatementResponse.from
        }
        if (detailStatementResponse.bankName.isNullOrBlank()) {
            binding.tvBankingInstitutionName.text = "K7 Bank"
        } else {
            binding.tvBankingInstitutionName.text = detailStatementResponse.bankName
        }
        val dateFormatted = DateUtils.dateTimeFormatter(detailStatementResponse.createdAt, true)
        binding.tvDate.text = dateFormatted
        binding.tvAuthenticationNumber.text = detailStatementResponse.id
    }

    private fun handleProgressBar(state: ReceiptViewModel.State) {
        when (state) {
            ReceiptViewModel.State.LOADING -> binding.progressBarReceipt.visibility = View.VISIBLE
            ReceiptViewModel.State.LOADING_FINISHED -> binding.progressBarReceipt.visibility =
                View.GONE
        }
    }

    private fun btnShare() {
        binding.btnShare.setOnClickListener {
            sendScreenshot()
        }
    }

    private fun sendScreenshot() {
        if (hasStoragePermission()) {
            sharedScreen()
        } else {
            requestMultipleStoragePermissions.launch(
                arrayOf(
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE,
                    android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
                )
            )
        }
    }

    private fun sharedScreen() {
        var bitmap: Bitmap? = null
        val v1: View? = activity?.window?.decorView?.rootView
        if (v1 != null) {
            v1.isDrawingCacheEnabled = true
            bitmap = Bitmap.createBitmap(v1.drawingCache)
            v1.isDrawingCacheEnabled = false
        }
        val mPath = context?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
            .toString() + "/" + "picture" + ".jpg"
        var fout: OutputStream? = null
        val imageFile = File(mPath)
        imageFile.mkdirs() //se não existe um diretorio cria-se um
        try {
            fout = FileOutputStream(imageFile)
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 90, fout)
            fout.flush()
            fout.close()
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        if (bitmap != null) {
            screenShot(bitmap)
        }
    }

    private fun hasStoragePermission(): Boolean = (ActivityCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.WRITE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.READ_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED
            ) || ActivityCompat.checkSelfPermission(
        requireContext(),
        android.Manifest.permission.MANAGE_EXTERNAL_STORAGE
    ) == PackageManager.PERMISSION_GRANTED

    private fun screenShot(bitmap: Bitmap) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "image/jpeg"
        val strPath = MediaStore.Images.Media.insertImage(
            activity?.contentResolver,
            bitmap,
            "testeIMG",
            "teste"
        )
        val uri = Uri.parse(strPath)
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        startActivity(Intent.createChooser(intent, "compartilha imagem"))
    }

    private fun apiError() {
        genericDialog.apply {
            setupDialog(
                DialogModel(
                    title = getString(R.string.dialog_title),
                    content = getString(R.string.alert_dialog_message),
                    button = DialogButton(
                        titleButton = getString(R.string.alert_dialog_button),
                        action = { this.dismiss() }
                    )
                )
            )
        }.show()
    }

    private fun handlerUserStoragePermissionDenial() {
        Toast.makeText(
            requireContext(),
            "Não vai ser possivel compartilhar",
            Toast.LENGTH_LONG
        ).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(myStatementItem: MyStatementItem) = Bundle().apply {
            putParcelable("myStatementItem", myStatementItem)
        }
    }

}