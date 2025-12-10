package com.example.task1.weather;
import java.util.List;

// 高德API
public class WeatherResponse {
    public String status;
    public List<Forecast> forecasts;

    public static class Forecast {
        public String city;
        public List<Cast> casts;
    }

    public static class Cast {
        public String date;
        public String dayweather;
        public String daytemp; // 高温
        public String nighttemp; // 低温
    }
}