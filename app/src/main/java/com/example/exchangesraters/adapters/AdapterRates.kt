package com.example.exchangesraters.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.exchangesraters.R
import com.example.exchangesraters.data.model.RatesModel
import com.example.exchangesraters.data.model.RecordList

class AdapterRates:RecyclerView.Adapter<AdapterRates.AdapterViewHolder>() {
    var listRatesModel = emptyList<RecordList>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AdapterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rates_element,parent, false)

        return AdapterViewHolder(view)
    }

    override fun onBindViewHolder(holder: AdapterViewHolder, position: Int) {
        holder.textView.text = listRatesModel.get(position).value
        holder.textViewTimePerson.text = listRatesModel.get(position).date
    }

    override fun getItemCount(): Int {
        return listRatesModel.size
    }


    class AdapterViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textView: TextView = itemView.findViewById(R.id.tx)
        val textViewTimePerson: TextView = itemView.findViewById(R.id.txTime)
    }

    fun update(list:List<RecordList>){
        listRatesModel=list

        notifyDataSetChanged()
    }

}



