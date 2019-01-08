package com.example.waleed.weatherapp.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.waleed.weatherapp.Common.Common;
import com.example.waleed.weatherapp.Model.WeatherForecastResult;
import com.example.waleed.weatherapp.R;
import com.squareup.picasso.Picasso;

public class WeatherForecastAdapter extends RecyclerView.Adapter<WeatherForecastAdapter.MYViewHolder> {
    Context context;
    WeatherForecastResult weatherForecastResult;

    public WeatherForecastAdapter(Context context, WeatherForecastResult weatherForecastResult) {
        this.context = context;
        this.weatherForecastResult = weatherForecastResult;
    }

    @NonNull
    @Override
    public MYViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemview= LayoutInflater.from(context).inflate(R.layout.item_weather_forecast,parent,false);

        return new MYViewHolder(itemview);
    }

    @Override
    public void onBindViewHolder(@NonNull MYViewHolder holder, int position) {
Picasso.get().load(new StringBuilder("https://openweathermap.org/img/w/").append(weatherForecastResult.list.get(position).weather.get(0).getIcon()).append(".png").toString()).into(holder.weather_image);
holder.txt_date_time.setText(new StringBuilder(Common.convertUnixTODate(weatherForecastResult.list.get(position).dt)));
holder.txt_description.setText(new StringBuilder(weatherForecastResult.list.get(position).weather.get(0).getDescription()));
holder.txt_temparture.setText(new StringBuilder(String.valueOf(weatherForecastResult.list.get(position).main.getTemp())).append("°C"));



    }

    @Override
    public int getItemCount() {
        return weatherForecastResult.list.size();
    }

    public class MYViewHolder extends RecyclerView.ViewHolder {
        TextView txt_date_time,txt_description,txt_temparture;
        ImageView weather_image;

        public MYViewHolder(View itemView) {
            super(itemView);
            txt_date_time=(TextView)itemView.findViewById(R.id.txtdate);
            txt_description=(TextView)itemView.findViewById(R.id.textdescription);
            txt_temparture=(TextView)itemView.findViewById(R.id.texttemparture);
            weather_image=(ImageView)itemView.findViewById(R.id.imageofweather);
        }
    }
}
