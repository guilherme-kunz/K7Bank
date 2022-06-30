package guilhermekunz.com.br.k7bank.ui.receipt

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import guilhermekunz.com.br.k7bank.R
import guilhermekunz.com.br.k7bank.api.response.DetailStatementResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementItem
import guilhermekunz.com.br.k7bank.databinding.FragmentReceiptBinding
import guilhermekunz.com.br.k7bank.ui.MainActivity
import guilhermekunz.com.br.k7bank.utils.DateUtils
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReceiptFragment : Fragment() {

    private var _binding: FragmentReceiptBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ReceiptViewModel>()

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
            val navController: NavController =
                Navigation.findNavController(activity as MainActivity, R.id.mainNavHostFragment)
            navController.navigate(R.id.extractFragment)
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
            setData(it)
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
//        binding.btnShare.setOnClickListener {
//            val bitmap: Bitmap
//            val v1: View = mCurrentUrlMask.getRootView()
//            v1.setDrawingCacheEnabled(true)
//            bitmap = Bitmap.createBitmap(v1.getDrawingCache())
//            v1.setDrawingCacheEnabled(false)
//            var fout: OutputStream? = null
//            imageFile = File(mPath)
//            try {
//                fout = FileOutputStream(imageFile)
//                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fout)
//                fout!!.flush()
//                fout!!.close()
//            } catch (e: FileNotFoundException) {
//                // TODO Auto-generated catch block
//                e.printStackTrace()
//            } catch (e: IOException) {
//                // TODO Auto-generated catch block
//                e.printStackTrace()
//            }
//            val screenShot = screenShot(requireView())
//            val sendIntent: Intent = Intent().apply {
//                action = Intent.ACTION_SEND
//                putExtra(Intent.EXTRA_STREAM, "This is my text to send.")
//                type = "image/jpg"
//            }
//            startActivity(Intent.createChooser(sendIntent, "Teste de compartilhamento"))
    }



private fun screenShot(view: View): Bitmap? {
    val bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    view.draw(canvas)
    return bitmap
}


private fun apiError() {
    Toast.makeText(
        requireContext(),
        "Um erro inesperado aconteceu. Tente novamente mais tarde",
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