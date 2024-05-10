package com.sample.multmodule;

import com.sample.multimodule.weather.request.WeatherCreateRequest;
import com.sample.multimodule.weather.response.WeatherResponse;
import com.sample.multimodule.handler.ex.CustomException;
import com.sample.multimodule.weather.WeatherStore;
import com.sample.multmodule.repositories.WeatherRepositoryJpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@RequiredArgsConstructor
@Component
public class WeatherStoreJpaImpl implements WeatherStore {

    private final WeatherRepositoryJpa weatherRepositoryJpa;

    @Override
    public void createWeather(WeatherCreateRequest weatherCreateRequest) {
        Weather weather = Weather.builder()
                .precipitationState(weatherCreateRequest.getPrecipitationState())
                .temperature(weatherCreateRequest.getTemperature())
                .skyState(weatherCreateRequest.getSkyState())
                .humidity(weatherCreateRequest.getHumidity())
                .baseTime(weatherCreateRequest.getBaseTime())
                .build();

        weatherRepositoryJpa.save(weather);
    }

    @Override
    public WeatherResponse findTopByOrderByIdDesc() {
        Weather weather = weatherRepositoryJpa.findTopByOrderByIdDesc();

        if (weather == null) {
            throw new CustomException("날씨 정보가 없습니다.");
        }

        return WeatherResponse.builder()
                .precipitationState(weather.getPrecipitationState())
                .temperature(weather.getTemperature())
                .skyState(weather.getSkyState())
                .humidity(weather.getHumidity())
                .baseTime(weather.getBaseTime())
                .build();
    }

}
