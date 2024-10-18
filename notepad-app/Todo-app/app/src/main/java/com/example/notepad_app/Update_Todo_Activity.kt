package com.example.notepad_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad_app.databinding.ActivityUpdateTodoBinding

class Update_Todo_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateTodoBinding
    private  lateinit var db:TODODatabaseHelper
    private var todoId: Int = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)

            db = TODODatabaseHelper(this)
        todoId = intent.getIntExtra("todo_id", -1)
        if (todoId == -1) {
            finish()
            return
        }
        val todo = db.getTodoById(todoId)
        binding.editTaskInput.setText(todo.title)
        binding.editTaskDiscription.setText(todo.content)

        val dateParts = todo.date.split("/")
        val day = dateParts[0].toInt()
        val month = dateParts[1].toInt() - 1
        val year = dateParts[2].toInt()


        binding.editTaskDate.updateDate(year, month, day)
        val timeParts = todo.time.split(":")
        val hour = timeParts[0].toInt()
        val minute = timeParts[1].toInt()


        binding.editTaskTime.hour = hour
        binding.editTaskTime.minute = minute

        binding.editBtn.setOnClickListener {
            val title = binding.editTaskInput.text.toString()
            val content = binding.editTaskDiscription.text.toString()
            val date = "${binding.editTaskDate.dayOfMonth}/${binding.editTaskDate.month + 1}/${binding.editTaskDate.year}"
            val time = "${binding.editTaskTime.hour}:${binding.editTaskTime.minute}"
            val todo = Todo(
                todoId,
                title,
                content,
                date,
                time,
                0
            )
            db.updateTodo(todo)
            finish()
            Toast.makeText(this, "Todo updated", Toast.LENGTH_SHORT).show()

        }
        binding.editBackButton.setOnClickListener {
            navigateToMain()
        }

    }
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}