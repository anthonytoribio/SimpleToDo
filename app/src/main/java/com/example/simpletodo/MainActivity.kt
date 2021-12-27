package com.example.simpletodo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.apache.commons.io.FileUtils
import java.io.File
import java.io.IOException
import java.nio.charset.Charset

class MainActivity : AppCompatActivity() {

    var listOfTasks = mutableListOf<String>()
    lateinit var adapter : TaskItemAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onLongClickListener  = object : TaskItemAdapter.OnLongClickListener{
            override fun onItemLongClick(position: Int) {
                //remove item from the list
                listOfTasks.removeAt(position)
                //notify the adapater that an item has been deleted
                adapter.notifyDataSetChanged()

                saveItems()
            }
        }

        loadItems()


        //Loop up the recyclerView in the layout
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        // Create adapter passing in the sample user data
        adapter = TaskItemAdapter(listOfTasks, onLongClickListener)
        // Attach the adapter to the recyclerview to populate items
        recyclerView.adapter = adapter
        // Set layout manager to position the items
        recyclerView.layoutManager = LinearLayoutManager(this)

        val InputField = findViewById<TextView>(R.id.addTaskField)
        //Getting a reference to the button
        //setting the onClick
        findViewById<Button>(R.id.button).setOnClickListener{
            //Grab the text the user has inputted in the TextView
            val userInputtedTask = InputField.text.toString()


            //Add the String to our listOfTasks
            listOfTasks.add(userInputtedTask)

            //Notify the Data Adapter to Item has been inserted
            adapter.notifyItemInserted(listOfTasks.size - 1)

            //Reset the input field
            InputField.setText("")

            saveItems()
        }

    }

    //Save the data the user has inputted
    //By writing and reading from a file

    fun getDataFile() : File {
        //every line is a specific task
        return File(filesDir, "data.txt")
    }

    //Create a method to get the file we need

    //Load the items by reading every line in the data file

    fun loadItems()
    {
        try
        {
            listOfTasks = FileUtils.readLines(getDataFile(), Charset.defaultCharset())
        }
        catch (ioException: IOException)
        {
            ioException.printStackTrace()
        }

    }

    //Save items by writing them in our data file

    fun saveItems()
    {
        try {
            FileUtils.writeLines(getDataFile(), listOfTasks)
        }
        catch (ioException : IOException)
        {
            ioException.printStackTrace()
        }
    }
}