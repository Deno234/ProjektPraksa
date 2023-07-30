package com.example.aplikacijazaprognozuvremena

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.findFragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView

class SearchAdapter(private var data: List<City>, private val searchFragment: SearchFragment, private val navController: NavController) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {
    var onCitySelectedListener: SearchFragment.onCitySelectedListener? = null
    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.city_name_text_view)

        init {
            itemView.setOnClickListener {
                val city = data[adapterPosition]
                val sharedViewModel = ViewModelProvider(searchFragment.requireActivity()).get(SharedViewModel::class.java)
                sharedViewModel.setSelectedCity(city)
                navController.popBackStack()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.city_item, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = data[position]
        holder.textView.text = item.name
    }

    override fun getItemCount(): Int {
        return data.size
    }

    fun submitList(newData: List<City>) {
        data = newData
        notifyDataSetChanged()
    }
}
