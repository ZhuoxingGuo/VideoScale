package com.example.servertest;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.example.servertest.imageview.CircularImage;
import com.yunos.account.client.YunosAccountClient;
import com.yunos.account.client.bo.OAuthPairBo;
import com.yunos.account.client.exception.OAuthException;

public class MainActivity extends Activity {
	File cache;
	private ImageView image;
	private AsyncImageTask imageTask;
	private String Uri = "http://wwc.taobaocdn.com/avatar/getAvatar.do?userId=1646899639&width=80&height=80&type=sns";
	private String Uri_2 = "http://wwc.taobaocdn.com/image/getUserImage.do?userId=38745469&width=80&height=80&type=sns";
	private String Uri_nick = "http://wwc.taobaocdn.com/avatar/getAvatar.do?userNick=����&width=80&height=80&type=sns";
	private String Uri_nick_2 = "http://wwc.taobaocdn.com/wangwang/headshow.htm?longId=";
	String str = "����";

	private String xiaocheng = "1646899639";
	private String guozhuoxing = "38745469";
	private String laoliao = "1875156188";
	private String laoliao_nick = "edgraliao";
	private Context mContext;
	private File file;
	private String test = "759019448";
	String newStr;
	String Uri_nick_3;
	String enUft;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		image = (ImageView) findViewById(R.id.image);
		file  = getFilesDir();
		
		try { 

			enUft = URLEncoder.encode("testvim05", "GBK");
			Uri_nick_2 = Uri_nick_2 + enUft;

			} catch (Exception e) { 
			e.printStackTrace(); 
			} 
//		try {
//			Uri_nick_3 = "http://wwc.taobaocdn.com/wangwang/headshow.htm?longId=����";
//			String str = "����";
//			newStr = new String(str.getBytes(), "GBK");
//			Uri_nick_3 = Uri_nick_3 + newStr;
//		} catch (UnsupportedEncodingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		CircularImage cover_user_photo = (CircularImage) findViewById(R.id.cover_user_photo);
		// cover_user_photo.setImageResource(R.drawable.face);
		imageTask = new AsyncImageTask(cover_user_photo, "112312312");
		imageTask.execute(Uri_nick_2);

		// String str="synctest1001";
		// boolean result=str.matches("[0-9]+");
		// if (result == true) {
		// Log.i("Willguo", "ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½");}else{Log.i("Willguo",
		// "ï¿½ï¿½ï¿½ï¿½ï¿½ï¿½");}

		//
		// new Thread(downloadRun).start();

	}

	class AsyncImageTask extends AsyncTask<String, Integer, Uri> {
		private ImageView imageView;
		private String filename;

		public AsyncImageTask(ImageView imageView, String name) {
			this.imageView = imageView;
			filename = name;
			cache = file;
			if (!cache.exists())
				cache.mkdirs();
				Log.e("Willguo","cache " +cache);
		}

		protected Uri doInBackground(String... params) {

			try {
				return HttpUtils.getImage(params[0], cache, filename);
			} catch (Exception e) {
				Log.e("Willguo", "Exception " + e);
				e.printStackTrace();
			}
			return null;
		}

		protected void onPostExecute(Uri result) {
			if (result != null && imageView != null)
				imageView.setImageURI(result);
			imageView.setScaleType(ScaleType.CENTER_CROP);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	Runnable downloadRun = new Runnable() {

		@Override
		public void run() {
			Del.doDel("3601783515");
			// test();
		}
	};

	public void test() {
		try {
			YunosAccountClient.subSystemInit(this.getBaseContext());
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			YunosAccountClient client = new YunosAccountClient(
					"https://account.yunos.com/openapi",
					"29224e59c0aacd83d15819e942ff29d6",
					"f605347c9b6cea4bab8d4e3dda5da6ae");
			ArrayList<OAuthPairBo> params = new ArrayList<OAuthPairBo>();
			params.add(new OAuthPairBo("taobaoNick", "testvim02"));
			// params.add(new OAuthPairBo("taobaoNick", "3601783521"));
			// String resp = client.callApi("account.get_kps_by_taobaonicks",
			// params);
			String resp2 = client.callApi(
					"account.get_userIdAndKp_by_taobaoNick", params);
			// String resp3 = client.callApi("account.get_kp_by_userId",
			// params);
			JSONObject json = new JSONObject(resp2);
			int statusCode = json.getInt("status");
			Log.e("Willguo", "statusCode " + statusCode);
			if (statusCode == 200) {
				Log.w("Willguo", "json " + json.toString());
				// Log.i("Willguo", "json 111111 " + json.getJSONArray("data"));

			}
		} catch (OAuthException e) {
			Log.e("Willguo", "  " + e.toString());
			e.printStackTrace();
		} catch (JSONException jsonException) {

			jsonException.printStackTrace();
		}
	}

	public void getImage() {

	}

}
