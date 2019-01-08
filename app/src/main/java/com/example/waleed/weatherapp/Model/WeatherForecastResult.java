package com.example.waleed.weatherapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherForecastResult {
    @SerializedName("cod")
        @Expose
        public String cod;
        @SerializedName("message")
        @Expose
        public Double message;
        @SerializedName("cnt")
        @Expose
        public Integer cnt;
        @SerializedName("list")
        @Expose
        public List<MyList> list;
        @SerializedName("city")
        @Expose
        public City city;

        public String getCod() {
            return cod;
        }

        public void setCod(String cod) {
            this.cod = cod;
        }

        public Double getMessage() {
            return message;
        }

        public void setMessage(Double message) {
            this.message = message;
        }

        public Integer getCnt() {
            return cnt;
        }

        public void setCnt(Integer cnt) {
            this.cnt = cnt;
        }


        public List<MyList> getList() {
        return list;
        }


         public void setList(List<MyList> list) {
         this.list = list;
      }

        public City getCity() {
            return city;
        }

        public void setCity(City city) {
            this.city = city;
        }

    }

