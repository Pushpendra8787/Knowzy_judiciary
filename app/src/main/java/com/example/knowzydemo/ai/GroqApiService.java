package com.example.knowzydemo.ai;

import com.example.knowzydemo.ai.models.GroqRequest;
import com.example.knowzydemo.ai.models.GroqResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface GroqApiService {

    @Headers({
            "Content-Type: application/json"
    })
    @POST("openai/v1/chat/completions")
    Call<GroqResponse> getAIResponse(
            @Header("Authorization") String authorization,
            @Body GroqRequest request
    );
}