package com.example.musicPlayer

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicPlayer.databinding.ActivitySignupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class SignupActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignupBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Gạch chân chữ "Đăng nhập"
        binding.txtLogin.paintFlags = binding.txtLogin.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // Xử lý nút Đăng ký
        binding.btnSignup.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()
            val confirmPassword = binding.edtConfirmPassword.text.toString().trim()

            when {
                username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() ->
                    Toast.makeText(this, "Vui lòng điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show()

                password != confirmPassword ->
                    Toast.makeText(this, "Mật khẩu không khớp!", Toast.LENGTH_SHORT).show()

                else -> {
                    binding.btnSignup.isEnabled = false
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener { createTask ->
                            if (!createTask.isSuccessful) {
                                binding.btnSignup.isEnabled = true
                                Toast.makeText(
                                    this,
                                    createTask.exception?.localizedMessage ?: "Đăng ký thất bại!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                return@addOnCompleteListener
                            }

                            val uid = auth.currentUser?.uid ?: return@addOnCompleteListener
                            val userDoc = mapOf(
                                "uid" to uid,
                                "username" to username,
                                "email" to email,
                                "createdAt" to System.currentTimeMillis()
                            )

                            firestore.collection("users").document(uid)
                                .set(userDoc)
                                .addOnCompleteListener { saveTask ->
                                    binding.btnSignup.isEnabled = true
                                    if (saveTask.isSuccessful) {
                                        // createUser đã đăng nhập sẵn -> vào thẳng màn hình chính
                                        startActivity(Intent(this, MainActivity::class.java))
                                        finish()
                                    } else {
                                        Toast.makeText(
                                            this,
                                            saveTask.exception?.localizedMessage
                                                ?: "Lưu thông tin người dùng thất bại!",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                }
                        }
                }
            }
        }

        // Quay lại màn hình đăng nhập
        binding.txtLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }
    }
}
