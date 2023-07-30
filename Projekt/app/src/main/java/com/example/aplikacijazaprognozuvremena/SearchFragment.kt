package com.example.aplikacijazaprognozuvremena

import android.os.Bundle
import android.os.Parcelable
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.parcelize.Parcelize

@Parcelize
data class City(val name: String, val country: String) : Parcelable

class SearchFragment() : Fragment() {
    private lateinit var searchEditText: EditText
    private lateinit var citiesRecyclerView: RecyclerView
    private val cities = listOf(
        City("Amsterdam", "nl"),
        City("Andorra la Vella", "ad"),
        City("Athens", "gr"),
        City("Belgrade", "rs"),
        City("Berlin", "de"),
        City("Bern", "ch"),
        City("Bratislava", "sk"),
        City("Brussels", "be"),
        City("Bucharest", "ro"),
        City("Budapest", "hu"),
        City("Chisinau", "md"),
        City("Copenhagen", "dk"),
        City("Dublin", "ie"),
        City("Helsinki", "fi"),
        City("Lisbon", "pt"),
        City("Ljubljana", "si"),
        City("London", "uk"),
        City("Luxembourg", "lu"),
        City("Madrid", "es"),
        City("Minsk", "by"),
        City("Monaco", "mc"),
        City("Moscow", "ru"),
        City("Nicosia", "cy"),
        City("Oslo", "no"),
        City("Paris", "fr"),
        City("Podgorica", "me"),
        City("Prague", "cz"),
        City("Pristina", "xk"),
        City("Reykjavik", "is"),
        City("Riga", "lv"),
        City("Rome", "it"),
        City("San Marino", "sm"),
        City("Sarajevo", "ba"),
        City("Skopje","mk"),
        City("Sofia","bg"),
        City("Stockholm","se"),
        City("Tallinn","ee"),
        City("Tirana","al"),
        City("Vaduz","li"),
        City("Valletta","mt"),
        City("Vatican City", "va"),
        City("Vienna", "at"),
        City("Vilnius","lt"),
        City("Zagreb", "hr"),
        City("Warsaw","pl")
    )

    interface onCitySelectedListener {
        fun onCitySelected(city: City)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        val viewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        searchEditText = view.findViewById(R.id.search_edit_text)
        citiesRecyclerView = view.findViewById(R.id.cities_recycler_view)

        citiesRecyclerView.layoutManager = LinearLayoutManager(context)

        citiesRecyclerView.adapter = SearchAdapter(cities, this, findNavController())

        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val searchText = s.toString()
                val filteredCities = cities.filter { city ->
                    city.name.contains(searchText, ignoreCase = true)
                }
                (citiesRecyclerView.adapter as SearchAdapter).submitList(filteredCities)
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val homeFragment = findNavController().previousBackStackEntry?.destination?.id?.let { parentFragmentManager.findFragmentById(it) } as? SearchFragment.onCitySelectedListener
        (citiesRecyclerView.adapter as? SearchAdapter)?.onCitySelectedListener = homeFragment
    }
}