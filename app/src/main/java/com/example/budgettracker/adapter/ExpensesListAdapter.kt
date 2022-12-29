package com.example.budgettracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.budgettracker.data.ExpensesAndUserExpenses
import com.example.budgettracker.data.getFormattedCost
import com.example.budgettracker.databinding.ExpensesListItemBinding

class ExpensesListAdapter(
    private val clickListener: (ExpensesAndUserExpenses) -> Unit
): ListAdapter<ExpensesAndUserExpenses, ExpensesListAdapter.ExpensesViewHolder>(DiffCallback) {

    class ExpensesViewHolder(
        private var binding: ExpensesListItemBinding
    ): RecyclerView.ViewHolder(binding.root) {

        fun bind(userExpenses: ExpensesAndUserExpenses) {
            binding.apply{
                itemName.text = userExpenses.itemName
                itemPrice.text = userExpenses.getFormattedCost()
            }
        }
    }

    companion object DiffCallback: DiffUtil.ItemCallback<ExpensesAndUserExpenses>() {
        override fun areItemsTheSame(oldItem: ExpensesAndUserExpenses, newItem: ExpensesAndUserExpenses): Boolean {
            return oldItem.userExpensesId == newItem.userExpensesId
        }

        override fun areContentsTheSame(oldItem: ExpensesAndUserExpenses, newItem: ExpensesAndUserExpenses): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpensesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ExpensesViewHolder(
            ExpensesListItemBinding.inflate(layoutInflater, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ExpensesViewHolder, position: Int) {
        val userExpenses = getItem(position)
        holder.itemView.setOnClickListener{
            clickListener(userExpenses)
        }
        holder.bind(userExpenses)
    }
}