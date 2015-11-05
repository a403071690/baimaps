package com.example.baimaps;

import java.util.ArrayList;
import java.util.List;

import android.app.Application;
import android.util.Log;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.Poi;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

public class MyApp extends Application {
	
	public static final String TAG = "MyApp";
	public LocationClient mLocationClient;
	public TextView tv_result;
	public MyLocationListenner myListener = new MyLocationListenner();
	//------------经纬度-----
	public double latitude;
	public double lontitude;
	
	//------------------baidu地图view
	public MapView map_view;//地體View
	public BaiduMap  mBaiduMap;//百度地圖對象
	@Override
	public void onCreate() {
		mLocationClient = new LocationClient(this);
		mLocationClient.registerLocationListener(myListener);//註冊監聽器
	}
	
	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {

		@Override
		public void onReceiveLocation(BDLocation location) {
	        //Receive Location
	        StringBuffer sb = new StringBuffer(256);
	        sb.append("time : ");
	        sb.append(location.getTime());
	        sb.append("\nerror code : ");
	        sb.append(location.getLocType());
	        sb.append("\nlatitude : ");
	        sb.append(location.getLatitude());
	        sb.append("\nlontitude : ");
	        sb.append(location.getLongitude());
	        sb.append("\nradius : ");
	        sb.append(location.getRadius());
	        latitude = location.getLatitude();
	        lontitude = location.getLongitude();
	        if (location.getLocType() == BDLocation.TypeGpsLocation){// GPS定位结果
	            sb.append("\nspeed : ");
	            sb.append(location.getSpeed());// 单位：公里每小时
	            sb.append("\nsatellite : ");
	            sb.append(location.getSatelliteNumber());
	            sb.append("\nheight : ");
	            sb.append(location.getAltitude());// 单位：米
	            sb.append("\ndirection : ");
	            sb.append(location.getDirection());// 单位度
	            sb.append("\naddr : ");
	            sb.append(location.getAddrStr());
	            sb.append("\ndescribe : ");
	            sb.append("gps定位成功");

	        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation){// 网络定位结果
	            sb.append("\naddr : ");
	            sb.append(location.getAddrStr());
	            //运营商信息
	            sb.append("\noperationers : ");
	            sb.append(location.getOperators());
	            sb.append("\ndescribe : ");
	            sb.append("网络定位成功");
	        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
	            sb.append("\ndescribe : ");
	            sb.append("离线定位成功，离线定位结果也是有效的");
	        } else if (location.getLocType() == BDLocation.TypeServerError) {
	            sb.append("\ndescribe : ");
	            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
	        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
	            sb.append("\ndescribe : ");
	            sb.append("网络不同导致定位失败，请检查网络是否通畅");
	        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
	            sb.append("\ndescribe : ");
	            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
	        }
	sb.append("\nlocationdescribe : ");
	            sb.append(location.getLocationDescribe());// 位置语义化信息
	            List<Poi> list = location.getPoiList();// POI数据
	            if (list != null) {
	                sb.append("\npoilist size = : ");
	                sb.append(list.size());
	                for (Poi p : list) {
	                    sb.append("\npoi= : ");
	                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
	                }
	            }
	            
				  
	        setAddress(sb.toString());
				
	        Log.i("BaiduLocationApiDem", sb.toString());
	    }

		 
	}
	
	private void setAddress(String text) {
		if(text!=null){
			if(tv_result!=null){
				tv_result.setText(text);
			}
		}
	}

}