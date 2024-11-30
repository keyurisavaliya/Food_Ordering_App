package com.examples.food_ordering_app.Fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.examples.food_ordering_app.LoginActivity
import com.examples.food_ordering_app.R
import com.examples.food_ordering_app.databinding.FragmentProfileBinding
import com.examples.food_ordering_app.model.UserModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileFragment : Fragment() {

    private lateinit var binding : FragmentProfileBinding
    private lateinit var auth : FirebaseAuth

    private val auth1 = FirebaseAuth.getInstance()
    private val database = FirebaseDatabase.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        auth = FirebaseAuth.getInstance()

        binding.logout.setOnClickListener {
            auth.signOut()
            startActivity(
                Intent(
                    requireContext(),
                    LoginActivity::class.java
                )
            )
            activity?.finish()
        }

        setUserData()

        binding.saveInfoButton.setOnClickListener {
            val name = binding.name.text.toString()
            val email = binding.email.text.toString()
            val address = binding.address.text.toString()
            val phone = binding.phone.text.toString()
            updateUserData(name,email,address,phone)
        }

        return binding.root
    }

    private fun updateUserData(name: String, email: String, address: String, phone: String) {
        val userId = auth1.currentUser?.uid
        if(userId!=null){
            val userReference = database.getReference("user").child(userId)
            val userData = hashMapOf(
                "name" to name,
                "address" to address,
                "email" to email,
                "phone" to phone
            )
            userReference.setValue(userData).addOnSuccessListener {
                Toast.makeText(requireContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener{
                Toast.makeText(requireContext(), "Profile Updation Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun setUserData() {

        val userId = auth1.currentUser?.uid
        if(userId!=null){
            val userReference = database.getReference("user").child(userId)

            userReference.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {

                    if(snapshot.exists()){
                        val userProfile = snapshot.getValue(UserModel::class.java)
                        if(userProfile!=null){
                            binding.name.setText(userProfile.name)
                            binding.address.setText(userProfile.address)
                            binding.email.setText(userProfile.email)
                            binding.phone.setText(userProfile.phone)
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        }
    }
}