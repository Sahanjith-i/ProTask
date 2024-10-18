package com.example.notepad_app
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.notepad_app.databinding.ActivityAddTodoBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AddTodo : AppCompatActivity() {
    private lateinit var binding: ActivityAddTodoBinding
    private lateinit var bd: TODODatabaseHelper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddTodoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        bd = TODODatabaseHelper(this)
        binding.saveBtn.setOnClickListener {
            val title = binding.taskInput.text.toString()
            val content = binding.taskDiscription.text.toString()
            val date = binding.taskDate.dayOfMonth.toString() + "/" + binding.taskDate.month.toString() + "/" + binding.taskDate.year.toString()
            val time = binding.taskTime.hour.toString() + ":" + binding.taskTime.minute.toString()
            val todo = Todo (0, title, content, date, time,0)
            GlobalScope.launch(Dispatchers.Main) {
                val success = insertTodoAsync(todo)
                if (success) {
                    Toast.makeText(this@AddTodo, "Todo Added", Toast.LENGTH_SHORT).show()
                    navigateToMain()
                } else {
                    Toast.makeText(this@AddTodo, "Failed to add Todo", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.backButton.setOnClickListener {
            navigateToMain()
        }
    }



private suspend fun insertTodoAsync(todo: Todo): Boolean {
    return withContext(Dispatchers.IO) {
        bd.insertTodoAsync(todo)
    }
}
   private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Finish the current activity
    }

}