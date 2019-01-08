package com.example.waleed.weatherapp;


import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.waleed.weatherapp.Common.Common;
import com.example.waleed.weatherapp.Model.WeatherResult;
import com.example.waleed.weatherapp.RetrofitClient.IOpenWeatherMap;
import com.example.waleed.weatherapp.RetrofitClient.RetrofitClient;
import com.squareup.picasso.Picasso;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class TodayWeatherFragment extends Fragment {
    ImageView weatherimage;
    TextView txt_cityname,txtHummidity,txtpressure,txtwind,txtsunrise,txtsunset,txtdesciption,txt_date_time,txtgeocoord,txttemparture;
    LinearLayout weatherpanel;
    ProgressBar loading;
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mservice;


   static TodayWeatherFragment instance;
   public static TodayWeatherFragment getInstance(){
       if (instance==null)
           instance=new TodayWeatherFragment();
       return instance;
   }


    public TodayWeatherFragment() {
        compositeDisposable=new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        mservice=retrofit.create(IOpenWeatherMap.class);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview= inflater.inflate(R.layout.fragment_today_weather, container, false);
        weatherimage=(ImageView)itemview.findViewById(R.id.imageofweather);
        txt_cityname=(TextView)itemview.findViewById(R.id.txtname);
        txtHummidity=(TextView)itemview.findViewById(R.id.txthumiddity);
        txtpressure=(TextView)itemview.findViewById(R.id.txtpressure);
        txtwind=(TextView)itemview.findViewById(R.id.txtwind);
        txtsunrise=(TextView)itemview.findViewById(R.id.txtsunrise);
        txtsunset=(TextView)itemview.findViewById(R.id.txtsunset);
        txttemparture=(TextView)itemview.findViewById(R.id.texttemparture);
        txtdesciption=(TextView)itemview.findViewById(R.id.txtdescription);
        txt_date_time=(TextView)itemview.findViewById(R.id.txt_date_time);
        txtgeocoord=(TextView)itemview.findViewById(R.id.txtGeocoords);

        weatherpanel=(LinearLayout)itemview.findViewById(R.id.weather_panel);
        loading=(ProgressBar)itemview.findViewById(R.id.loading);
        getweatherInformation();
        return itemview;

    }

    private void getweatherInformation() {
       compositeDisposable.add(mservice.getweatherBylatlng(String.valueOf(Common.current_location.getLatitude()),String.valueOf(Common.current_location.getLongitude()),Common.API_ID,"metric").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WeatherResult>() {
                   @Override
                   public void accept(WeatherResult weatherResult) throws Exception {
                       //load image
                       Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/").append(weatherResult.getWeather().get(0).getIcon()).append(".png").toString()).into(weatherimage);
                       //load info
                       txt_cityname.setText(weatherResult.getName());
                       txtdesciption.setText(new StringBuilder("weather in").append(weatherResult.getName()).toString());
                       txttemparture.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getTemp())).append("°C").toString());
                       txt_date_time.setText(Common.convertUnixTODate(weatherResult.getDt()));
                       txtpressure.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getPressure())).append("hpa").toString());
                       txtHummidity.setText(new StringBuilder(String.valueOf(weatherResult.getMain().getHumidity())).append("%").toString());
                       txtsunrise.setText(Common.convertUnixTOHour(weatherResult.getSys().getSunrise()));
                       txtsunrise.setText(Common.convertUnixTOHour(weatherResult.getSys().getSunset()));
                       txtgeocoord.setText(new StringBuilder("[").append(weatherResult.getCoord().toString()).append("]").toString());
                       //Display panel
                       weatherpanel.setVisibility(View.VISIBLE);
                       loading.setVisibility(View.GONE);





                   }
               }, new Consumer<Throwable>() {
                   @Override
                   public void accept(Throwable throwable) throws Exception {

                   }
               })
       );



    }
    @Override
    public void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }

    @Override
    public void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }

}
