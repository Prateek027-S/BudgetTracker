package com.example.budgettracker

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.example.budgettracker.model.ExpensesViewModel
import com.example.budgettracker.model.ExpensesViewModelFactory
import java.util.*

class EditBudgetDialogFragment(val userId: Int): DialogFragment() {

    private val viewModel: ExpensesViewModel by activityViewModels {
        ExpensesViewModelFactory(
            (activity?.application as BudgetApplication).database.userBudgetDao(),
            (activity?.application as BudgetApplication).database.expensesDao(),
            (activity?.application as BudgetApplication).database.userExpensesDao()
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val myView = layoutInflater.inflate(R.layout.edit_budget_dialog, null)
        builder.setCancelable(false)
        builder.setTitle(getString(R.string.edit_budget))
        val currencyText = myView.findViewById(R.id.currency2) as TextView
        val editedBudget = myView.findViewById(R.id.edited_budget) as EditText
        val okBtn = myView.findViewById(R.id.ok_btn) as Button
        currencyText.text = Currency.getInstance(Locale.getDefault()).symbol
        builder.setView(myView)
        okBtn.setOnClickListener {
            if (TextUtils.isEmpty(editedBudget.text.toString()))
                Toast.makeText(activity, getString(R.string.enter_budget), Toast.LENGTH_SHORT).show()
            else{
                viewModel.setBudget(editedBudget.text.toString())
                viewModel.updateBudget(userId)
                Toast.makeText(activity, getString(R.string.budget_edit_successful), Toast.LENGTH_SHORT).show()
                dismiss()
            }
        }
        return builder.create()
    }
}