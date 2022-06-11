package guilhermekunz.com.br.k7bank.ui.receipt

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.navArgs
import guilhermekunz.com.br.k7bank.R
import guilhermekunz.com.br.k7bank.api.response.DetailStatementResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementItem
import guilhermekunz.com.br.k7bank.databinding.FragmentReceiptBinding
import guilhermekunz.com.br.k7bank.ui.MainActivity
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onBackPressed()
        getDetailStatement()
        initObserver()
        btnShare()
        val arguments = arguments?.
    }

    private fun onBackPressed() {
        binding.btnBack.setOnClickListener {
            val navController: NavController =
                Navigation.findNavController(activity as MainActivity, R.id.mainNavHostFragment)
            navController.popBackStack(R.id.extractFragment, false)
        }
    }

    private fun getDetailStatement() {
        val statementId = viewModel.myStatementItem?.id ?: 0
        viewModel.statementDetail(statementId.toString())
    }

    private fun initObserver() {
        viewModel.statementDetailResponse.observe(viewLifecycleOwner) {
            setData(it)
        }
        viewModel.loadingStateLiveDate.observe(viewLifecycleOwner) {
            handleProgressBar(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(detailStatementResponse: DetailStatementResponse) {
        binding.tvTypeOfMovement.text = detailStatementResponse.description
        binding.tvValueQuantity.text = "R$ " + detailStatementResponse.amount.toString() + ",00"
        binding.tvReceiverName.text = detailStatementResponse.from
        binding.tvBankingInstitutionName.text = detailStatementResponse.bankName
        binding.tvDate.text = detailStatementResponse.createdAt
        binding.tvAuthenticationNumber.text = detailStatementResponse.id
    }

    private fun handleProgressBar(state: ReceiptViewModel.State) {
        when (state) {
            ReceiptViewModel.State.LOADING -> binding.progressBarReceipt.visibility = View.VISIBLE
            ReceiptViewModel.State.LOADING_FINISHED -> binding.progressBarReceipt.visibility = View.GONE
        }
    }

    private fun btnShare() {
        binding.btnShare.setOnClickListener {
            Toast.makeText(context, "Você é burro cara!", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        fun newInstance(myStatementItem: MyStatementItem) : ReceiptFragment {
            val bundle = Bundle().apply { putParcelable("myStatement", myStatementItem) }
            val receiptFragment = ReceiptFragment()
            receiptFragment.arguments = bundle
        }
    }

}