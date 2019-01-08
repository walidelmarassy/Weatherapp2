package com.example.waleed.weatherapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.waleed.weatherapp.Adapter.WeatherForecastAdapter;
import com.example.waleed.weatherapp.Common.Common;
import com.example.waleed.weatherapp.Model.WeatherForecastResult;
import com.example.waleed.weatherapp.RetrofitClient.IOpenWeatherMap;
import com.example.waleed.weatherapp.RetrofitClient.RetrofitClient;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class ForecastFragment extends Fragment {

    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mservice;
    TextView txt_cityname,txt_geo_coord;
    RecyclerView  recycler_forecast;

    static ForecastFragment instance;
    public static ForecastFragment getInstance(){
        if (instance==null)
            instance=new ForecastFragment();
        return instance;


    }


    public ForecastFragment() {
        compositeDisposable=new CompositeDisposable();
        Retrofit retrofit=RetrofitClient.getInstance();
        mservice=retrofit.create(IOpenWeatherMap.class);


    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview=inflater.inflate(R.layout.fragment_forecast, container, false);
        txt_cityname=(TextView)itemview.findViewById(R.id.cityname);
        txt_geo_coord=(TextView)itemview.findViewById(R.id.txt_geo_coord);
        recycler_forecast=(RecyclerView)itemview.findViewById(R.id.recylerforecast);
        recycler_forecast.setHasFixedSize(true);
        recycler_forecast.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
        getForecastWeatherInformation();

         return itemview;


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

    private void getForecastWeatherInformation() {
        compositeDisposable.add(mservice.getWeatherForecastBylatlng(
               String.valueOf(Common.current_location.getLatitude()),
                String.valueOf(Common.current_location.getLongitude()),
                Common.API_ID,"metric").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WeatherForecastResult>() {
                    @Override
                    public void accept(WeatherForecastResult weatherForecastResult) throws Exception {
                        displayForecastWeather(weatherForecastResult);

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        Log.d("Error",""+throwable.getMessage());


                    }
                })

        );
    }

    private void displayForecastWeather(WeatherForecastResult weatherForecastResult) {
        txt_cityname.setText(new StringBuilder(weatherForecastResult.city.name));
        txt_geo_coord.setText(new StringBuilder(weatherForecastResult.city.coord.toString()));
        WeatherForecastAdapter adapter=new WeatherForecastAdapter(getContext(),weatherForecastResult);
        recycler_forecast.setAdapter(adapter);
    }

}
