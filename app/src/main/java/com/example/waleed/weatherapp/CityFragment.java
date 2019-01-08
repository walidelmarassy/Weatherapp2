package com.example.waleed.weatherapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.label305.asynctask.SimpleAsyncTask;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.squareup.picasso.Picasso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class CityFragment extends Fragment {
    private List<String>citylist;
    private MaterialSearchBar searchBar;
    ImageView weatherimage;
    TextView txt_cityname,txtHummidity,txtpressure,txtwind,txtsunrise,txtsunset,txtdesciption,txt_date_time,txtgeocoord,txttemparture;
    LinearLayout weatherpanel;
    ProgressBar loading;
    CompositeDisposable compositeDisposable;
    IOpenWeatherMap mservice;
    static CityFragment instance;
    public static CityFragment getInstance(){
        if (instance==null)
            instance=new CityFragment();
        return instance;
    }


    public CityFragment() {
        compositeDisposable=new CompositeDisposable();
        Retrofit retrofit= RetrofitClient.getInstance();
        mservice=retrofit.create(IOpenWeatherMap.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View itemview= inflater.inflate(R.layout.fragment_city, container, false);
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
        searchBar=(MaterialSearchBar)itemview.findViewById(R.id.searchbar);
        searchBar.setEnabled(false);//
        new loadcities().execute();



        return itemview;
    }

    private class loadcities extends SimpleAsyncTask<List<String>>{

        @Override
        protected List<String> doInBackgroundSimple() {
            citylist=new ArrayList<>();
            try {
                StringBuilder builder=new StringBuilder();
                InputStream is=getResources().openRawResource(R.raw.city_list);
                GZIPInputStream gzipInputStream=new GZIPInputStream(is);
                InputStreamReader reader=new InputStreamReader(gzipInputStream);
                BufferedReader in=new BufferedReader(reader);
                String Readed;
                while ((Readed=in.readLine())!=null)
                    builder.append(Readed);
                citylist=new Gson().fromJson(builder.toString(),new TypeToken<List<String>>(){}.getType());

            } catch (IOException e) {
                e.printStackTrace();
            }

            return citylist;
        }


        @Override
        protected void onSuccess(final List<String> CityList) {
            super.onSuccess(CityList);
            searchBar.setEnabled(true);
            searchBar.addTextChangeListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    List<String>suggest=new ArrayList<>();
                    for (String search:CityList){
                        if (search.toLowerCase().contains(searchBar.getText().toLowerCase()))
                            suggest.add(search);

                    }
                    searchBar.setLastSuggestions(suggest);



                }
            });
            searchBar.setOnSearchActionListener(new MaterialSearchBar.OnSearchActionListener() {
                @Override
                public void onSearchStateChanged(boolean enabled) {

                }

                @Override
                public void onSearchConfirmed(CharSequence text) {
                    getweatherInformation(text.toString());
                    searchBar.setLastSuggestions(citylist);


                }

                @Override
                public void onButtonClicked(int buttonCode) {

                }
            });
            searchBar.setLastSuggestions(citylist);
            loading.setVisibility(View.GONE);
        }
    }

    private void getweatherInformation(String cityName) {
        compositeDisposable.add(mservice.getweatherByCityName(cityName,Common.API_ID,"metric").subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<WeatherResult>() {
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
