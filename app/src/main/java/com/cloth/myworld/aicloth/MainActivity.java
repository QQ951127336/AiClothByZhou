package com.cloth.myworld.aicloth;

import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapOptions;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements LocationSource {
    MapView mapView;
    private AMap aMap = null;
    private UiSettings mapUiSetting = null;
    public AMapLocationClient aMapLocationClient = null;
    //声明AMapLocationClinentOption类
    public AMapLocationClientOption aMapLocationClientOption = null;
    public AMapLocation motherLocation;
    public Marker motherMarker=null;
    public LatLng motherLatLng=null;
    private Double mlatitude=null;
    private Double mLongitude=null;
    private EditText latitudeInput;
    private EditText longitudeInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        latitudeInput=(EditText)findViewById(R.id.childLatitude);
        longitudeInput=(EditText)findViewById(R.id.childLongitude);



        if (aMap == null) {
            aMap = mapView.getMap();
//            if (AMapUtil.checkReady(this, aMap)) {
//                //这个方法是定位并且显示当前位置
//                setUpMap();
//            }
        }
        mapUiSetting = aMap.getUiSettings();
        mapUiSetting.setZoomControlsEnabled(true);//放大按钮有效
        mapUiSetting.setZoomPosition(AMapOptions.LOGO_POSITION_BOTTOM_RIGHT);//放大按钮位置点
        mapUiSetting.getZoomPosition();//放大按钮获得位置
        mapUiSetting.setCompassEnabled(true);
        mapUiSetting.setScrollGesturesEnabled(true);

        aMap.setLocationSource(this);//设置定位监听

        aMapLocationClient = new AMapLocationClient(getApplicationContext());
        aMapLocationClientOption = new AMapLocationClientOption();

        aMapLocationClient.setLocationListener(mLocationListener);

        aMapLocationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        aMapLocationClientOption.setInterval(1500);
        aMapLocationClientOption.setOnceLocation(false);//设置单次定位



        aMapLocationClient.setLocationOption(aMapLocationClientOption);

        aMapLocationClient.startLocation();



    }

    public AMapLocationListener mLocationListener =new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation !=null)
            {
                if (aMapLocation.getErrorCode() == 0) {

                    aMapLocation.getLocationType();
                    mlatitude = aMapLocation.getLatitude();
                    mLongitude = aMapLocation.getLongitude();
                    aMapLocation.getAccuracy();
                    if(aMapLocationClient !=null&&mlatitude!=null&&mLongitude!=null)
                    {
                        try {
                            new Thread(new Runnable() {
                                @Override
                                public void run() {
                        LatLng motherLatLng = new LatLng(mlatitude, mLongitude);
                                    aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(motherLatLng, 19));//移动到坐标点
                                    aMap.addMarker(new MarkerOptions().
                                position(motherLatLng).
                                title("Mother").
                                snippet("MotherMarker").visible(true));



                                }
                            }).start();
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }

                    }
                    else
                    {
                        Log.d("LatLon","can not input");
                    }
                }
                else {
                    Log.e("aMapLocation", "location Error,ErrorCode"
                            + aMapLocation.getErrorCode() + ",errInfo"
                            + aMapLocation.getErrorInfo());
                }

                }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
        aMapLocationClient.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();

    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {

    }



    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    @Override
    public void onStop() {
        super.onStop();


    }
    private void childMarke(Double cLatitude,Double cLongtitude)

    {

        MarkerOptions cmarkerOptions = null;
        cmarkerOptions.position(new LatLng(cLatitude,cLongtitude));
        cmarkerOptions.title("Child");
        cmarkerOptions.visible(true);

        aMap.addMarker(cmarkerOptions);
    }
}
