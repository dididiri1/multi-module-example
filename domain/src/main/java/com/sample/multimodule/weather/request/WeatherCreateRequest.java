package com.sample.multimodule.weather.request;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WeatherCreateRequest {

    private int temperature;

    private String skyState;

    private String precipitationState;

    @Builder
    public WeatherCreateRequest(int temperature, String skyState, String precipitationState) {
        this.temperature = temperature;
        this.skyState = skyState;
        this.precipitationState = precipitationState;
    }

}
