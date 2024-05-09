package com.sample.multimodule.weather;


import com.sample.multimodule.weather.request.WeatherCreateRequest;
import com.sample.multimodule.weather.response.WeatherResponse;

public interface WeatherStore {

    WeatherResponse findTopByOrderByIdDesc();

    void createWeather(WeatherCreateRequest weatherCreateRequest);
}
