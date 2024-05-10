package com.sample.multimodule.weather.response;

import lombok.Builder;
import lombok.Getter;


@Getter
public class WeatherResponse {


    private int temperature;

    private String skyState;

    private String precipitationState;

    private int humidity;

    private String content;

    @Builder
    public WeatherResponse(int temperature, String skyState, String precipitationState, int humidity, String baseTime) {
        this.temperature = temperature;
        this.skyState = skyState;
        this.precipitationState = precipitationState;
        this.humidity = humidity;
        this.content = message(temperature, skyState, humidity, baseTime);
    }

    private String message(int temperature, String skyState, int humidity, String baseTime) {
        return "오늘 날씨 기온은 "+temperature+"℃ 이며, 하늘은 "+skyState+" 강수량은 "+humidity+"% 입니다. (발표시점: "+baseTime+")";
    }
}
