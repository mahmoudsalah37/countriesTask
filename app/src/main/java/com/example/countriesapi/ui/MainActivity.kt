package com.example.countriesapi.ui

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.countriesapi.R
import com.example.countriesapi.data.ApiCountries
import com.example.countriesapi.models.get.data.Country
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_list_of_country.view.*
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private var mAuth: FirebaseAuth? = null
    private lateinit var countryAdapter: CountriesAdapter
    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = mAuth?.currentUser
        updateUI(currentUser)
    }

    private fun updateUI(currentUser: FirebaseUser?) {

    }

    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mAuth = FirebaseAuth.getInstance()
//        actionBar.setDisplayHomeAsUpEnabled(true)
        val url = "https://raw.githubusercontent.com"
        countryAdapter = CountriesAdapter()
        RVCountries.adapter = countryAdapter
        RVCountries.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        RVCountries.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        val retrofit: Retrofit = Retrofit.Builder()
             //   .baseUrl("https://raw.githubusercontent.com")
                 .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        val apiCountries = retrofit.create(ApiCountries::class.java)
        apiCountries.getCountries()
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    countryAdapter.setCountry(it.data)
                }, {
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT)
                })

    }
     override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        onBackPressed()
        mAuth?.signOut()
         val changePage = Intent(this, LoginActivity::class.java)
         startActivity(changePage)
        finish()
        return true
    }
    inner class CountriesAdapter : RecyclerView.Adapter<CountriesAdapter.CountryViewHolder>() {
        private var countries: List<Country> = mutableListOf()
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryViewHolder {
            return CountryViewHolder(layoutInflater.inflate(R.layout.item_list_of_country, parent, false))
        }

        override fun getItemCount(): Int {
            return countries.size
        }

        override fun onBindViewHolder(holder: CountryViewHolder, position: Int) {
            holder.bindModel(countries[position])
        }

        fun setCountry(data: List<Country>?) {
            countries  = (data) as List<Country>
            notifyDataSetChanged()
        }

        inner class CountryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

            private val nameNativeText: TextView = itemView.nameNative
            private val nameText: TextView = itemView.name

            fun bindModel(country: Country) {
                nameNativeText.text = country.year
                nameText.text =  country.title
            }
        }

    }
}
