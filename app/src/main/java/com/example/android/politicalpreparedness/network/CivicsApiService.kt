package com.example.android.politicalpreparedness.network

import com.example.android.politicalpreparedness.network.models.ElectionResponse
import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/civicinfo/v2/"

// TODO: Add adapters for Java Date and custom adapter ElectionAdapter (included in project)
private val moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

private val retrofit = Retrofit.Builder()
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .client(CivicsHttpClient.getClient())
        .baseUrl(BASE_URL)
        .build()

/**
 *  Documentation for the Google Civics API Service can be found at https://developers.google.com/civic-information/docs/v2
 */

interface CivicsApiService {

    @GET("elections")
    suspend fun getElections(): Response<ElectionResponse>

    @GET("voterinfo")
    suspend fun getVoterInfo(@Query("address") address: String): Response<VoterInfoResponse>

    @GET("representatives")
    suspend fun getRepresentativeInfoByAddress(@Query("address") address: String): Response<RepresentativeResponse>

    @GET("representatives/{ocdId}")
    suspend fun getRepresentativeInfoByDivision(@Path("ocdId") ocdId: String): Response<RepresentativeResponse> // Assuming it has the same response structure as representativeInfoByAddress.

    @GET("divisions")
    suspend fun searchDivisions(@Query("query") query: String): Response<Any> // Replace `Any` with the appropriate data class for the response, e.g., DivisionsResponse.
}



object CivicsApi {
    val retrofitService: CivicsApiService by lazy {
        retrofit.create(CivicsApiService::class.java)
    }
}