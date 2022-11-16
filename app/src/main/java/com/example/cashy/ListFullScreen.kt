package com.example.cashy

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.util.*


class ListFullScreen : AppCompatActivity() {

    lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth

    lateinit var recyclerView : RecyclerView
    lateinit var adapter : FullScreenAdapter
    lateinit var receipts : MutableList<Receipt>

    val c = Calendar.getInstance()
    var currentMonth = (c.get(Calendar.MONTH) + 1).toString()
    var currentYear = c.get(Calendar.YEAR).toString()

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_full_screen)


        db = Firebase.firestore
        auth = Firebase.auth

        //finding the current user and assigning a variable to the user id
        val user = Firebase.auth.currentUser
        var uid = user?.uid

        recyclerView = findViewById(R.id.recyclerViewFullScreen)
        receipts = mutableListOf()
        adapter = FullScreenAdapter(receipts)
        recyclerView.adapter = adapter
        recyclerView.apply {
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(this.context, DividerItemDecoration.VERTICAL))
        }

        readFrom()

        adapter.notifyDataSetChanged()


        val goBack = findViewById<FloatingActionButton>(R.id.backButton)
        goBack.setOnClickListener {
            val intent = Intent(this, Overview::class.java)
            startActivity(intent)
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem : MenuItem = menu.findItem(R.id.actionSearch)
        val searchView : SearchView = searchItem.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }
            override fun onQueryTextChange(msg: String): Boolean {
                filter(msg)
                return false
            }
        })
        return true
    }
    fun readFrom() {
        val user = auth.currentUser
        if (user != null) {
            db.collection("users").document(user.uid).collection("receipts")
                //.whereEqualTo("monthNo", currentMonth)
                //.whereEqualTo("year", currentYear)
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    recyclerView = findViewById(R.id.recyclerViewFullScreen)
                    adapter = FullScreenAdapter(receipts)
                    recyclerView.adapter = adapter
                    for (document in documentSnapshot.documents) {
                        val item = document.toObject<Receipt>()
                        if (item != null) {
                            receipts.add(item)
                        }
                    }
                }
        }
    }
    fun filter(text: String) {
        val filteredList : MutableList<Receipt> = mutableListOf()
        for (item in receipts) {
            if (item.company!!.contains(text, true) || item.notis!!.contains(text, true)){
                filteredList.add(item)
            }
        }
        if (filteredList.isEmpty()) {
            //Toast.makeText(this, "No data found...", Toast.LENGTH_SHORT).show()
        } else {
            adapter.filterList(filteredList)
        }
    }
}
