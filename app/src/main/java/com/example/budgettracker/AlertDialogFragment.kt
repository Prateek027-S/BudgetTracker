package com.example.budgettracker

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.model.SignUpViewModel
import com.example.budgettracker.model.SignUpViewModelFactory


class AlertDialogFragment: DialogFragment() {
    private val viewModel: SignUpViewModel by activityViewModels{
        SignUpViewModelFactory(
            (activity?.application as BudgetApplication).database.usersDao(),
            (activity?.application as BudgetApplication).database.userBudgetDao()
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(R.string.recovery_code)
        viewModel.setRecoveryCode()
        builder.setMessage(getString(R.string.recovery_code_dialog_msg, viewModel.recoveryCode.value.toString()))
        builder.setCancelable(false)
        builder.setPositiveButton(R.string.ok) { _, _ ->
            viewModel.addNewUser()
            findNavController().navigate(R.id.action_signupFragment_to_loginFragment)
            viewModel.retain = false
            viewModel.newAccount = true
        }
        return builder.create()
    }
}