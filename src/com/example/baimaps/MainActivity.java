package com.example.baimaps;

import java.util.ArrayList;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.LocationClientOption.LocationMode;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

//假如用到位置提醒功能，需要import该类
public class MainActivity extends Activity implements OnClickListener{
	private Button bt_getlocat ;
	private LocationClient mLocationClient;//功能：定位SDK的核心类
	private TextView tv_result;//地址信息顯示view
	private MapView map_view;//地體View
	private BaiduMap  mBaiduMap;//百度地圖對象
	
    private Handler handler =new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		switch (msg.what) {
			case 1:
				Toast.makeText(MainActivity.this, "返回碼："+msg.arg1, 0).show();
				break;

			default:
				break;
			}
    	}
    	
    };
	
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());  
		setContentView(R.layout.activity_main);
		findViewById();//元素獲取佈局
		mLocationClient = ((MyApp) getApplication()).mLocationClient;//獲取appliction對象
		tv_result.setMovementMethod(new ScrollingMovementMethod());
		 
		((MyApp) getApplication()).tv_result = tv_result;
		mBaiduMap = ((MyApp) getApplication()).mBaiduMap;
		setListener();
		
		 //普通地图 
        mBaiduMap = map_view.getMap();  
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);  
		 
        

		 
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.bt_getlocat:
			 initLocation();//初始化參數
			 mLocationClient.start(); 
			 int code =  mLocationClient.requestLocation(); 
			  
		        //地图上打点
				 //定义Maker坐标点  
			        LatLng point = new LatLng(((MyApp) getApplication()).latitude, ((MyApp) getApplication()).lontitude);  
			        //构建Marker图标  
			        //BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka);  
			        BitmapDescriptor bitmap2 = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka1);  
			        BitmapDescriptor bitmap3 = BitmapDescriptorFactory.fromResource(R.drawable.icon_marka2);  
			        
			     // 通过marker的icons设置一组图片，再通过period设置多少帧刷新一次图片资源
			        ArrayList<BitmapDescriptor> giflist = new ArrayList<BitmapDescriptor>();
			        //giflist.add(bitmap);
			        giflist.add(bitmap2);
			        giflist.add(bitmap3);
			        OverlayOptions ooD = new MarkerOptions().position(point).icons(giflist)
			       				.zIndex(0).period(10);	
			        Marker mMarkerD = (Marker) (mBaiduMap.addOverlay(ooD));
			 
			 Message message = new Message();
			 message.what=1;
			 message.arg1 = code;
			 handler.sendMessage(message); 
			break;

		default:
			break;
		}
	}
	
	private void setListener() {
		bt_getlocat.setOnClickListener(this); 
	}
	
	public void findViewById(){
		  bt_getlocat = (Button) findViewById(R.id.bt_getlocat);
		  tv_result = (TextView) findViewById(R.id.tv_result);
		  map_view= (MapView) findViewById(R.id.map_view);
	}
	
	
	private void initLocation(){
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationMode.Hight_Accuracy
);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	protected void onStop() {
		mLocationClient.stop();
		super.onStop();
	}

	
}
