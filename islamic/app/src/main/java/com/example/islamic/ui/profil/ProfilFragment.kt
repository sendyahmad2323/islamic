// File: ProfilFragment.kt
package com.example.islamic.ui.profil

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.example.islamic.ChangePasswordActivity
import com.example.islamic.LoginActivity
import com.example.islamic.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ProfilFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var profileImage: ImageView
    private lateinit var profileName: TextView
    private lateinit var profileEmail: TextView
    private lateinit var editProfileItem: LinearLayout
    private lateinit var changePasswordItem: LinearLayout
    private lateinit var logoutItem: LinearLayout
    private lateinit var bottomNavigation: BottomNavigationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profil, container, false)

        auth = FirebaseAuth.getInstance()

        // Inisialisasi view dari layout
        profileImage = view.findViewById(R.id.profileImage)
        profileName = view.findViewById(R.id.profileName)
        profileEmail = view.findViewById(R.id.profileEmail)
        editProfileItem = view.findViewById(R.id.editProfileItem)
        changePasswordItem = view.findViewById(R.id.changePasswordItem)
        logoutItem = view.findViewById(R.id.logoutItem)
        bottomNavigation = view.findViewById(R.id.bottomNavigation)

        // Listener tombol
        setupClickListeners()

        // Bottom navigation
        setupBottomNavigation()

        return view
    }

    override fun onResume() {
        super.onResume()
        // Set ulang data user setiap kali fragment tampil
        setUserData(auth.currentUser)
    }

    private fun setUserData(user: FirebaseUser?) {
        profileName.text = user?.displayName ?: "Nama Belum Diatur"
        profileEmail.text = user?.email ?: "Email Belum Diatur"
        // Jika user memiliki photoUrl, kamu bisa load ke profileImage, misal:
        // user.photoUrl?.let { uri ->
        //     Glide.with(this).load(uri).into(profileImage)
        // }
    }

    private fun setupClickListeners() {
        editProfileItem.setOnClickListener {
            val intent = Intent(requireContext(), EditProfileActivity::class.java)
            startActivity(intent)
        }

        changePasswordItem.setOnClickListener {
            val intent = Intent(requireContext(), ChangePasswordActivity::class.java)
            startActivity(intent)
        }

        logoutItem.setOnClickListener {
            auth.signOut()
            Toast.makeText(requireContext(), "Logout berhasil", Toast.LENGTH_SHORT).show()
            startActivity(Intent(requireContext(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.selectedItemId = R.id.navigation_profil

        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    // Contoh: pindah ke fragment Home jika memakai Navigation Component
                    // atau tampilkan Toast
                    Toast.makeText(requireContext(), "Home clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_notifications -> {
                    Toast.makeText(requireContext(), "Notifications clicked", Toast.LENGTH_SHORT).show()
                    true
                }
                R.id.navigation_profil -> true
                else -> false
            }
        }
    }
}
