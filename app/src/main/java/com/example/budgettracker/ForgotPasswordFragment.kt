package com.example.budgettracker

import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.databinding.FragmentForgotPasswordBinding
import com.example.budgettracker.model.ForgotPasswordViewModel
import com.example.budgettracker.model.ForgotPasswordViewModelFactory

/**
 * Fragment for Forgot Password
 */
class ForgotPasswordFragment : Fragment() {

    private var binding: FragmentForgotPasswordBinding? = null
    private val viewModel: ForgotPasswordViewModel by viewModels{
        ForgotPasswordViewModelFactory(
            (activity?.application as BudgetApplication).database.usersDao()
        )
    }
    private lateinit var username: EditText
    private lateinit var code: EditText
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private var chkExist: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentForgotPasswordBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            fragment = this@ForgotPasswordFragment
        }
        username = binding!!.username
        code = binding!!.recoveryCode
        password = binding!!.newPassword
        confirmPassword = binding!!.confirmPassword

        binding!!.apply {
            newPasswordTextLayout.visibility = View.INVISIBLE
            confirmPasswordTextLayout.visibility = View.INVISIBLE
            resetBtn.visibility = View.INVISIBLE
        }
    }

    fun onValidateButtonClick(){
        viewModel.setUsername(username.text.toString())
        viewModel.setRecoveryCode(code.text.toString())
        viewModel.validate().observe(this.viewLifecycleOwner){ newChkExist ->
            chkExist = newChkExist
            if (!TextUtils.isEmpty(code.text.toString()) && chkExist!!){
                binding!!.apply {
                    validateBtn.visibility = View.INVISIBLE
                    newPasswordTextLayout.visibility = View.VISIBLE
                    confirmPasswordTextLayout.visibility = View.VISIBLE
                    resetBtn.visibility = View.VISIBLE
                    username.isFocusable = false
                    recoveryCode.isFocusable = false
                }
            }
            else {
                Toast.makeText(activity, getString(R.string.incorrect_username_code), Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun onResetButtonClick(){
        if (TextUtils.isEmpty(password.text.toString())){
            Toast.makeText(activity, getString(R.string.password_not_empty), Toast.LENGTH_LONG).show()
        }
        else if(password.text.toString() != confirmPassword.text.toString()){
            Toast.makeText(activity, getString(R.string.password_confirm_not_same), Toast.LENGTH_LONG).show()
        }
        else if (password.text.toString() == confirmPassword.text.toString()){
            viewModel.setPassword(password.text.toString())
            Toast.makeText(activity, getString(R.string.reset_successful), Toast.LENGTH_LONG).show()
            viewModel.resetPassword()
            findNavController().navigate(R.id.action_forgotPasswordFragment_to_loginFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}