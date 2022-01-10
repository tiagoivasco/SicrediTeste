package com.ivasco.sicrediteste.model

data class Events(
    val date: String,
    val description: String,
    val id: String,
    val image: String,
    val latitude: Double,
    val longitude: Double,
    val price: Float,
    val title: String
)