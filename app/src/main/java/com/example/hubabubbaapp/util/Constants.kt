package com.example.hubabubbaapp.util

class Constants {
    companion object {

        const val BASE_URL = "http://www.boredapi.com"

        // ROOM DATABASE
        const val DATABASE_NAME = "bored_db"
        const val ACTIVITY_TABLE = "activities_table"
        const val FAVORITES_TABLE = "favorites_table"

        // API QUERY KEYS

        const val QUERY_MINPRICE = "minprice"
        const val QUERY_MAXPRICE = "maxprice"
        const val QUERY_TYPE = "type"
        const val QUERY_PARTICIPANT = "participants"

        // Bottom sheet preferences
        const val DEFAULT_TYPE = "recreational"
        const val DEFAULT_MINPRICE = 0.00
        const val DEFAULT_MAXPRICE = 1.00
        const val DEFAULT_PARTICIPANT = 1
    }
}