package com.sample.multimodule.weather.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WeatherCreateRequest {

    private int temperature;

    private String skyState;

    private String precipitationState;

    private int humidity;

    private String baseTime;

    @Builder
    public WeatherCreateRequest(int temperature, String skyState, String precipitationState, int humidity, String baseTime) {
        this.temperature = temperature;
        this.skyState = skyState;
        this.precipitationState = precipitationState;
        this.humidity = humidity;
        this.baseTime = baseTime;
    }
}
