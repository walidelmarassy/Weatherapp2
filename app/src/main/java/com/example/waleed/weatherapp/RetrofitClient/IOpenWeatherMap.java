package com.example.waleed.weatherapp.RetrofitClient;

import io.reactivex.Observable;

import com.example.waleed.weatherapp.Model.WeatherForecastResult;
import com.example.waleed.weatherapp.Model.WeatherResult;

import retrofit2.http.GET;
import retrofit2.http.Query;

public interface IOpenWeatherMap {
    @GET("weather")
    Observable<WeatherResult> getweatherBylatlng(@Query("lat")String lat, @Query("lon")String lng, @Query("appid")String appid, @Query("units")String unit);
    @GET("weather")
    Observable<WeatherResult> getweatherByCityName(@Query("q")String CityName,  @Query("appid")String appid, @Query("units")String unit);



    @GET("forecast")
    Observable<WeatherForecastResult> getWeatherForecastBylatlng(@Query("lat")String lat, @Query("lon")String lng, @Query("appid")String appid, @Query("units")String unit);


}
