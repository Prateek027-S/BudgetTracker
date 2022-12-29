package com.example.budgettracker

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.budgettracker.adapter.ExpensesListAdapter
import com.example.budgettracker.databinding.FragmentExpensesListBinding
import com.example.budgettracker.model.ExpensesViewModel
import com.example.budgettracker.model.ExpensesViewModelFactory
import java.util.*


/**
 * A Fragment for showing list of Expenses
 */
class ExpensesListFragment : Fragment() {

    private val navigationArgs: ExpensesListFragmentArgs by navArgs()
    private var binding: FragmentExpensesListBinding? = null
    private val viewModel: ExpensesViewModel by activityViewModels {
        ExpensesViewModelFactory(
            (activity?.application as BudgetApplication).database.userBudgetDao(),
            (activity?.application as BudgetApplication).database.expensesDao(),
            (activity?.application as BudgetApplication).database.userExpensesDao()
        )
    }
    private val currency = Currency.getInstance(Locale.getDefault())
    private val symbol = currency.symbol

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentExpensesListBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = navigationArgs.id

        val adapter = ExpensesListAdapter { userExpenses ->
            val action = ExpensesListFragmentDirections
                .actionExpensesListFragmentToExpenseDetailFragment(userExpenses.userExpensesId, userId)
            findNavController().navigate(action)
        }

        // when new items are added, the Recycler View is also updated
        viewModel.retrieveAllUserExpenses(userId).observe(this.viewLifecycleOwner){ userExpenses ->
            userExpenses.let {
                adapter.submitList(it)
            }
        }
        binding!!.recyclerView.layoutManager = LinearLayoutManager(this.context)

        viewModel.getBudget(userId).observe(this.viewLifecycleOwner) { newBudget ->
            val formattedBudget = "$symbol$newBudget"
            binding!!.budget.text = getString(R.string.budget, formattedBudget)
            viewModel.setBudget(newBudget.toString())
            difference()
        }

        viewModel.getMoneySpent(userId).observe(this.viewLifecycleOwner) { newMoneySpent ->
            viewModel.setMoneySpent(newMoneySpent)
            val formattedMoneySpent = "$symbol$newMoneySpent"
            binding!!.totalAmountSpent.text = getString(R.string.total_amt_spent, formattedMoneySpent)
            difference()
        }

        binding!!.apply {
            recyclerView.adapter = adapter

            editFloatingActionButton.setOnClickListener {
                val dialogFragment = EditBudgetDialogFragment(navigationArgs.id)
                dialogFragment.isCancelable = false
                dialogFragment.show(parentFragmentManager, "My Fragment")
                difference()
            }

            deleteAllFloatingActionButton.setOnClickListener {
                val builder = AlertDialog.Builder(requireContext())
                builder.setTitle(R.string.delete_warning)
                builder.setMessage(R.string.delete_all_warning_message)
                builder.setIcon(R.drawable.warning_icon)
                builder.setPositiveButton(getString(R.string.yes), null)
                builder.setNegativeButton(getString(R.string.no), null)
                val alertDialog: AlertDialog = builder.create()
                alertDialog.setCancelable(false)
                alertDialog.show()

                val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                positiveButton.setOnClickListener {
                    viewModel.deleteAllUserExpenses(userId)
                    Toast.makeText(activity, getString(R.string.delete_all_successful), Toast.LENGTH_SHORT).show()
                    alertDialog.cancel()
                }

                val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                negativeButton.setOnClickListener {
                    alertDialog.cancel()
                }
            }

            addItemFloatingActionButton.setOnClickListener {
                val action = ExpensesListFragmentDirections
                    .actionExpensesListFragmentToAddExpenseFragment(userId, getString(R.string.add_expense), -1)
                findNavController().navigate(action)
                difference()
            }
        }
    }

    private fun difference(){
        try {
            val difference = viewModel.moneySpent.value!!.minus(viewModel.budget.value!!)
            if (difference > 0){
                val formattedDifference = "$symbol$difference"
                binding!!.budgetWarning.text = getString(R.string.warning, formattedDifference)
            }
            else {
                binding!!.budgetWarning.text = ""
            }
        }catch (e: Exception){
            binding!!.budgetWarning.text = ""
            Log.d("Null Value", "Null Pointer Exception")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}