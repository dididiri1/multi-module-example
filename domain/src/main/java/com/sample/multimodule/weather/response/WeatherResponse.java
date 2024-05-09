package com.sample.multimodule.weather.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class WeatherResponse {


    private Long id;

    private int temperature;

    private String skyState;

    private String precipitationState;

    @Builder
    public WeatherResponse(Long id, int temperature, String skyState, String precipitationState) {
        this.id = id;
        this.temperature = temperature;
        this.skyState = skyState;
        this.precipitationState = precipitationState;
    }
}
