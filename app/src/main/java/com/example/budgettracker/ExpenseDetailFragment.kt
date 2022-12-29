package com.example.budgettracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgettracker.databinding.FragmentExpenseDetailBinding
import com.example.budgettracker.model.ExpensesViewModel
import com.example.budgettracker.model.ExpensesViewModelFactory
import java.util.*

/**
 * Fragment for showing the details of the selected item
 */
class ExpenseDetailFragment : Fragment() {

    private val navigationArgs: ExpenseDetailFragmentArgs by navArgs()
    private var binding: FragmentExpenseDetailBinding? = null
    private val viewModel: ExpensesViewModel by activityViewModels {
        ExpensesViewModelFactory(
            (activity?.application as BudgetApplication).database.userBudgetDao(),
            (activity?.application as BudgetApplication).database.expensesDao(),
            (activity?.application as BudgetApplication).database.userExpensesDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentExpenseDetailBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userExpenseId = navigationArgs.userExpenseId
        val userId = navigationArgs.userId
        val currency = Currency.getInstance(Locale.getDefault())
        val symbol = currency.symbol
        binding!!.currency.text = symbol

        viewModel.retrieveUserExpenses(userExpenseId).observe(this.viewLifecycleOwner){ expenseAndUserExpense ->
            val dateTimeList = expenseAndUserExpense.dateTime.split(" ")
            binding!!.apply {
                itemName.text = expenseAndUserExpense.itemName
                cost.text = expenseAndUserExpense.cost.toString()
                date.text = dateTimeList[0]
                time.text = dateTimeList[1]

                editExpenseFab.setOnClickListener {
                    val action = ExpenseDetailFragmentDirections
                        .actionExpenseDetailFragmentToAddExpenseFragment(userId, getString(R.string.edit_details), userExpenseId)
                    findNavController().navigate(action)
                }

                deleteExpenseFab.setOnClickListener {
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle(R.string.delete_warning)
                    builder.setMessage(R.string.delete_warning_message)
                    builder.setIcon(R.drawable.warning_icon)
                    builder.setPositiveButton(getString(R.string.yes)){ _, _ ->
                        viewModel.deleteUserExpense(userId, userExpenseId, expenseAndUserExpense.cost)
                        Toast.makeText(activity, getString(R.string.delete_details_successful), Toast.LENGTH_SHORT).show()
                        val action = ExpenseDetailFragmentDirections
                            .actionExpenseDetailFragmentToExpensesListFragment(userId)
                        findNavController().navigate(action)
                    }
                    builder.setNegativeButton(getString(R.string.no), null)
                    // Create the AlertDialog
                    val alertDialog: AlertDialog = builder.create()
                    // Set other dialog properties
                    alertDialog.setCancelable(false)
                    alertDialog.show()

                    val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                    negativeButton.setOnClickListener {
                        alertDialog.cancel()
                    }
                }
            }
        }
    }
}