package guilhermekunz.com.br.k7bank.ui.extract

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import guilhermekunz.com.br.k7bank.api.response.MyBalanceResponse
import guilhermekunz.com.br.k7bank.api.response.MyStatementItem
import guilhermekunz.com.br.k7bank.api.response.MyStatementResponse
import guilhermekunz.com.br.k7bank.databinding.FragmentExtractBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExtractFragment : Fragment() {

    private var _binding: FragmentExtractBinding? = null
    private val binding get() = _binding!!

    private val viewModel by viewModel<ExtractViewModel>()

    lateinit var extractAdapter: ExtractAdapter

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
    }

    private fun initObserver() {
        viewModel.balanceResponse.observe(viewLifecycleOwner) {
            it?.let {
                setAmount(it)
            }
        }
        viewModel.statementResponse.observe(viewLifecycleOwner){
            it?.let {
                setAdapter(it)
            }
        }
    }

    private fun setAmount(myBalanceResponse: MyBalanceResponse) {
        binding.tvExtractBalance.text = myBalanceResponse.amount.toString()
    }

    private fun setAdapter(myStatementItem : MyStatementResponse) {
        binding.rvYourMovements.apply {
            adapter = extractAdapter
            extractAdapter.append(myStatementItem.myStatementItems)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}