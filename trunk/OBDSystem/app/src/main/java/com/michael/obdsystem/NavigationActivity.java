package com.michael.obdsystem;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;
import android.widget.Toast;

import com.michael.obdsystem.model.Point;
import com.michael.obdsystem.util.CoordinateConversion;

import java.util.List;

/**
 * Created by Michael on 2016/12/24 0024.
 */

public class NavigationActivity extends Activity {
    private TextView positionTextView;
    private LocationManager locationManager;
    private String provider;
    Location myPoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        positionTextView = (TextView) findViewById(R.id.txt_gps);
        GPSReceiver();
    }


    public void showLocation(Location point) {
        String result = "lat: " + point.getLatitude() + "  " + "lon: " + point.getLongitude();
        //接受GPS并且转换称百度坐标
        Point myPoint= CoordinateConversion.wgs_gcj_encrypts(point.getLatitude(), point.getLongitude());
        myPoint = CoordinateConversion.google_bd_encrypt(myPoint.getLat(), myPoint.getLng());


//        double lat = 30.3287750;//纬度
//        double lon = 120.149800;//经度
//        Point myPoint= CoordinateConversion.wgs_gcj_encrypts(lat, lon);
//        myPoint = CoordinateConversion.google_bd_encrypt(myPoint.getLat(), myPoint.getLng());

        String currentPosition = "latitude is " + myPoint.getLat() + "\n"
                + "longitude is " +  myPoint.getLng();
        positionTextView.setText(currentPosition);

    }


    public void GPSReceiver() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //大致意思就是在这里写没有权限的话会怎么样
            Toast.makeText(NavigationActivity.this, "没有权限", Toast.LENGTH_LONG);
            return;
        } else {
            //获取所有可用的位置提供器
            List<String> providerList = locationManager.getProviders(true);
            provider = LocationManager.NETWORK_PROVIDER;

            if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                provider = LocationManager.NETWORK_PROVIDER;
            } else {
                //当没有可用的位置提供器时，弹出Toast提示用户
                Toast.makeText(NavigationActivity.this, "没有定位服务", Toast.LENGTH_LONG).show();
                return;
            }
            myPoint = locationManager.getLastKnownLocation(locationManager.NETWORK_PROVIDER);

            if (myPoint != null) {
                //显示当前设备的位置信息
                this.showLocation(myPoint);
            }

            //provider为位置提供器，5000为毫秒数=5秒，1为一米，locationlistener为要做的事情清单。
            //位置提供器，如：GPS，NetWork。
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 1, locationListener);
        }
    }



    protected void onDestroy() {
        super.onDestroy();
        if (locationManager != null) {
            //关闭程序时将监听器移除
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
    }


    LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //更新当前设备的位置信息
            showLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }
    };



}
