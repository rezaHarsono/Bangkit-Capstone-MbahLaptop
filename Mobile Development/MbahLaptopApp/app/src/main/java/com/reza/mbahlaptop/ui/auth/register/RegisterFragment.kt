package com.reza.mbahlaptop.ui.auth.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.FragmentRegisterBinding
import com.reza.mbahlaptop.ui.main.HandleLoginActivity

class RegisterFragment : Fragment() {
    private lateinit var _binding: FragmentRegisterBinding
    private val binding get() = _binding

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        db = FirebaseFirestore.getInstance()

        setupAction()
    }

    private fun setupAction() {
        binding.apply {
            edEmail.addTextChangedListener {
                val email = edEmail.text.toString()
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    tilEmail.isErrorEnabled = true
                    tilEmail.error = resources.getString(R.string.error_email_invalid)
                } else {
                    tilEmail.error = null
                    tilEmail.isErrorEnabled = false
                }
            }

            edPassword.addTextChangedListener {
                val password = edPassword.text.toString()
                if (password.length < 8) {
                    tilPassword.isErrorEnabled = true
                    tilPassword.error = resources.getString(R.string.error_password_invalid)
                } else {
                    tilPassword.error = null
                    tilPassword.isErrorEnabled = false
                }
            }

            edPasswordValid.addTextChangedListener {
                val password = edPassword.text.toString()
                val passwordValid = binding.edPasswordValid.text.toString()
                if (passwordValid != password) {
                    tilPasswordValid.isErrorEnabled = true
                    tilPasswordValid.error = getString(R.string.passwords_don_t_match)
                } else {
                    tilPasswordValid.error = null
                    tilPasswordValid.isErrorEnabled = false
                }
            }

            btnRegister.setOnClickListener {
                val username = edUsername.text.toString().trim()
                val email = edEmail.text.toString().trim()
                val password = edPassword.text.toString()
                val passwordValid = edPasswordValid.text.toString()
                if (email.isEmpty() || password.isEmpty() || username.isEmpty() || passwordValid.isEmpty()) {
                    showToast(getString(R.string.please_fill_all_fields))
                } else {
                    register()
                }
            }
        }
    }

    private fun register() {
        val username = binding.edUsername.text.toString()
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()
        showLoading(true)

        val firesStoreUser = hashMapOf(
            "username" to username,
            "email" to email,
            "password" to password
        )

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    showLoading(false)
                    Log.d(TAG, "Create user: Success")
                    val user = auth.currentUser
                    db.collection("users")
                        .add(firesStoreUser)
                        .addOnSuccessListener {
                            Log.d(TAG, "Create user: Success")
                        }
                        .addOnFailureListener {
                            Log.w(TAG, "Create user: Failure", it)
                        }
                    updateUI(user)
                } else {
                    showLoading(false)
                    Log.w(TAG, "Create user: Failure", task.exception)
                    if (task.exception?.message == "The email address is already in use by another account.") {
                        showToast(getString(R.string.email_registered_error))
                    } else {
                        showToast(task.exception?.message!!)
                    }
                    updateUI(null)
                }
            }

    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun showLoading(active: Boolean) {
        if (active) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val loginIntent = Intent(requireContext(), HandleLoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(
                loginIntent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
            requireActivity().finish()
        }
    }

    companion object {
        private const val TAG = "RegisterFragment"
    }
}