package com.run.demo.simpleweather.Impl;

import com.run.demo.simpleweather.Bean.WeatherDataBean;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by run on 2017/12/11.
 */

public interface WeatherImpl {

    // http://v.juhe.cn/
    // weather/index?
    // format=2&cityname=上海&key=e897a4bec54fd3cbdfbdb1600c2cd07a

    @GET("weather/index?")
    Call<WeatherDataBean> getWeather(@Query("format")String format, @Query("cityname")String cityname, @Query("key")String key);
}
