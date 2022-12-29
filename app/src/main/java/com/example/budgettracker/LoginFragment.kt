package com.example.budgettracker

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.budgettracker.databinding.FragmentLoginBinding
import com.example.budgettracker.model.LoginViewModel
import com.example.budgettracker.model.LoginViewModelFactory


class LoginFragment : Fragment() {
    private var binding: FragmentLoginBinding? = null
    private val viewModel: LoginViewModel by viewModels{
        LoginViewModelFactory(
            (activity?.application as BudgetApplication).database.usersDao()
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val fragmentBinding = FragmentLoginBinding.inflate(inflater, container, false)
        binding = fragmentBinding
        return fragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding?.apply {
            lifecycleOwner = viewLifecycleOwner
            loginFragment = this@LoginFragment
        }
    }

    fun goToSignupScreen(){
        findNavController().navigate(R.id.action_loginFragment_to_signupFragment)
    }

    fun goToForgotPasswordScreen(){
        findNavController().navigate(R.id.action_loginFragment_to_forgotPasswordFragment)
    }

    fun goToExpensesListFragment(){
        val username = binding!!.username.text.toString()
        val password = binding!!.password.text.toString()
        viewModel.checkUserExists(username, password).observe(this.viewLifecycleOwner){ newChkExist ->
            if (newChkExist!!){
                Toast.makeText(activity, getString(R.string.login_successful), Toast.LENGTH_SHORT).show()
                viewModel.getUserId(username).observe(this.viewLifecycleOwner){ newUserId ->
                    val action = LoginFragmentDirections.actionLoginFragmentToExpensesListFragment(newUserId)
                    findNavController().navigate(action)
                }
            }
            else{
                Toast.makeText(activity, getString(R.string.incorrect_credentials), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}