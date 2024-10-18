package com.example.notepad_app

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.notepad_app.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var db: TODODatabaseHelper
    private lateinit var adapter: TODOAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = TODODatabaseHelper(this)
        adapter = TODOAdapter(db.getAllTodo(), this)
        binding.todoRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.todoRecyclerView.adapter = adapter


        binding.Addbtn.setOnClickListener {
            val intent = Intent(this, AddTodo::class.java)
            startActivity(intent)
        }


    }

    override fun onResume() {
        super.onResume()
        adapter.refresh(db.getAllTodo())

    }



    }
