package com.example.notepad_app

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView


class TODOAdapter(private var todos: List<Todo>, context: Context): RecyclerView.Adapter<TODOAdapter.TodoViewHolder>() {

    private val db: TODODatabaseHelper = TODODatabaseHelper(context)

    class TodoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val todoText: TextView = itemView.findViewById<TextView>(R.id.todo_title)
        val desc: TextView = itemView.findViewById<TextView>(R.id.todo_content)
        val date: TextView = itemView.findViewById<TextView>(R.id.todo_date)
        val time: TextView = itemView.findViewById<TextView>(R.id.todo_time)
        val updatebtn: ImageView = itemView.findViewById<ImageView>(R.id.edit_todo)
        val deletebtn: ImageView = itemView.findViewById<ImageView>(R.id.delete_todo)
        val checkbox: CheckBox = itemView.findViewById(R.id.todo_checkbox)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.todo_items, parent, false)
        return TodoViewHolder(view)
    }

    override fun getItemCount(): Int {

        return todos.size
    }

    override fun onBindViewHolder(holder: TodoViewHolder, position: Int) {
        val currentTodo = todos[position]
        holder.todoText.text = currentTodo.title
        holder.desc.text = currentTodo.content
        holder.date.text = currentTodo.date
        holder.time.text = currentTodo.time
        holder.checkbox.isChecked = currentTodo.status == 1

        holder.updatebtn.setOnClickListener {
            val intent = Intent(holder.itemView.context, Update_Todo_Activity::class.java).apply {
                putExtra("todo_id", currentTodo.id)
            }
            holder.itemView.context.startActivity(intent)
        }

        holder.deletebtn.setOnClickListener {
            db.deleteTodoById(currentTodo.id)
            refresh(db.getAllTodo())
            Toast.makeText(holder.itemView.context, "Todo Deleted", Toast.LENGTH_SHORT).show()
        }
        holder.checkbox.isChecked = currentTodo.status == 1

        holder.checkbox.setOnCheckedChangeListener { _, isChecked ->
            currentTodo.status = if (isChecked) 1 else 0
            db.updateTodoStatus(currentTodo.id, currentTodo.status)
        }
    }


        fun refresh(todo: List<Todo>) {
            todos = todo
            notifyDataSetChanged()
        }


    }

