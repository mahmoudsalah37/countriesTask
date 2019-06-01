package com.example.countriesapi.data

import io.reactivex.Observable
import retrofit2.http.GET


interface ApiCountries {
  //  @GET("/filippella/Sample-API-Files/master/json/movies-api.json")
    @GET("/mahmoudsalah37/countries/master/movies-api.json")
    fun getCountries(): Observable<CountryResponse>
}