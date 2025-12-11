package com.example.task1.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.example.task1.R;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.task1.weather.WeatherResponse;
import com.example.task1.weather.WeatherService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherFragment extends Fragment {

    // 高德Web服务Key
    private static final String AMAP_KEY = "42fc4ed6c0d705111ebb946d6d9f886f";

    private TextView tvCityName, tvTempRange, tvWeather;
    private TextView tvForecast1, tvForecast2, tvForecast3;
    private Spinner citySpinner;
    private WeatherService weatherService;

    // 城市映射
    private final String[] cities = {"北京", "上海", "广州", "深圳"};
    private final String[] cityCodes = {"110000", "310000", "440100", "440300"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        initView(view);
        initRetrofit();
        setupSpinner();
        return view;
    }

    private void initView(View view) {
        citySpinner = view.findViewById(R.id.spinner_city);
        tvCityName = view.findViewById(R.id.tv_city_name);
        tvTempRange = view.findViewById(R.id.tv_temp_range);
        tvWeather = view.findViewById(R.id.tv_weather);
        tvForecast1 = view.findViewById(R.id.tv_forecast_1);
        tvForecast2 = view.findViewById(R.id.tv_forecast_2);
        tvForecast3 = view.findViewById(R.id.tv_forecast_3);
    }

    private void initRetrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://restapi.amap.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        weatherService = retrofit.create(WeatherService.class);
    }
    // 初始化下拉框，设置监听器
    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_dropdown_item, cities);
        citySpinner.setAdapter(adapter);

        citySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fetchWeather(cityCodes[position]);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void fetchWeather(String cityCode) {
        weatherService.getWeather(cityCode, AMAP_KEY).enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null && "1".equals(response.body().status)) {
                    if (!response.body().forecasts.isEmpty()) {
                        updateUI(response.body().forecasts.get(0));
                    }
                } else {
                    tvWeather.setText("获取数据失败");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                Toast.makeText(getContext(), "网络错误: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(WeatherResponse.Forecast forecast) {
        tvCityName.setText(forecast.city);

        // 今天
        WeatherResponse.Cast today = forecast.casts.get(0);
        tvWeather.setText(today.dayweather);
        tvTempRange.setText(today.nighttemp + "°C ~ " + today.daytemp + "°C");

        // 预报
        setForecastText(tvForecast1, forecast.casts.get(0), "今天");
        if (forecast.casts.size() > 1) setForecastText(tvForecast2, forecast.casts.get(1), "明天");
        if (forecast.casts.size() > 2) setForecastText(tvForecast3, forecast.casts.get(2), "后天");
    }

    private void setForecastText(TextView tv, WeatherResponse.Cast cast, String dayLabel) {
        tv.setText(dayLabel + "：" + cast.dayweather + "  " + cast.nighttemp + "~" + cast.daytemp + "°C");
    }
}