package com.ilicit.ewerdima.helper;

import android.app.Activity;
import android.net.http.AndroidHttpClient;
import android.util.Log;

import com.ilicit.ewerdima.Utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;

public class ServiceHandler {

	static InputStream is = null;
	static String response = null;
	public final static int GET = 1;
	public final static int POST = 2;
    Activity activity;

        public ServiceHandler(Activity activity) {
            this.activity =activity;
	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * */
	public String makeServiceCall(String url, int method) {
		return this.makeServiceCall(url, method, null);
	}

	/*
	 * Making service call
	 * @url - url to make request
	 * @method - http request method
	 * @params - http request params
	 * */
	public String makeServiceCall(String url, int method,
			List<NameValuePair> params) {
        AndroidHttpClient httpClient = AndroidHttpClient.newInstance("user agent");
        HttpEntity httpEntity = null;
        HttpResponse httpResponse = null;
		try {
			// http client



            URL urlObj = new URL(url);
            HttpHost host = new HttpHost(urlObj.getHost(), urlObj.getPort(), urlObj.getProtocol());
            AuthScope scope = new AuthScope(urlObj.getHost(), urlObj.getPort());
            UsernamePasswordCredentials creds = new UsernamePasswordCredentials(Utils.getSaved("apiUsername", activity), Utils.getSaved("apipassword",activity));

            CredentialsProvider cp = new BasicCredentialsProvider();
            cp.setCredentials(scope, creds);
            HttpContext credContext = new BasicHttpContext();
            credContext.setAttribute(ClientContext.CREDS_PROVIDER, cp);


			
			// Checking http request method type
			if (method == POST) {

				HttpPost httpPost = new HttpPost(url);
				// adding post params
				if (params != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(params));
				}

				httpResponse = httpClient.execute(host,httpPost,credContext);
                httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

			} else if (method == GET) {
				// appending params to url
				if (params != null) {
					String paramString = URLEncodedUtils
							.format(params, "utf-8");
					url += "?" + paramString;
				}
				HttpGet httpGet = new HttpGet(url);

				httpResponse = httpClient.execute(host,httpGet,credContext);
                httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

			}


		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();
			httpClient.close();
			response = sb.toString();
			Log.e("response in file",""+response);
		} catch (Exception e) {
			Log.e("Buffer Error", "Error: " + e.toString());
		}

		return response;

	}
}