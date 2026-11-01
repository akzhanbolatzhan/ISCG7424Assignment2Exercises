package com.example.iscg7424assignment2exercises.exerciseb.network;

import com.example.iscg7424assignment2exercises.exerciseb.model.Customers;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CustomerRESTAPI {
    @GET("users")
    Call<Customers> getCustomers();

}