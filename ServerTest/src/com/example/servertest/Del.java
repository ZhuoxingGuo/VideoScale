package com.example.servertest;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Del {
	static public void doDel(String number) {
		String encoder = 
				"http://10.69.17.106:8080/mmpus/get_mmp_nick?account_type=mmpno&taobao_id="
						+number;
		
		HttpInvoker invoker = new HttpInvoker();
		String ResponseJson = invoker.doGet(encoder);
		JSONObject json;
		try {
			json = new JSONObject(ResponseJson);
			int statusCode = json.getInt("status"); 
			Log.i("Willguo", "get Nickname  statusCode " + statusCode);
			if (statusCode == 200) {

				Log.e("Willguo", "json   222 " + json.getJSONArray("data").getJSONObject(0).getString("nick"));
						
				
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}


	}
}
