package com.example.budgettracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.budgettracker.databinding.FragmentSignupBinding
import com.example.budgettracker.model.SignUpViewModel
import com.example.budgettracker.model.SignUpViewModelFactory


class SignupFragment : Fragment() {
    private val viewModel: SignUpViewModel by activityViewModels{
        SignUpViewModelFactory(
            (activity?.application as BudgetApplication).database.usersDao(),
            (activity?.application as BudgetApplication).database.userBudgetDao()
        )
    }

    private var binding: FragmentSignupBinding? = null
    private lateinit var username: TextView
    private lateinit var password: EditText
    private lateinit var confirmPassword: EditText
    private var chkExist: Boolean? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val fragmentBinding = FragmentSignupBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            signupFragment = this@SignupFragment
        }
        username = binding!!.username
        password = binding!!.password
        confirmPassword = binding!!.confirmPassword

        binding!!.apply {
            passwordTextLayout.visibility = View.INVISIBLE
            confirmPasswordTextLayout.visibility = View.INVISIBLE
            createAccountBtn.visibility = View.INVISIBLE
        }
    }

    override fun onPause() {
        super.onPause()
        viewModel.retain = true
    }

    override fun onResume() {
        super.onResume()
        viewModel.retain = false
    }

    fun showRecoveryCode(){
        if (areEntriesValid() == 1){
            Toast.makeText(activity, getString(R.string.password_not_empty), Toast.LENGTH_SHORT).show()
        }
        else if(areEntriesValid() == 2){
            Toast.makeText(activity, getString(R.string.password_confirm_not_same), Toast.LENGTH_SHORT).show()
        }
        else if(areEntriesValid() == 3){
            viewModel.setUsername(username.text.toString())
            viewModel.setPassword(password.text.toString())
            Toast.makeText(activity, getString(R.string.signup_successful), Toast.LENGTH_LONG).show()
            val dialogFragment = AlertDialogFragment()
            dialogFragment.isCancelable = false
            dialogFragment.show(parentFragmentManager, "My Fragment")
        }
    }

    fun checkUsernameAvailability(){
        val userName = binding!!.username.text.toString()
        viewModel.checkUsernameExists(userName).observe(this.viewLifecycleOwner){ newChkExist ->
            chkExist = newChkExist
            if(userName.isBlank()){
                Toast.makeText(activity, R.string.username_not_empty, Toast.LENGTH_SHORT).show()
            }
            else if (chkExist!!){
                Toast.makeText(activity, getString(R.string.username_taken), Toast.LENGTH_SHORT).show()
            }
            else{
                binding!!.apply {
                    checkAvailabilityBtn.visibility = View.INVISIBLE
                    passwordTextLayout.visibility = View.VISIBLE
                    confirmPasswordTextLayout.visibility = View.VISIBLE
                    createAccountBtn.visibility = View.VISIBLE
                    username.isFocusable = false
                }
            }
        }
    }

    private fun areEntriesValid(): Int{
        return viewModel.areEntriesValid(
            password.text.toString(),
            confirmPassword.text.toString()
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}