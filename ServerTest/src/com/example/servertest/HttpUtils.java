package com.example.servertest;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.net.Uri;
import android.util.Log;

public class HttpUtils {
	
	public static final String TAG = "cn.nubia.xingxingsheshe.utils.HttpUtils";

	public static final int BUFFER_SIZE = 1024;
	public static final String FILEDATA = "FileData";

	public InputStream post(String actionUrl, Map<String, String> params) {
		InputStream inputStream = null;
		try {
			DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpResponse response;
			HttpPost httpost = new HttpPost(actionUrl);
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (Map.Entry<String, String> entry : params.entrySet()) {
				nvps.add(new BasicNameValuePair(entry.getKey(), entry
						.getValue()));
			}

			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			response = httpclient.execute(httpost);

			HttpEntity entity = response.getEntity();
			if (response.getStatusLine().getStatusCode() == 200) {
				inputStream = entity.getContent();
			} else {
				int code = response.getStatusLine().getStatusCode();
				System.out.println("==========HttpUtils statusCode:" + code);
				Log.i(TAG, String.valueOf(code));
			}
		} catch (ClientProtocolException e) {
			Log.i(TAG, e.getMessage() + "ClientProtocolException");
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
		}
		return inputStream;
	}

	

	
    /**
     * 获取网络图片,如果图片存在于缓存中，就返回该图片，否则从网络中加载该图片并缓存起来
     * @param path 图片路径
     * @return
     */
    public static Uri getImage(String path, File cacheDir, String name) throws Exception{// path -> MD5 ->32字符串.jpg

        File localFile = new File(cacheDir, name);
//        if(localFile.exists()){
//            return Uri.fromFile(localFile);
//        }else{
        	Log.i("Willguo", "cache dont have...");
            HttpURLConnection conn = (HttpURLConnection) new URL(path).openConnection();
            conn.setConnectTimeout(5000);
            conn.setRequestMethod("GET");
            if(conn.getResponseCode() == 200){    
            	Log.i("Willguo","conn.getResponseCode()  " +conn.getResponseCode());   
                FileOutputStream outStream = new FileOutputStream(localFile);
                InputStream inputStream = conn.getInputStream();
                byte[] buffer = new byte[1024]; 
                int len = 0;
                while( (len = inputStream.read(buffer)) != -1){
                    outStream.write(buffer, 0, len);
                }
                inputStream.close();
                outStream.close();
                
                return Uri.fromFile(localFile);
            }else{
            	Log.e("Willguo","ResponseCode() " +conn.getResponseCode());
            }
            
//        }
        return null;
    }
	
}
