package com.example.servertest;

import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.net.ssl.X509TrustManager;

final public class TaoX509TrustManager implements X509TrustManager 
{
	static Certificate alipayCert = null;

	public TaoX509TrustManager() throws Exception 
	{	
		synchronized(TaoX509TrustManager.class) 
		{
			if (alipayCert == null) 
			{
				throw new Exception("X509TrustManager not inited.");
			}
		}
	}

	public synchronized static void init (InputStream in) throws Exception 
	{
		CertificateFactory cerFactory = CertificateFactory.getInstance("X.509");
		alipayCert = cerFactory.generateCertificate(in);
	}
	
	@Override
	public void checkClientTrusted(X509Certificate[] chain, String authType)
			throws CertificateException {
	}

	@Override
	public void checkServerTrusted(X509Certificate[] chain, String paramString)
			throws CertificateException 
	{
		for (int i = 0; i < chain.length; i++)
		{
			boolean exceptionOccur = false;
			X509Certificate cert = chain[i];
			// 验证时间有效�?
			cert.checkValidity();
			// 验证签名
			try {
				cert.verify(alipayCert.getPublicKey());
			} catch (InvalidKeyException e) {
				e.printStackTrace();
				exceptionOccur = true;
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
				exceptionOccur = true;
			} catch (NoSuchProviderException e) {
				e.printStackTrace();
				exceptionOccur = true;
			} catch (SignatureException e) {
				e.printStackTrace();
				exceptionOccur = true;
			}

			if (!exceptionOccur) 
			{
				return;
			}

			exceptionOccur = false;
		}
	}

	@Override
	public X509Certificate[] getAcceptedIssuers()
	{
		return null;
	}
}
