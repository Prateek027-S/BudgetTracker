package com.example.budgettracker

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.budgettracker.databinding.FragmentAddExpenseBinding
import com.example.budgettracker.model.ExpensesViewModel
import com.example.budgettracker.model.ExpensesViewModelFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * Fragment for Adding/Editing an Item
 * mode = 0 for Adding; mode = 1  for Editing
 */
class AddExpenseFragment : Fragment() {

    private val navigationArgs: AddExpenseFragmentArgs by navArgs()
    private var binding: FragmentAddExpenseBinding? = null
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
        val fragmentBinding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userId = navigationArgs.userId
        val userExpenseId = navigationArgs.userExpenseId
        val currency = Currency.getInstance(Locale.getDefault())
        val symbol = currency.symbol
        var oldPrice = 0.0
        binding!!.currency.text = symbol

        if (userExpenseId >= 0){
            binding!!.apply {
                viewModel.retrieveUserExpenses(userExpenseId).observe(this@AddExpenseFragment.viewLifecycleOwner){ expenseAndUserExpense ->
                    binding!!.apply {
                        itemName.setText(expenseAndUserExpense.itemName)
                        oldPrice = expenseAndUserExpense.cost
                        price.setText(oldPrice.toString())
                    }
                }
            }
        }

        binding!!.saveBtn.setOnClickListener {
            val itemName = binding!!.itemName.text.toString()
            val price = binding!!.price.text.toString()

            if(TextUtils.isEmpty(itemName) || TextUtils.isEmpty(price)){
                Toast.makeText(activity, getString(R.string.enter_all_details), Toast.LENGTH_SHORT).show()
            }
            else{
                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
                val formatted = current.format(formatter).toString()
                viewModel.addEditItem(userId, userExpenseId, itemName, oldPrice, price.toDouble(), formatted)
                if(userExpenseId >= 0)
                    Toast.makeText(activity, getString(R.string.edit_details_successful), Toast.LENGTH_SHORT).show()
                else
                    Toast.makeText(activity, getString(R.string.expense_add_successful), Toast.LENGTH_SHORT).show()
                val action = AddExpenseFragmentDirections.actionAddExpenseFragmentToExpensesListFragment(userId)
                findNavController().navigate(action)
            }
        }
    }
   override fun onDestroyView() {
       super.onDestroyView()
       binding = null
   }
}