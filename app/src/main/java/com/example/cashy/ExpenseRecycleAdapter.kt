package com.example.cashy

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseRecycleAdapter(val context : Context, val expenses : List<Expense>) :
    RecyclerView.Adapter<ExpenseRecycleAdapter.ViewHolder>() {

    val layoutInflater = LayoutInflater.from(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = layoutInflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val expense = expenses[position]
        holder.expense_txtView.text = expense.amount.toString()
        holder.category_txtView.text = expense.category
        holder.paymentMethod_txtView.text = expense.paymentMethod
    }

    override fun getItemCount(): Int {
        return expenses.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var expense_txtView = itemView.findViewById<TextView>(R.id.expense_txt)
        var category_txtView = itemView.findViewById<TextView>(R.id.category_txt)
        var paymentMethod_txtView = itemView.findViewById<TextView>(R.id.paymentMethod_txt)
    }
}