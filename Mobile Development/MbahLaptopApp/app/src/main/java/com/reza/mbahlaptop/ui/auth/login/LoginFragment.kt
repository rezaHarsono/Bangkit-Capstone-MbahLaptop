package com.reza.mbahlaptop.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.exceptions.GetCredentialException
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.google.firebase.Firebase
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.reza.mbahlaptop.R
import com.reza.mbahlaptop.databinding.FragmentLoginBinding
import com.reza.mbahlaptop.ui.main.HandleLoginActivity
import com.reza.mbahlaptop.ui.main.MainActivity
import kotlinx.coroutines.launch


class LoginFragment : Fragment() {
    private lateinit var _binding: FragmentLoginBinding
    private val binding get() = _binding

    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth

        setupAction()
    }

    private fun setupAction() {
        binding.btnGoogle.setOnClickListener {
            signInWithGoogle()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showToast(getString(R.string.please_fill_all_fields))
            } else {
                signInWithEmailAndPassword()
            }
        }

        binding.btnLoginGuest.setOnClickListener {
            val intent = Intent(requireContext(), MainActivity::class.java)
            startActivity(
                intent,
                ActivityOptionsCompat.makeSceneTransitionAnimation(requireActivity()).toBundle()
            )
        }
    }

    private fun signInWithGoogle() {
        val credentialManager = CredentialManager.create(requireContext())

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(getString(R.string.default_web_client_id))
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result: GetCredentialResponse = credentialManager.getCredential(
                    //import from androidx.CredentialManager
                    request = request,
                    context = requireContext(),
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) { //import from androidx.CredentialManager
                Log.d("Error", e.message.toString())
            }
        }
    }

    private fun signInWithEmailAndPassword() {
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()
        showLoading(true)

        auth.signInWithEmailAndPassword(email, password)

            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    showLoading(false)
                    Log.d(TAG, "signInWithEmail: Success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    showLoading(false)
                    Log.w(TAG, "signInWithEmail: Failure", task.exception)
                    showToast(getString(R.string.error_login_invalid))
                    updateUI(null)
                }
            }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        // Handle the successfully returned credential.
        when (val credential = result.credential) {
            is CustomCredential -> {
                if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                    try {
                        val googleIdTokenCredential =
                            GoogleIdTokenCredential.createFrom(credential.data)
                        firebaseAuthWithGoogle(googleIdTokenCredential.idToken)
                    } catch (e: GoogleIdTokenParsingException) {
                        Log.e(TAG, "Received an invalid google id token response", e)
                    }
                } else {
                    // Catch any unrecognized custom credential type here.
                    Log.e(TAG, "Unexpected type of credential")
                }
            }

            else -> {
                // Catch any unrecognized credential type here.
                Log.e(TAG, "Unexpected type of credential")
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        showLoading(true)
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user: FirebaseUser? = auth.currentUser
                    updateUI(user)
                    showLoading(false)
                } else {
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    showToast("Login Failed")
                    updateUI(null)
                    showLoading(false)
                }
            }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
        if (currentUser != null) {
            val loginIntent = Intent(requireContext(), HandleLoginActivity::class.java)
            loginIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(loginIntent)
            requireActivity().finish()
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

    companion object {
        private const val TAG = "LoginFragment"
    }
}