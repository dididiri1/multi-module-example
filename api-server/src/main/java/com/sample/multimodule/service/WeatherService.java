package com.sample.multimodule.service;


import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sample.multimodule.weather.request.WeatherCreateRequest;
import com.sample.multimodule.weather.response.WeatherResponse;
import com.sample.multimodule.weather.WeatherStore;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;

    private final WeatherStore weatherStore;

    private final String API = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=je3xpfccvODTTa7rHpRbmnGRBdergDEDfSA%2B3yGjgXbRz6g35AmYmEgFK83JAX%2BuyT4A1Etldb8TzkSNsoQQqw%3D%3D&pageNo=1&numOfRows=10&dataType=JSON&base_date=20240508&base_time=0500&nx=61&ny=131";

    @Transactional
    public void createWeather() throws Exception {
        URI uri = new URI(API);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        String jsonResponse = response.getBody();

        WeatherCreateRequest weatherCreateRequest = jsonToWeatherEntity(jsonResponse);
        weatherStore.createWeather(weatherCreateRequest);
    }

    public WeatherResponse getWeather() {
        WeatherResponse weatherResponse = weatherStore.findTopByOrderByIdDesc();

        return weatherResponse;
    }

    public WeatherCreateRequest jsonToWeatherEntity(String jsonResponse) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject responseBody = jsonObject.getAsJsonObject("response").getAsJsonObject("body");
        JsonObject itemsObject = responseBody.getAsJsonObject("items");
        JsonArray items = itemsObject.getAsJsonArray("item");

        int TMP = 0;
        String SKY = "";
        String PTY = "";
        for(int i = 0; i < items.size(); i++) {
            JsonObject item = items.get(i).getAsJsonObject();
            String fcstValue = item.get("fcstValue").getAsString();
            String category = item.get("category").getAsString();
            if(category.equals("SKY")){
                if(fcstValue.equals("1")) {
                    SKY = "맑은";
                }else if(fcstValue.equals("3")) {
                    SKY = "구름 많은";
                }else if(fcstValue.equals("4")) {
                    SKY = "흐린";
                }
            }
            if(category.equals("PTY")){
                if(fcstValue.equals("0")) {
                    PTY = "없음";
                }else if(fcstValue.equals("3")) {
                    PTY = "비";
                } else if(fcstValue.equals("4")) {
                    PTY = "비/눈";
                } else if(fcstValue.equals("4")) {
                    PTY = "눈";
                } else {
                    PTY = "소나기";
                }
            }
            if(category.equals("TMP") ){
                TMP = Integer.valueOf(fcstValue);
            }
        }

        return WeatherCreateRequest.builder()
                .temperature(TMP)
                .skyState(SKY)
                .precipitationState(PTY)
                .build();
    }
}
