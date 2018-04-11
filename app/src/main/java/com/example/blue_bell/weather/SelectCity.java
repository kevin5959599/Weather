package com.example.blue_bell.weather;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Blue_bell on 2018/2/23.
 */

public class SelectCity extends Activity implements View.OnClickListener {
    private TextView city;
    private ImageView backBtn;
    private ListView cityListLv;
    private String updatecitycode = "-1";
    private String cityname;
    //選擇城市
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_city);

        backBtn = (ImageView)findViewById(R.id.title_selectCity_back);
        backBtn.setOnClickListener(this);


        final String[] listData = {"基隆市","臺北市","新北市","桃園市","新竹市","新竹縣","宜蘭縣","苗栗縣","臺中市","彰化縣","南投縣","雲林縣","嘉義市","嘉義縣","臺南市","高雄市","屏東縣","澎湖縣","花蓮縣","臺東縣","金門縣","連江縣"};
        cityListLv = (ListView)findViewById(R.id.selectcity_lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(SelectCity.this,android.R.layout.simple_list_item_1,listData);
        cityListLv.setAdapter(adapter);

        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                String text = listData[position];
                //String result = "索引值:" + position + "\n" + "內容" + text;
                String result = "已選擇:"+text;
                Toast.makeText(SelectCity.this, result,Toast.LENGTH_SHORT).show();

                if(text=="基隆市"){
                    updatecitycode = "03";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:基隆市");
                    cityname = text;
                }else if(text=="臺北市"){
                    updatecitycode = "01";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺北市");
                    cityname = text;
                }else if(text=="新北市"){
                    updatecitycode = "04";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新北市");
                    cityname = text;
                }else if(text=="桃園市"){
                    updatecitycode = "05";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:桃園市");
                    cityname = text;
                }else if(text=="新竹市"){
                    updatecitycode = "14";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新竹市");
                    cityname = text;
                }else if(text=="新竹縣"){
                    updatecitycode = "06";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:新竹縣");
                    cityname = text;
                }else if(text=="宜蘭縣"){
                    updatecitycode = "17";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:宜蘭縣");
                    cityname = text;
                }else if(text=="苗栗縣"){
                    updatecitycode = "07";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:苗栗縣");
                    cityname = text;
                }else if(text=="臺中市"){
                    updatecitycode = "08";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺中市");
                    cityname = text;
                }else if(text=="彰化縣"){
                    updatecitycode = "09";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:彰化縣");
                    cityname = text;
                }else if(text=="南投縣"){
                    updatecitycode = "10";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:南投縣");
                    cityname = text;
                }else if(text=="雲林縣"){
                    updatecitycode = "11";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:雲林縣");
                    cityname = text;
                }else if(text=="嘉義市"){
                    updatecitycode = "16";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:嘉義市");
                    cityname = text;
                }else if(text=="嘉義縣"){
                    updatecitycode = "12";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:嘉義縣");
                    cityname = text;
                }else if(text=="臺南市"){
                    updatecitycode = "13";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺南市");
                    cityname = text;
                }else if(text=="高雄市"){
                    updatecitycode = "02";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:高雄市");
                    cityname = text;
                }else if(text=="屏東縣"){
                    updatecitycode = "15";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:屏東縣");
                    cityname = text;
                }else if(text=="澎湖縣"){
                    updatecitycode = "20";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:澎湖縣");
                    cityname = text;
                }else if(text=="花蓮縣"){
                    updatecitycode = "18";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:花蓮縣");
                    cityname = text;
                }else if(text=="臺東縣"){
                    updatecitycode = "19";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:臺東縣");
                    cityname = text;
                }else if(text=="金門縣"){
                    updatecitycode = "21";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:金門縣");
                    cityname = text;
                }else if(text=="連江縣"){
                    updatecitycode = "22";
                    city = (TextView)findViewById(R.id.title_selectCity_name);
                    city.setText("當前城市:連江縣");
                    cityname = text;
                }
            }
        };
        cityListLv.setOnItemClickListener(itemClickListener);
        }
    //把citycode值傳到MainActivity
    @Override
    public void onClick(View v){
        switch (v.getId())
        {
            case R.id.title_selectCity_back:
               // finish();
                Intent intent = new Intent(this,MainActivity.class);
                if(updatecitycode!="-1") {
                    intent.putExtra("citycode", updatecitycode);
                    intent.putExtra("city", cityname);
                }
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
