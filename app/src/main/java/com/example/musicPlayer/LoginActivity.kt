package com.example.musicPlayer

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.musicPlayer.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        // Luôn hiển thị màn hình đăng nhập khi mở app

        // Gạch chân chữ "Đăng ký"
        binding.txtSignup.paintFlags = binding.txtSignup.paintFlags or Paint.UNDERLINE_TEXT_FLAG

        // Nút đăng nhập
        binding.btnLogin.setOnClickListener {
            val email = binding.edtEmail.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show()
            } else {
                binding.btnLogin.isEnabled = false
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        binding.btnLogin.isEnabled = true
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Chào mừng bạn đến với ứng dụng!", Toast.LENGTH_SHORT).show()
                            // Ghi/ cập nhật thông tin người dùng lên Firestore (không ghi đè username)
                            auth.currentUser?.let { user ->
                                val uid = user.uid
                                val email = user.email
                                val doc = mapOf(
                                    "uid" to uid,
                                    "email" to email,
                                    "lastLoginAt" to FieldValue.serverTimestamp()
                                )
                                firestore.collection("users").document(uid)
                                    .set(doc, SetOptions.merge())
                            }
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(
                                this,
                                task.exception?.localizedMessage ?: "Đăng nhập thất bại!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }

        // Chuyển sang màn hình đăng ký
        binding.txtSignup.setOnClickListener {
            startActivity(Intent(this, SignupActivity::class.java))
        }
    }
}
