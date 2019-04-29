package com.example.challengepapb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class WeatherActivity extends AppCompatActivity {

    private boolean stat = false;
    private String latitude, longitude, suhu, cuaca, kota;
    private static String JSON_URL = "";
    private TextView txtCity, txtTemp, txtWeather;


    protected Cursor cursor;


    @SuppressLint("WrongConstant")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);

        txtCity = findViewById(R.id.tvKota);
        txtTemp = findViewById(R.id.tvSuhu);
        txtWeather = findViewById(R.id.tvCuaca);

        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");



        JSON_URL = "https://api.openweathermap.org/data/2.5/find?lat="+latitude+"&lon="+longitude+"&cnt=1&appid=20a6702b4e57c0e0fcb3e0b63a81f2ef";

        if (isConnected()){
            loadWeather();
        }else{
            finish();
            Toast.makeText(this,"Nyalakan Koneksi Internet", 1000);
        }

    }

    private boolean isConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }
        else
            return false;

    }

    private void loadWeather(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, JSON_URL, new Response.Listener<String>() {            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    JSONArray arr = obj.getJSONArray("list");
                    JSONObject cityObject = arr.getJSONObject(0);
                    setKota(cityObject.getString("name"));
                    JSONObject coordObject = cityObject.getJSONObject("coord");
                    setLatitude(coordObject.getString("lat"));
                    setLongitude(coordObject.getString("lon"));
                    JSONObject tempObject = cityObject.getJSONObject("main");
                    String temp2 = tempObject.getString("temp");
                    JSONArray weatherArray = cityObject.getJSONArray("weather");
                    JSONObject weatherObject = weatherArray.getJSONObject(0);
                    setCuaca(weatherObject.getString("main"));
                    setSuhu(String.valueOf(Math.ceil(Double.valueOf(temp2)-273.15)));

                    txtCity.setText(kota);
                    txtTemp.setText(suhu+" 'C");
                    txtWeather.setText(cuaca);

                    stat=true;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getKota() {
        return kota;
    }

    public void setKota(String kota) {
        this.kota = kota;
    }

    public String getSuhu() {
        return suhu;
    }

    public void setSuhu(String suhu) {
        this.suhu = suhu;
    }

    public String getCuaca() {
        return cuaca;
    }

    public void setCuaca(String cuaca) {
        this.cuaca = cuaca;
    }
}
