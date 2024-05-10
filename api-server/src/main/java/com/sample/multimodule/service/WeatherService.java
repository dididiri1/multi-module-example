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
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;


@Service
@RequiredArgsConstructor
public class WeatherService {

    private final RestTemplate restTemplate;

    private final WeatherStore weatherStore;

    private static final List<Integer> BASE_TIMES = Arrays.asList(2, 5, 8, 11, 14, 17, 20, 23);

    private final String API = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst?serviceKey=je3xpfccvODTTa7rHpRbmnGRBdergDEDfSA%2B3yGjgXbRz6g35AmYmEgFK83JAX%2BuyT4A1Etldb8TzkSNsoQQqw%3D%3D&pageNo=1&numOfRows=10&dataType=JSON&base_date=20240508&base_time=0500&nx=61&ny=131";

    @Transactional
    public void createWeather() throws Exception {

        LocalDateTime now = LocalDateTime.now();
        String serviceKey = "je3xpfccvODTTa7rHpRbmnGRBdergDEDfSA%2B3yGjgXbRz6g35AmYmEgFK83JAX%2BuyT4A1Etldb8TzkSNsoQQqw%3D%3D";
        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = calculateBaseTime(now);

        String apiUrl = getApiUrl(serviceKey, baseDate, baseTime, 1, 30, 61, 131);;

        System.out.println("apiUrl = " + apiUrl);

        URI uri = new URI(apiUrl);
        ResponseEntity<String> response = restTemplate.exchange(uri, HttpMethod.GET, null, String.class);
        String jsonResponse = response.getBody();

        WeatherCreateRequest weatherCreateRequest = jsonToWeatherEntity(jsonResponse, baseTime);
        weatherStore.createWeather(weatherCreateRequest);
    }

    private static String calculateBaseTime(LocalDateTime now) {
        int hour = now.getHour();
        int baseHour = 0;
        if (hour < 2) {
            baseHour = 23;
        } else if (hour < 5) {
            baseHour = 2;
        } else if (hour < 8) {
            baseHour = 5;
        } else if (hour < 11) {
            baseHour = 8;
        } else if (hour < 14) {
            baseHour = 11;
        } else if (hour < 17) {
            baseHour = 14;
        } else if (hour < 20) {
            baseHour = 17;
        } else if (hour < 23) {
            baseHour = 20;
        } else {
            baseHour = 23;
        }
        return String.format("%02d00", baseHour);
    }

    private static String getApiUrl(String serviceKey, String baseDate, String baseTime, int pageNo, int numOfRows, int nx, int ny) {
        return "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst"
                + "?serviceKey=" + serviceKey
                + "&base_date=" + baseDate
                + "&base_time=" + baseTime
                + "&pageNo=" + pageNo
                + "&numOfRows=" + numOfRows
                + "&dataType=JSON"
                + "&nx=" + nx
                + "&ny=" + ny;
    }



    public WeatherCreateRequest jsonToWeatherEntity(String jsonResponse, String baseTime) {
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);
        JsonObject responseBody = jsonObject.getAsJsonObject("response").getAsJsonObject("body");
        JsonObject itemsObject = responseBody.getAsJsonObject("items");
        JsonArray items = itemsObject.getAsJsonArray("item");

        int TMP = 0;
        String SKY = "";
        String PTY = "";
        int REH = 0;
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
            if(category.equals("REH") ){
                REH = Integer.valueOf(fcstValue);
            }
        }

        return WeatherCreateRequest.builder()
                .temperature(TMP)
                .skyState(SKY)
                .precipitationState(PTY)
                .humidity(REH)
                .baseTime(baseTime)
                .build();
    }

    public WeatherResponse getWeather() {
        WeatherResponse weatherResponse = weatherStore.findTopByOrderByIdDesc();

        return weatherResponse;
    }
}
