package com.example.blue_bell.weather;

/**
 * Created by Blue_bell on 2018/5/10.
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

//繼承android.app.Service
public class NickyService extends Service {
    String a;
    String[]b;
    private Handler handler = new Handler();

    TodayWeather todayWeather = null;
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
    private void getWeatherDatafromNet()
    {
        final String address = "https://www.cwb.gov.tw/rss/Data/cwb_warning.xml";
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
                        Log.d("date from url",str);
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
                            if(xmlPullParser.getName().equals("title") && titleCount==2)
                            {
                                eventType = xmlPullParser.next();
                                todayWeather.setNotice(xmlPullParser.getText());
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
    void updateTodayWeather(TodayWeather todayWeather)
    {
        a=todayWeather.toString();
        b = a.split(" ");
        for(int i=0;i<b.length;i++){
            System.out.println("array["+i+"] = "+b[i]);
            Log.d("5555555",b[i]);
        }
        //日期=b[0]        時間=b[1]        事件=b[2]
        Log.d("6666666",b[2]);

    }
    private void notice(TodayWeather todayWeather){
        NotificationManager mNotificationManager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);

        Intent notifyIntent = new Intent(NickyService.this, MainActivity.class);
        notifyIntent.setFlags( Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent appIntent = PendingIntent.getActivity(NickyService.this, 0, notifyIntent, 0);
        a=todayWeather.toString();
        b = a.split(" ");
        for(int i=0;i<b.length;i++){
            System.out.println("array["+i+"] = "+b[i]);
            Log.d("5555555",b[i]);
        }
        Notification notification
                = new Notification.Builder(NickyService.this)
                .setContentIntent(appIntent)
                //.setSmallIcon(R.drawable.ic_launcher)
                //.setLargeIcon(BitmapFactory.decodeResource(MainActivity.this.getResources(), R.drawable.ic_launcher))
                .setTicker("警報")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
                .setContentTitle("警報")
                .setContentText(a)
                .setOngoing(true)
                .setDefaults(Notification.DEFAULT_ALL)
                //.setVibrate(vibrate)
                .build();

        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags = Notification.FLAG_NO_CLEAR;
        notification.flags = Notification.FLAG_SHOW_LIGHTS;
        notification.flags = Notification.FLAG_INSISTENT;
        mNotificationManager.notify(0, notification);
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        handler.postDelayed(showTime, 1000);
        super.onStart(intent, startId);
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(showTime);
        super.onDestroy();
    }

    private Runnable showTime = new Runnable() {
        public void run() {
            getWeatherDatafromNet();
            notice(TodayWeather tod);
            handler.postDelayed(this, 60000);
        }
    };
}
