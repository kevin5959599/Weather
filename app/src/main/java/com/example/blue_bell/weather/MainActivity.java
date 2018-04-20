package com.example.blue_bell.weather;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends Activity implements View.OnClickListener,ConnectionCallbacks,OnConnectionFailedListener {
    protected static final String TAG = "MainActivity";
    protected GoogleApiClient mGoogleApiClient;
    protected Location mLastLocation;
    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    private ImageView UpdateBtn;
    private String updatecitycode;
    private String cityname;
    private ImageView SelectCityBtn;
    private ImageView cloudy;
    private TextView cityT;
    private TextView temperatureT;
    private TextView rainfullT;
    private TextView statusT;
    private TextView timeT;
    private TextView cityT_2;
    private TextView temperatureT_2;
    private TextView rainfullT_2;
    private TextView statusT_2;
    private TextView timeT_2;
    String a;
    String[] b;
    String c;
    String[] d;
    String longitude;
    String latitude;
    //解析工具
    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message message)
        {
            switch (message.what)
            {
                case 1:
                    updateTodayWeather((TodayWeather) message.obj);
                    break;
                default:
                    break;
            }
        }
    };
    private Handler mHandler_2 = new Handler()
    {
        public void handleMessage(android.os.Message message_2)
        {
            switch (message_2.what)
            {
                case 1:
                    updateTodayWeather_2((TodayWeather)message_2.obj);
                    break;
                default:
                    break;
            }
        }
    };

    TodayWeather todayWeather = null;
    TodayWeather todayWeather_2 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mLatitudeLabel = "緯度";
        mLongitudeLabel = "經度";
        SelectCityBtn = (ImageView)findViewById(R.id.title_city_manager);
        SelectCityBtn.setOnClickListener(this);
        initView();

        if (CheckNet.getNetState(this)==CheckNet.NET_NONE){
            Toast.makeText(MainActivity.this,"網路不通",Toast.LENGTH_LONG).show();
        }else
        {
            Toast.makeText(MainActivity.this,"網路良好",Toast.LENGTH_LONG).show();
            buildGoogleApiClient();


            updatecitycode = getIntent().getStringExtra("citycode");
            cityname = getIntent().getStringExtra("city");
            if(updatecitycode==null)
            {
                updatecitycode = getConfig(MainActivity.this, "Config", "SaveText", "87878787");
                cityname = getConfig(MainActivity.this, "Config_2", "SaveText_2", "N/A");
                cityT_2.setText(cityname);
                getWeatherDatafromNet_2(updatecitycode);
                Log.d("111111", "代碼:" + updatecitycode);
                Log.d("222222", "城市:" + cityname);
            }
           if(updatecitycode!=getConfig(MainActivity.this, "Config","SaveText","87878787")&&updatecitycode!=null) {
                Log.d("555555","555555");
                setConfig(MainActivity.this,"Config","SaveText",updatecitycode);
                setConfig(MainActivity.this,"Config_2","SaveText_2",cityname);
                Log.d("666666","666666");
                Toast.makeText(MainActivity.this,updatecitycode,Toast.LENGTH_SHORT).show();
                getWeatherDatafromNet_2(updatecitycode);
                cityT_2.setText(cityname);
                Log.d("333333","城市:"+cityname);
                Log.d("444444","代碼:"+updatecitycode);
            }
            }

    }
    //設定檔儲存
    public static void setConfig(Context context, String name, String key, String value)
    {
        SharedPreferences settings =context.getSharedPreferences(name,0);
        SharedPreferences.Editor PE = settings.edit();
        PE.putString(key, value);
        PE.commit();
    }

    //設定檔讀取
    public static String getConfig(Context context , String name , String key , String def)
    {
        SharedPreferences settings =context.getSharedPreferences(name,0);
        return settings.getString(key, def);
    }

    //跳選擇城市
    @Override
    public void onClick(View v){
        if(v.getId()==R.id.title_city_manager)
        {

            Intent intent = new Intent(this,SelectCity.class);
            startActivity(intent);
        }else  if(v.getId()==R.id.title_city_locate)
        {
            Toast.makeText(MainActivity.this,"Test",Toast.LENGTH_SHORT);
            buildGoogleApiClient();
        }else if(v.getId()==R.id.title_city_update)
        {
            Toast.makeText(MainActivity.this,"Test_02",Toast.LENGTH_SHORT);
            getWeatherDatafromNet(updatecitycode);
        }
    }
    //獲取XML資料
    private void getWeatherDatafromNet(String cityCode)
    {
        final String address = "https://www.cwb.gov.tw/rss/forecast/36_"+cityCode+".xml";
        Log.d("Address:",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    while((str=reader.readLine())!=null)
                    {
                        sb.append(str);
                        //Log.d("date from url",str);
                    }
                    String response = sb.toString();
                    Log.d("response",response);
                    todayWeather = parseXML(response);
                    if(todayWeather!=null)
                    {
                        Message message = new Message();
                        message.what = 1;
                        message.obj = todayWeather;
                        mHandler.sendMessage(message);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getWeatherDatafromNet_2(String cityCode)
    {
        final String address = "https://www.cwb.gov.tw/rss/forecast/36_"+cityCode+".xml";
        Log.d("Address:",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection urlConnection = null;
                try {
                    URL url = new URL(address);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(8000);
                    urlConnection.setReadTimeout(8000);
                    InputStream in = urlConnection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuffer sb = new StringBuffer();
                    String str;
                    while((str=reader.readLine())!=null)
                    {
                        sb.append(str);
                        //Log.d("date from url",str);
                    }
                    String response = sb.toString();
                    Log.d("response",response);
                    todayWeather_2 = parseXML_2(response);
                    if(todayWeather_2!=null)
                    {
                        Message message_2 = new Message();
                        message_2.what = 1;
                        message_2.obj = todayWeather_2;
                        mHandler_2.sendMessage(message_2);
                    }
                }catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    //解析XML
    private TodayWeather parseXML(String xmlData)
    {
        int titleCount = 0;

        TodayWeather todayWeather = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            // Log.d("MWeater", "start parse xml");

            while (eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse", "開始解析");
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("rss"))
                        {
                            todayWeather = new TodayWeather();
                        }
                        if (todayWeather != null) {
                            if (xmlPullParser.getName().equals("title")) {
                                titleCount++;
                            }
                            if(xmlPullParser.getName().equals("title") && titleCount==3)
                            {
                                eventType = xmlPullParser.next();
                                todayWeather.setTitle(xmlPullParser.getText());
                                Log.d("87878787", xmlPullParser.getText());
                            }
                        }


                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return todayWeather;
    }
    private TodayWeather parseXML_2(String xmlData)
    {
        int titleCount = 0;

        TodayWeather todayWeather_2 = null;

        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlData));

            int eventType = xmlPullParser.getEventType();
            // Log.d("MWeater", "start parse xml");

            while (eventType!=xmlPullParser.END_DOCUMENT)
            {
                switch (eventType)
                {
                    //文档开始位置
                    case XmlPullParser.START_DOCUMENT:
                        Log.d("parse", "開始解析");
                        break;
                    //标签元素开始位置
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals("rss"))
                        {
                            todayWeather_2 = new TodayWeather();
                        }
                        if (todayWeather_2 != null) {
                            if (xmlPullParser.getName().equals("title")) {
                                titleCount++;
                            }
                            if(xmlPullParser.getName().equals("title") && titleCount==3)
                            {
                                eventType = xmlPullParser.next();
                                todayWeather_2.setTitle_2(xmlPullParser.getText());
                                Log.d("87878787", xmlPullParser.getText());
                            }
                        }


                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return todayWeather_2;
    }
    //主畫面文字
    void initView()
    {

            cityT = (TextView) findViewById(R.id.title_city_name);
            timeT = (TextView) findViewById(R.id.time);
            temperatureT = (TextView) findViewById(R.id.temperature);
            rainfullT = (TextView) findViewById(R.id.rainfall);
            statusT = (TextView) findViewById(R.id.status);

            cityT.setText("N/A");
            timeT.setText("N/A");
            temperatureT.setText("N/A");
            rainfullT.setText("N/A");
            statusT.setText("N/A");

            cityT_2 = (TextView) findViewById(R.id.title_city_name_2);
            timeT_2 = (TextView) findViewById(R.id.time_2);
            temperatureT_2 = (TextView) findViewById(R.id.temperature_2);
            rainfullT_2 = (TextView) findViewById(R.id.rainfall_2);
            statusT_2 = (TextView) findViewById(R.id.status_2);

            cityT_2.setText("N/A");
            timeT_2.setText("N/A");
            temperatureT_2.setText("N/A");
            rainfullT_2.setText("N/A");
            statusT_2.setText("N/A");
    }
    void updateTodayWeather(TodayWeather todayWeather)
    {

        a=todayWeather.toString();
        b = a.split(" ");
        for(int i=0;i<b.length;i++){
            System.out.println("array["+i+"] = "+b[i]);
            Log.d("5555555",b[i]);
        }
        Log.d("6666666",b[5]);
        timeT.setText("("+b[2]+")");
        temperatureT.setText("溫度:"+b[5]+b[6]+b[7]+"℃");
        rainfullT.setText(b[8]+b[9]);
        statusT.setText(b[3]);


        LinearLayout background = (LinearLayout) findViewById(R.id.today_relative);
        //RelativeLayout background = (RelativeLayout) findViewById(R.id.today_relative);
        Resources resources = this.getResources();
        Drawable qing = resources.getDrawable(R.drawable.biz_plugin_weather_qing);
        Drawable cloudy = resources.getDrawable(R.drawable.biz_plugin_weather_cloudy);
        Drawable yin = resources.getDrawable(R.drawable.biz_plugin_weather_yin);
        Drawable rain = resources.getDrawable(R.drawable.biz_plugin_weather_rain);

        Log.d("77777",b[3]);

        if(statusT.getText()!=null){
            Log.d("88888888",b[3]);
            switch (b[3]){
                case "晴時多雲":
                    background.setBackground(cloudy);
                    break;
                case "多雲":
                    background.setBackground(rain);
            }
        }
        Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
    }
    void updateTodayWeather_2(TodayWeather todayWeather_2)
    {

        c=todayWeather_2.toString_2();
        d = c.split(" ");
        for(int i=0;i<d.length;i++){
            System.out.println("array["+i+"] = "+d[i]);
        }



        timeT_2.setText("("+d[2]+")");
        temperatureT_2.setText("溫度:"+d[5]+d[6]+d[7]+"℃");
        rainfullT_2.setText(d[8]+d[9]);
        statusT_2.setText(d[3]);
        Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();
    }
    //獲取JSON資料
    class TransTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            try {
                URL url = new URL(params[0]);
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(url.openStream()));
                String line = in.readLine();
                while(line!=null){
                    //Log.d("HTTP", line);
                    sb.append(line);
                    line = in.readLine();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return sb.toString();
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //Log.d("JSON", s);
            parseJSON(s);
        }

    }
    //解析JSON
    private void parseJSON(String s) {
        try {
            JSONObject jsonObject = new JSONObject(s);
            JSONArray array = jsonObject.getJSONArray("results");
            JSONObject jsonObject1 = array.getJSONObject(0);
            JSONArray array1 = jsonObject1.getJSONArray("address_components");
            JSONObject jsonObject3 = array1.getJSONObject(4);
            String city = jsonObject3.getString("long_name");
            cityT.setText(city);
            if(cityT.getText().equals("基隆市")){
                getWeatherDatafromNet("03");
            }else if (cityT.getText().equals("臺北市"))
            {
                getWeatherDatafromNet("01");
            }else if (cityT.getText().equals("新北市"))
            {
                getWeatherDatafromNet("04");
            }else if (cityT.getText().equals("桃園市"))
            {
                getWeatherDatafromNet("05");
            }else if (cityT.getText().equals("新竹市"))
            {
                getWeatherDatafromNet("14");
            }else if (cityT.getText().equals("新竹縣"))
            {
                getWeatherDatafromNet("06");
            }else if (cityT.getText().equals("宜蘭縣"))
            {
                getWeatherDatafromNet("17");
            }else if (cityT.getText().equals("苗栗縣"))
            {
                getWeatherDatafromNet("07");
            }else if (cityT.getText().equals("臺中市"))
            {
                getWeatherDatafromNet("08");
            }else if (cityT.getText().equals("彰化縣"))
            {
                getWeatherDatafromNet("09");
            }else if (cityT.getText().equals("南投縣"))
            {
                getWeatherDatafromNet("10");
            }else if (cityT.getText().equals("雲林縣"))
            {
                getWeatherDatafromNet("11");
            }else if (cityT.getText().equals("嘉義市"))
            {
                getWeatherDatafromNet("16");
            }else if (cityT.getText().equals("嘉義縣"))
            {
                getWeatherDatafromNet("12");
            }else if (cityT.getText().equals("臺南市"))
            {
                getWeatherDatafromNet("13");
            }else if (cityT.getText().equals("高雄市"))
            {
                getWeatherDatafromNet("02");
            }else if (cityT.getText().equals("屏東縣"))
            {
                getWeatherDatafromNet("15");
            }else if (cityT.getText().equals("澎湖縣"))
            {
                getWeatherDatafromNet("20");
            }else if (cityT.getText().equals("花蓮縣"))
            {
                getWeatherDatafromNet("18");
            }else if (cityT.getText().equals("臺東縣"))
            {
                getWeatherDatafromNet("19");
            }else if (cityT.getText().equals("金門縣"))
            {
                getWeatherDatafromNet("21");
            }else if (cityT.getText().equals("連江縣")) {
                getWeatherDatafromNet("22");
            }
            else
                Log.d("TAG", "city:" + city);
        }

        catch(JSONException e) {
            e.printStackTrace();

        }
    }
    //
    protected synchronized void buildGoogleApiClient()
    {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
    @Override
    protected void onStart()
    {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onStop()
    {
        super.onStop();
        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
    }
    // 當 GoogleApiClient 連上 Google Play Service 後要執行的動作
    @Override
    public void onConnected(Bundle connectionHint)
    {
        // 這行指令在 IDE 會出現紅線，不過仍可正常執行，可不予理會
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null)
        {
            longitude = String.valueOf(mLastLocation.getLongitude());
            latitude = String.valueOf(mLastLocation.getLatitude());
            new TransTask().execute("http://maps.google.com/maps/api/geocode/json?latlng="+latitude+","+longitude+"&language=zh-CN&sensor=true");
            Log.d("Location",latitude+"~~"+longitude);
        }
        else
        {
            Toast.makeText(this, "偵測不到定位，請確認定位功能已開啟。", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult result)
    {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + result.getErrorCode());
    }
    @Override
    public void onConnectionSuspended(int cause)
    {
        Log.i(TAG, "Connection suspended");
        mGoogleApiClient.connect();
    }
}
