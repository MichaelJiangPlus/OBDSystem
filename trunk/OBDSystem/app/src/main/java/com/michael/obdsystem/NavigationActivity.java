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
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeOption;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.IndoorRouteResult;
import com.baidu.mapapi.search.route.MassTransitRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.michael.obdsystem.model.Point;
import com.michael.obdsystem.util.CoordinateConversion;

import java.util.List;

/**
 * Created by Michael on 2016/12/24 0024.
 */

public class NavigationActivity extends Activity  implements BaiduMap.OnMapClickListener,OnGetRoutePlanResultListener, OnGetGeoCoderResultListener {
    /*******浏览路线节点相关*******/
    // 浏览路线节点相关
    int nodeIndex = -1; // 节点索引,供浏览节点时使用
    RouteLine route = null;
    OverlayManager routeOverlay = null;
    boolean useDefaultIcon = false;
    // 地图相关，使用继承MapView的MyRouteMapView目的是重写touch事件实现泡泡处理
    // 如果不处理touch事件，则无需继承，直接使用MapView即可
    MapView mMapView = null;    // 地图View
    BaiduMap mBaidumap = null;
    // 搜索相关
    RoutePlanSearch mSearch = null;    // 搜索模块，也可去掉地图模块独立使用
    DrivingRouteResult nowResultdrive  = null;
    int nowSearchType = -1 ; // 当前进行的检索，供判断浏览节点时结果使用。
    String startNodeStr = "";
    String endNodeStr = "";

    /*******位置信息转换相关*******/
    GeoCoder geoCoderSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private String city = "";
    private String address = "";

    /*******GPS相关*******/
    private LocationManager locationManager;
    private String provider;
    Location myPoint = null;
    LatLng latLngpoint = null;

    /*******Android相关*******/
    private EditText edt_search ;





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_navigation);
        CharSequence titleLable = "路线规划功能";
        setTitle(titleLable);
        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaidumap = mMapView.getMap();
        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
        geoCoderSearch = GeoCoder.newInstance();
        geoCoderSearch.setOnGetGeoCodeResultListener(this);
        edt_search = (EditText)findViewById(R.id.edt_search);
        GPSReceiver();
    }


    /**
     * 发起路线规划搜索示例
     *
     * @param v
     */
    public void searchButtonProcess(View v) {
        // 重置浏览节点的路线数据
        route = null;
        mBaidumap.clear();
        // 处理搜索按钮响应
        // 设置起终点信息，对于tranist search 来说，城市名无意义
        startNodeStr = address;
        endNodeStr = edt_search.getText().toString();
        PlanNode stNode = PlanNode.withLocation(latLngpoint);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, endNodeStr);

        // 实际使用中请对起点终点城市进行正确的设定
        if (v.getId() == R.id.btn_search) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
            nowSearchType = 1;
        }
    }

    public void LatLon2City(LatLng ptCenter){
        geoCoderSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));
    }

    public void City2LatLon(String city,String address){
        geoCoderSearch.geocode(new GeoCodeOption().city(city).address(address));
    }


    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult result) {
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
        }
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            result.getSuggestAddrInfo();
            return;
        }
        if (result.error == SearchResult.ERRORNO.NO_ERROR) {
            nodeIndex = -1;
            if (result.getRouteLines().size() > 1 ) {
                nowResultdrive = result;

                //寻找红绿灯最少的那个路线
                int num = 0;int count = 0;
                for(int i = 0;i < result.getRouteLines().size();i++){
                    if(result.getRouteLines().get(i).getLightNum()>= num){
                        count = i;
                    }
                }
                //选择红绿灯最少的那个
                route = nowResultdrive.getRouteLines().get(count);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);
                routeOverlay = overlay;
                overlay.setData(nowResultdrive.getRouteLines().get(count));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else if ( result.getRouteLines().size() == 1 ) {
                //只有一个直接显示路线
                route = result.getRouteLines().get(0);
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);
                overlay.setData(result.getRouteLines().get(0));
                overlay.addToMap();
                overlay.zoomToSpan();
            } else {
                Log.d("route result", "结果数<0" );
                return;
            }

        }
    }


    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        //根据给定的地址名称计算相应的经纬度信息
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(NavigationActivity.this, strInfo, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        //根据给定的经纬度计算地理位置
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        city = result.getAddressDetail().city;
        address = "浙江大学城市学院";
        Toast.makeText(NavigationActivity.this, result.getAddress(), Toast.LENGTH_LONG).show();
    }

    // 定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            }
            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
            }
            return null;
        }
    }

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        if (locationManager != null) {
            //关闭程序时将监听器移除
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
        super.onDestroy();
    }

    /*****GPS系列*****/
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

    public void showLocation(Location point) {
        String result = "lat: " + point.getLatitude() + "  " + "lon: " + point.getLongitude();
        //接受GPS并且转换称百度坐标
        Point myPoint= CoordinateConversion.wgs_gcj_encrypts(point.getLatitude(), point.getLongitude());
        myPoint = CoordinateConversion.google_bd_encrypt(myPoint.getLat(), myPoint.getLng());
        String currentPosition = "latitude is " + myPoint.getLat() + "\n"
                + "longitude is " +  myPoint.getLng();
        latLngpoint = new LatLng(myPoint.getLat() ,myPoint.getLng());
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_marka);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(latLngpoint)
                .icon(bitmap)
                .zIndex(18);
        //在地图上添加Marker，并显示
        mBaidumap.addOverlay(option);
        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(latLngpoint, 18);
        mBaidumap.setMapStatus(mapStatus);
        LatLon2City(latLngpoint);
    }

    /*****没用系列*****/
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetMassTransitRouteResult(MassTransitRouteResult massTransitRouteResult) {

    }

    @Override
    public void onGetIndoorRouteResult(IndoorRouteResult indoorRouteResult) {
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
    }
    @Override
    public void onMapClick(LatLng point) {
    }


}
