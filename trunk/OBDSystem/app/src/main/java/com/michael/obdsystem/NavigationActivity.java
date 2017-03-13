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
import android.view.Window;
import android.view.WindowManager;
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
    // 浏览路线节
    // 点相关
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
    DrivingRouteResult nowResultdrive = null;
    int nowSearchType = -1; // 当前进行的检索，供判断浏览节点时结果使用。
    String startNodeStr = "";
    String endNodeStr = "";

    /*******位置信息转换相关*******/
    GeoCoder geoCoderSearch = null; // 搜索模块，也可去掉地图模块独立使用
    private String city = "";
    private String address = "";

    /*******GPS相关*******/
    private LocationManager locationManager;
    Location myPoint = null;
    LatLng latLngpoint = null;

    /*******Android相关*******/
    private EditText edt_search;
    private boolean isLead = false;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //加载百度SDK
        SDKInitializer.initialize(getApplicationContext());

        setContentView(R.layout.activity_navigation);

        // 初始化地图
        mMapView = (MapView) findViewById(R.id.bmapView);
        mBaidumap = mMapView.getMap();
        // 地图点击事件处理
        mBaidumap.setOnMapClickListener(this);
        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);

        //初始化地理位置解析模块
        geoCoderSearch = GeoCoder.newInstance();
        geoCoderSearch.setOnGetGeoCodeResultListener(this);

        //初始化目的地输入框
        edt_search = (EditText) findViewById(R.id.edt_search);

        //注册GPS监听器，接受GPS变化
        GPSReceiver();

    }

    /*****GPS系列*****/
    /**
     * GPS变化的时候需要做什么事情，GOS监听类
     */
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
//            if (ActivityCompat.checkSelfPermission(NavigationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//                // TODO: Consider calling
//                //    ActivityCompat#requestPermissions
//                // here to request the missing permissions, and then overriding
//                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//                //                                          int[] grantResults)
//                // to handle the case where the user grants the permission. See the documentation
//                // for ActivityCompat#requestPermissions for more details.
//                return;
//            }
//            locationManager.removeUpdates(locationListener);
//            GPSReceiver();
        }
    };

    /**
     * 选择如果接受到Gps信息的方式
     */
    public void GPSReceiver() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //大致意思就是在这里写没有权限的话会怎么样
            Toast.makeText(NavigationActivity.this, "没有权限", Toast.LENGTH_LONG);
            return;
        } else {
            //获取所有可用的位置提供器
            List<String> providerList = locationManager.getProviders(true);
            String provider = "";//provider 位置提供器

            if (providerList.contains(LocationManager.GPS_PROVIDER)) {
                provider = LocationManager.GPS_PROVIDER;
            } else if (providerList.contains(LocationManager.NETWORK_PROVIDER)) {
                provider = LocationManager.NETWORK_PROVIDER;
            } else {
                //当没有可用的位置提供器时，弹出Toast提示用户
                Toast.makeText(this, "No location provider to use", Toast.LENGTH_SHORT).show();
                return;
            }
            myPoint = locationManager.getLastKnownLocation(provider);
            Log.d("result",provider);


            if (myPoint != null) {
                //显示当前设备的位置信息
                this.showLocation(myPoint);
            }

            //provider为位置提供器，2000为毫秒数=5秒，1为一米，locationlistener为要做的事情清单。
            //位置提供器，如：GPS，NetWork。
            locationManager.requestLocationUpdates(provider, 2000, 10, locationListener);
        }
    }

    /**
     * GPS信息改变的时候要做什么
     */
    public void showLocation(Location point) {
        //接受GPS信息并且转换成百度坐标
        Point myPoint= CoordinateConversion.wgs_gcj_encrypts(point.getLatitude(), point.getLongitude());
        myPoint = CoordinateConversion.google_bd_encrypt(myPoint.getLat(), myPoint.getLng());

        //两者经纬度有偏差，所以把自己获取的经纬度转换为百度的经纬度
        latLngpoint = new LatLng(myPoint.getLat() ,myPoint.getLng());

//        latLngpoint = new LatLng(point.getLatitude(),point.getLongitude());
        //构建标记图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory
                .fromResource(R.drawable.icon_gcoding);

        //构建标记选项，用于在地图上添加标记
        OverlayOptions option = new MarkerOptions()
                .position(latLngpoint)
                .icon(bitmap)
                .zIndex(18);

        if(!isLead){
            mBaidumap.clear();
        }

        //在地图上添加标记，并显示
        mBaidumap.addOverlay(option);


        //更新位置
        MapStatusUpdate mapStatus = MapStatusUpdateFactory.newLatLngZoom(latLngpoint, 18);
        mBaidumap.setMapStatus(mapStatus);

        //根据经纬度获取目标地址所在城市和街道等
        LatLon2City(latLngpoint);
    }


    /**
     * 根据经纬度获取目标地址所在城市和街道等
     */
    public void LatLon2City(LatLng ptCenter){
        //根据经纬度获取目标地址所在城市和街道等
        geoCoderSearch.reverseGeoCode(new ReverseGeoCodeOption()
                .location(ptCenter));
    }


    /**
     * 根据给定的经纬度计算地理位置
     */
    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult result) {
        //根据给定的经纬度计算地理位置
        //判断能不能找的到
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        //获取找到的城市信息
        city = result.getAddressDetail().city;
        //显示
        Toast.makeText(NavigationActivity.this, result.getAddress(), Toast.LENGTH_LONG).show();
    }


    /**
     * 发起路线规划搜索
     */
    // 处理搜索按钮响应
    public void searchButtonProcess(View v) {
        // 重置浏览节点的路线数据
        route = null;
        mBaidumap.clear();

        // 设置起终点信息
        endNodeStr = edt_search.getText().toString();
        PlanNode stNode = PlanNode.withLocation(latLngpoint);
        PlanNode enNode = PlanNode.withCityNameAndPlaceName(city, endNodeStr);

        //是否点击搜索按钮
        if (v.getId() == R.id.btn_search) {
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).to(enNode));
            nowSearchType = 1;
        }
    }

    /**
     * 当获得行车记录路线后显示
     */
    @Override
    //获得驾驶路线结果
    public void onGetDrivingRouteResult(DrivingRouteResult result) {

        //判断能不能找的到该地址
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigationActivity.this, "抱歉，未找到结果", Toast.LENGTH_SHORT).show();
            isLead = false;
        }
        // 起终点或途经点地址有岐义，通过以下接口获取建议查询信息
        if (result.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            result.getSuggestAddrInfo();
            isLead = false;
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

                //在地图上显示路线
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
                mBaidumap.setOnMarkerClickListener(overlay);//设置百度地图的活动点监听
                routeOverlay = overlay;
                overlay.setData(nowResultdrive.getRouteLines().get(count));//在地图上要显示的点
                overlay.addToMap();//把这些点加进Baidu地图
                overlay.zoomToSpan();//自动缩放，能够看到所有的点
            } else if ( result.getRouteLines().size() == 1 ) {
                //只有一个直接显示路线
                route = result.getRouteLines().get(0);
                //在地图上显示路线
                DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(mBaidumap);
                routeOverlay = overlay;
                mBaidumap.setOnMarkerClickListener(overlay);//设置百度地图的活动点监听
                overlay.setData(result.getRouteLines().get(0));//在地图上要显示的点
                overlay.addToMap();//把这些点加进Baidu地图
                overlay.zoomToSpan();//自动缩放，能够看到所有的点
            } else {
                Log.d("route result", "结果数<0" );
                return;
            }
            isLead = true;
        }
    }


    /**
     * 定制路线，行车路线的开始和结束都用什么标志
     */
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {
        MyDrivingRouteOverlay(BaiduMap baiduMap) {
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

    /*****系统生命周期*****/

    @Override
    protected void onPause() {
        mMapView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mMapView.onResume();
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        else {
            if(locationManager!=null){
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 2000, 10, locationListener);
            }
        }
    }

    @Override
    //关闭APP时，把相关服务销毁
    protected void onDestroy() {
        if (mSearch != null) {
            mSearch.destroy();
        }
        mMapView.onDestroy();
        //关闭程序时将监听器移除
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                    PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            locationManager.removeUpdates(locationListener);
        }
        super.onDestroy();
    }

    /*****没用系列*****/

    @Override
    public boolean onMapPoiClick(MapPoi poi) {
        return false;
    }

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

    /**
     * 根据给定的地址名称计算相应的经纬度信息
     */
    @Override
    public void onGetGeoCodeResult(GeoCodeResult result) {
        //根据给定的地址名称计算相应的经纬度信息
        //判断能不能找的到
        if (result == null || result.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(NavigationActivity.this, "抱歉，未能找到结果", Toast.LENGTH_LONG)
                    .show();
            return;
        }
        String strInfo = String.format("纬度：%f 经度：%f",
                result.getLocation().latitude, result.getLocation().longitude);
        Toast.makeText(NavigationActivity.this, strInfo, Toast.LENGTH_LONG).show();
    }

    /**
     * 更具目标地址获取所在的经纬度信息
     */
    public void City2LatLon(String city,String address){
        //更具目标地址获取所在的经纬度信息
        geoCoderSearch.geocode(new GeoCodeOption().city(city).address(address));
    }
}
