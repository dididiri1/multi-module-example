package com.sample.multimodule.controller;

import com.sample.multimodule.weather.response.WeatherResponse;
import com.sample.multimodule.dto.ApiResponse;
import com.sample.multimodule.service.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class WeatherController {

    private final WeatherService weatherService;

    @PostMapping("/api/weather")
    public ResponseEntity<?> createWeather() throws Exception{
        weatherService.createWeather();
        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "날씨 등록 성공", null), HttpStatus.OK);
    }

    @GetMapping("/api/weather")
    public ResponseEntity<?> getWeather() {
        WeatherResponse weatherResponse = weatherService.getWeather();

        return new ResponseEntity<>(new ApiResponse<>(HttpStatus.OK.value(), "날씨 조회 성공", weatherResponse), HttpStatus.OK);
    }

}
