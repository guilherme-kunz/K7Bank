package guilhermekunz.com.br.k7bank.ui.extract

import android.annotation.SuppressLint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import guilhermekunz.com.br.k7bank.api.response.MyStatementItem
import guilhermekunz.com.br.k7bank.databinding.ListOfTransfersItemBinding
import guilhermekunz.com.br.k7bank.utils.DateUtils

class ExtractAdapter() : RecyclerView.Adapter<ExtractAdapter.ViewHolder>() {

    private val transfersList: MutableList<MyStatementItem> = mutableListOf()
    private var clickListener: ClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ListOfTransfersItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: ExtractAdapter.ViewHolder, position: Int) {
        holder.bind(transfersList[position])
    }

    override fun getItemCount(): Int = transfersList.size

    fun append(transfersList: List<MyStatementItem>) {
        this.transfersList.clear()
        this.transfersList.addAll(transfersList)
        notifyDataSetChanged()
    }

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    inner class ViewHolder(binding: ListOfTransfersItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private lateinit var myStatementItem: MyStatementItem
        private val tvDescription = binding.tvExtractMovementType
        private val tvFrom = binding.tvExtractMovementAccountName
        private val tvAmount = binding.tvExtractAmountOfMovement
        private val tvCreatedAt = binding.tvExtractMovementDate
        private val btnPix = binding.btnPix

        init {
            itemView.let {
                it.setOnClickListener {
                    if (::myStatementItem.isInitialized) {
                        clickListener?.onItemClick(myStatementItem, absoluteAdapterPosition)
                    }
                }
            }
        }

        @RequiresApi(Build.VERSION_CODES.O)
        @SuppressLint("SetTextI18n")
        fun bind(myStatementItem: MyStatementItem) {
            this.myStatementItem = myStatementItem
            tvDescription.text = myStatementItem.description
            val from: String? = myStatementItem.from
            val to: String? = myStatementItem.to
            val textFrom = when {
                from != null -> from
                to != null -> to
                else -> ""
            }
            tvFrom.text = textFrom
            tvAmount.text = "R$ " + myStatementItem.amount.toString()
            val dateFormatted = DateUtils.dateTimeFormatter(myStatementItem.createdAt, false)
            tvCreatedAt.text = dateFormatted
            when (myStatementItem.tType) {
                PIX_OUT -> btnPix.visibility = View.VISIBLE
                PIX_IN -> btnPix.visibility = View.VISIBLE
                else -> btnPix.visibility = View.GONE
            }
        }

    }

    interface ClickListener {
        fun onItemClick(myStatementItem: MyStatementItem, position: Int)
    }

    companion object {
        val PIX_OUT = "PIXCASHOUT"
        val PIX_IN = "PIXCASHIN"
    }

}