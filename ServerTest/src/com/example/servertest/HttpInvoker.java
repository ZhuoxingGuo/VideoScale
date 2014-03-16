package com.example.servertest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

public class HttpInvoker 
{
	static Logger log = Logger.getLogger(
			HttpInvoker.class.getName());
	
	private SSLSocketFactory ssf;
    public void Init(TrustManager trustManager) throws NoSuchAlgorithmException, KeyManagementException
    {  
        // 创建SSLContext对象，并使用我们指定的信任管理器初始�?
        TrustManager[] tm = { trustManager };
        SSLContext sslContext =SSLContext.getInstance("TLS");
		sslContext.init(null, tm, new java.security.SecureRandom());
        // 从上述SSLContext对象中得到SSLSocketFactory对象
        this.ssf = sslContext.getSocketFactory();
    }
 
    /**
     * Host name verifier that does not perform nay checks.
     */
    class NullHostnameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String arg0, SSLSession arg1) {
			
			return true;
		}
    }
    
    public String doGet(String getURL)
    {
    	URI uri = null;
		try {
			uri = new URI(getURL);
		} catch (URISyntaxException e) {
			log.warning("doGet Construct URI Exception: " + e.toString() );
			return null;
		}
		String ResponseString = null;
		try
		{
	    	if ( uri.getScheme().equals("https") )
	    	{
	    		ResponseString = doHttpsGet(getURL);
	    	}else
	    	{
	    		ResponseString = doHttpGet(getURL);
	    	}
		}catch(Exception e)
		{
			log.warning("doGet doHttpsGet or doHttpGet Exception: " + e.toString() );
			return null;
		}
    	return ResponseString;
    }
    
    private String doHttpsGet(String getURL) throws IOException
    {
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打�?��接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpsURLConnection connection = (HttpsURLConnection) getUrl
                .openConnection();
        connection.setConnectTimeout(7500);
        connection.setReadTimeout(7500);
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发�?
        // 服务�?
        connection.setSSLSocketFactory(this.ssf);
        connection.setHostnameVerifier(new NullHostnameVerifier());
        //connection.connect();    
        InputStream is = connection.getInputStream();
        String returnData = ReadStringFromStream(is);
        is.close();
        connection.disconnect();
        return returnData;
    }

    private String doHttpGet(String getURL) throws IOException
    {
        URL getUrl = new URL(getURL);
        // 根据拼凑的URL，打�?��接，URL.openConnection函数会根据URL的类型，
        // 返回不同的URLConnection子类的对象，这里URL是一个http，因此实际返回的是HttpURLConnection
        HttpURLConnection connection = (HttpURLConnection) getUrl
                .openConnection();
        connection.setConnectTimeout(7500);
        connection.setReadTimeout(7500);        
        // 进行连接，但是实际上get request要在下一句的connection.getInputStream()函数中才会真正发�?
        // 服务�?
        //connection.connect();    
        InputStream is = connection.getInputStream();
        String returnData = ReadStringFromStream(is);
        is.close();
        connection.disconnect();
        return returnData;
    }
    
    private String ReadStringFromStream(InputStream is) 
    {
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	    StringBuilder sb = new StringBuilder();

	    String line = null;
	    try {
	        while ((line = reader.readLine()) != null) {
	            sb.append(line);
	        }
	    } catch (IOException e) 
	    {
	        e.printStackTrace();
	    }
	    return sb.toString();
	}
    
    
    public static void readContentFromPost(String Url) throws IOException {
        // Post请求的url，与get不同的是不需要带参数
        URL postUrl = new URL(Url);
        // 打开连接
        HttpURLConnection connection = (HttpURLConnection) postUrl
                .openConnection();
        // Output to the connection. Default is
        // false, set to true because post
        // method must write something to the
        // connection
        // 设置是否向connection输出，因为这个是post请求，参数要放在
        // http正文内，因此�?��设为true
        connection.setDoOutput(true);
        // Read from the connection. Default is true.
        connection.setDoInput(true);
        // Set the post method. Default is GET
        connection.setRequestMethod("POST");
        // Post cannot use caches
        // Post 请求不能使用缓存
        connection.setUseCaches(false);
        // This method takes effects to
        // every instances of this class.
        // URLConnection.setFollowRedirects是static函数，作用于�?��的URLConnection对象�?
        // connection.setFollowRedirects(true);

        // This methods only
        // takes effacts to this
        // instance.
        // URLConnection.setInstanceFollowRedirects是成员函数，仅作用于当前函数
        connection.setInstanceFollowRedirects(true);
        // Set the content type to urlencoded,
        // because we will write
        // some URL-encoded content to the
        // connection. Settings above must be set before connect!
        // 配置本次连接的Content-type，配置为application/x-www-form-urlencoded�?
        // 意�?是正文是urlencoded编码过的form参数，下面我们可以看到我们对正文内容使用URLEncoder.encode
        // 进行编码
        connection.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
        // 连接，从postUrl.openConnection()至此的配置必须要在connect之前完成�?
        // 要注意的是connection.getOutputStream会隐含的进行connect�?
        connection.connect();
        DataOutputStream out = new DataOutputStream(connection
                .getOutputStream());
        // The URL-encoded contend
        // 正文，正文内容其实跟get的URL�??'后的参数字符串一�?
        String content = "firstname=" + URLEncoder.encode("", "utf-8");
        // DataOutputStream.writeBytes将字符串中的16位的unicode字符�?位的字符形式写道流里�?
        out.writeBytes(content); 

        out.flush();
        out.close(); // flush and close
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        String line;
        System.out.println("=============================");
        System.out.println("Contents of post request");
        System.out.println("=============================");
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("=============================");
        System.out.println("Contents of post request ends");
        System.out.println("=============================");
        reader.close();
        connection.disconnect();
    }
}