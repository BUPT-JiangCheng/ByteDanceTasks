package com.example.task1.weather;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

// Retrofit 接口定义
public interface WeatherService {
    @GET("v3/weather/weatherInfo?extensions=all")
    Call<WeatherResponse> getWeather(
            @Query("city") String cityCode,
            @Query("key") String apiKey
    );
}