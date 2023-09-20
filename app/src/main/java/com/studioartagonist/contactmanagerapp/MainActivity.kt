package com.studioartagonist.contactmanagerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.studioartagonist.contactmanagerapp.viewModel.UserViewModel
import com.studioartagonist.contactmanagerapp.viewModel.UserViewModelFactory
import com.studioartagonist.contactmanagerapp.databinding.ActivityMainBinding
import com.studioartagonist.contactmanagerapp.room.User
import com.studioartagonist.contactmanagerapp.room.UserDatabase
import com.studioartagonist.contactmanagerapp.room.UserRepository
import com.studioartagonist.contactmanagerapp.viewUI.MyRecyclerViewAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var userViewModel: UserViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        // Room database

        val dao = UserDatabase.getInstance(application).userDAO
        val repository = UserRepository(dao)
        val factory = UserViewModelFactory(repository)



        userViewModel = ViewModelProvider(this,
            factory).get(UserViewModel::class.java)


        binding.userViewModel = userViewModel


        binding.lifecycleOwner = this

        initRecyclerView()








    }

    private fun initRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        DisplayUsersList()
    }

    private fun DisplayUsersList() {
        userViewModel.users.observe(this, Observer {
            binding.recyclerView.adapter = MyRecyclerViewAdapter(
                it, {selectedItem:User-> listItemClicked(selectedItem)}
            )
        })
    }

    private fun listItemClicked(selectedItem: User) {

        Toast.makeText(this,
        "Selected name is ${selectedItem.name}",
        Toast.LENGTH_LONG).show()

        userViewModel.initUpdateAndDelete(selectedItem)


    }
}