package com.run.demo.simpleweather;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.run.demo.simpleweather.Bean.WeatherDataBean;
import com.run.demo.simpleweather.Impl.WeatherImpl;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    private LinearLayout mLayout_Refresh;
    private ImageView mImageView_Refresh;
    private TextView mTextView_Refresh;
    private TextView mTextView_City;
    private TextView mTextView_UpdateTime;
    private TextView mTextView_LunarDate;
    private ImageView mImageView_WeatherIcon;
    private TextView mTextView_Temperature;
    private TextView mTextView_WeatherDescription;
    private TextView mTextView_WindDirection;
    private TextView mTextView_WindStrength;

    public LocationClient mLocationClient = null;
    private MyLocationListener myListener = new MyLocationListener();

    //提示框
    private ProgressDialog dialog;

    private WeatherImpl weatherImpl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //去掉actionBar的阴影
        getSupportActionBar().setElevation(0);

        initViews();

    }

    private void initViews() {

        //声明LocationClient类
        mLocationClient = new LocationClient(getApplicationContext());
        //注册监听函数
        mLocationClient.registerLocationListener(myListener);

        initLocation();
        initDialog();
        initRetrofit();
        dialog.show();

        mLocationClient.start();
        Log.i("SimpleWeather", "开始定位");


        mLayout_Refresh = findViewById(R.id.layout_Refresh);
        mImageView_Refresh = findViewById(R.id.imageView_Refresh);
        mTextView_Refresh = findViewById(R.id.textView_Refresh);
        mTextView_City = findViewById(R.id.textView_City);
        mTextView_UpdateTime = findViewById(R.id.textView_UpdateTime);
        mTextView_LunarDate = findViewById(R.id.textView_LunarDate);
        mImageView_WeatherIcon = findViewById(R.id.imageView_WeatherIcon);
        mTextView_Temperature = findViewById(R.id.textView_Temperature);
        mTextView_WeatherDescription = findViewById(R.id.textView_WeatherDescription);
        mTextView_WindDirection = findViewById(R.id.textView_WindDirection);
        mTextView_WindStrength = findViewById(R.id.textView_WindStrength);
    }

    //初始化进度
    private void initDialog(){

        dialog = new ProgressDialog(this);
        dialog.setMessage("定位中");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setCancelable(false);

    }

    private void initLocation() {

        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);

        option.setCoorType("bd09ll");

        option.setScanSpan(0);

        option.setOpenGps(true);

        option.setLocationNotify(true);

        option.setIgnoreKillProcess(false);

        option.SetIgnoreCacheException(false);

        option.setWifiCacheTimeOut(5*60*1000);

        option.setEnableSimulateGps(false);

        option.setIsNeedAddress(true);

        mLocationClient.setLocOption(option);

    }

    private void initRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://v.juhe.cn/")
                .build();

         weatherImpl = retrofit.create(WeatherImpl.class);
    }

    private void requestWeather(String city){
        weatherImpl.getWeather("2", city, "e897a4bec54fd3cbdfbdb1600c2cd07a").enqueue(new Callback<WeatherDataBean>() {
            @Override
            public void onResponse(Call<WeatherDataBean> call, Response<WeatherDataBean> response) {
                Log.i("SimpleWeather", response.body().toString());
                WeatherDataBean weatherDataBean = response.body();

                mTextView_City.setText(weatherDataBean.getResult().getToday().getCity());
                mTextView_UpdateTime.setText(weatherDataBean.getResult().getToday().getDate_y());
                mTextView_LunarDate.setText(weatherDataBean.getResult().getToday().getWeek());
                mTextView_Temperature.setText(weatherDataBean.getResult().getToday().getTemperature());
                mTextView_WeatherDescription.setText(weatherDataBean.getResult().getToday().getWeather());
                mTextView_WindDirection.setText(weatherDataBean.getResult().getSk().getWind_direction());
                mTextView_WindStrength.setText(weatherDataBean.getResult().getSk().getWind_strength());
                mTextView_UpdateTime.setText(weatherDataBean.getResult().getSk().getTime());


            }

            @Override
            public void onFailure(Call<WeatherDataBean> call, Throwable t) {

            }
        });
    }



    public class MyLocationListener implements BDLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            String addr = location.getAddrStr();    //获取详细地址信息
            String country = location.getCountry();    //获取国家
            String province = location.getProvince();    //获取省份
            String city = location.getCity();    //获取城市
            String district = location.getDistrict();    //获取区县
            String street = location.getStreet();    //获取街道信息

            dialog.dismiss();

            Log.i("SimpleWeather", city);

            requestWeather(city);
        }
    }

}
