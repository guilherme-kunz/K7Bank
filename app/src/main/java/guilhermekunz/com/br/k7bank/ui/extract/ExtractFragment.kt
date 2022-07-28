package guilhermekunz.com.br.k7bank.ui.extract

import android.annotation.SuppressLint
import androidx.appcompat.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import guilhermekunz.com.br.k7bank.R
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementItem
import guilhermekunz.com.br.k7bank.api.response.MyStatementResponse
import guilhermekunz.com.br.k7bank.databinding.FragmentExtractBinding
import guilhermekunz.com.br.k7bank.ui.receipt.ReceiptFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExtractFragment : Fragment() {

    private var _binding: FragmentExtractBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ExtractViewModel>()

    private lateinit var extractAdapter: ExtractAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExtractBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMyBalance()
        viewModel.getMyStatement("10", "1")
        initObserver()
        extractAdapter = ExtractAdapter()
        toggleButton()
    }

    private fun initObserver() {
        viewModel.balanceResponse.observe(viewLifecycleOwner) {
            it?.let {
                setAmount(it)
            }
        }
        viewModel.statementResponse.observe(viewLifecycleOwner) {
            it?.let {
                setAdapter(it)
            }
        }
        viewModel.loadingStateLiveDate.observe(viewLifecycleOwner) {
            it?.let {
                handleProgressBar(it)
            }
            viewModel.balanceError.observe(viewLifecycleOwner) {
                apiError()
            }
            viewModel.statementError.observe(viewLifecycleOwner) {
                apiError()
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setAmount(myBalanceResponse: MyBalanceResponse) {
        binding.tvExtractBalance.text = "R$ " + myBalanceResponse.amount.toString()
    }

    private fun setAdapter(myStatementItem: MyStatementResponse) {
        binding.rvYourMovements.apply {
            adapter = extractAdapter
            extractAdapter.append(myStatementItem.myStatementItems)
            extractAdapter.setClickListener(object : ExtractAdapter.ClickListener {
                override fun onItemClick(myStatementItem: MyStatementItem, position: Int) {
                    val fragmentDestination = ReceiptFragment()
                    val fragmentArgs = ReceiptFragment.newInstance(myStatementItem)
                    fragmentDestination.arguments = fragmentArgs
                    val fragmentManager = fragmentManager
                    val fragmentTransition = fragmentManager?.beginTransaction()
                    fragmentTransition?.replace(R.id.mainNavHostFragment, fragmentDestination)
                    fragmentTransition?.commit()
                }
            })
        }
    }

    private fun handleProgressBar(state: ExtractViewModel.State) {
        when (state) {
            ExtractViewModel.State.LOADING -> binding.progressBarExtract.visibility = View.VISIBLE
            ExtractViewModel.State.LOADING_FINISHED -> binding.progressBarExtract.visibility =
                View.GONE
        }
    }

    private fun apiError() {
        val builder = AlertDialog.Builder(requireContext(), R.style.AlertDialogStyle)
        builder.setTitle(getString(R.string.dialog_title))
        builder.setMessage(getString(R.string.alert_dialog_message))
        builder.setNeutralButton(android.R.string.ok) { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }

    private fun toggleButton() {
        binding.ivToggle.apply {
            setOnClickListener {
                if (binding.tvExtractBalance.isVisible) {
                    binding.tvExtractBalance.visibility = View.INVISIBLE
                } else {
                    binding.tvExtractBalance.visibility = View.VISIBLE
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}