package http;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {
	
	public static String doGet(String url) throws Exception {
	      HttpGet httpGet = new HttpGet(url);
	      return execute(httpGet);
	   }

	   public static String doPost(String url, Map<String, String> param) throws Exception {
	      HttpPost httpPost = new HttpPost(url);
	      ArrayList<BasicNameValuePair> arrayList = new ArrayList<BasicNameValuePair>();
	      Set<String> keySet = param.keySet();
	      for (String key : keySet) {
	         arrayList.add(new BasicNameValuePair(key, param.get(key)));
	      }
	      httpPost.setEntity(new UrlEncodedFormEntity(arrayList));
	      return execute(httpPost);
	   }

	   private static String execute(HttpRequestBase request) throws IOException, ClientProtocolException {
	      CloseableHttpClient httpClient = HttpClients.createDefault();
	      CloseableHttpResponse response = httpClient.execute(request);
	      if (200 == response.getStatusLine().getStatusCode()) {
	         return EntityUtils.toString(response.getEntity(), Charset.forName("utf-8"));
	      } else {
	         System.out.println(EntityUtils.toString(response.getEntity(), Charset.forName("utf-8")));
	      }
	      return "";
	   }

	public static String getHttpInfo(String urlString)	throws IOException, ClientProtocolException {
		URI uri = URI.create(urlString);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet(uri);
        HttpClientContext localContext = HttpClientContext.create();
        HttpResponse response = httpClient.execute(host, httpGet, localContext);
    	return EntityUtils.toString(response.getEntity());
	}
	
	public static void main(String[] args) throws Exception {
		String url = "http://139.159.235.103:8082/pda/api/getInventoryOrganizationList?ID&Ioname";
		String json = doPost(url,new HashMap<>());
		
		JSONObject obj = JSONObject.parseObject(json);
		String facets = obj.getString("facets");
		String values = JSONArray.parseArray(facets).getJSONObject(0).getString("values");
		JSONArray valuesArr = JSONArray.parseArray(values);
		String code_smell = valuesArr.getJSONObject(0).getString("count");
		String vulnerability = valuesArr.getJSONObject(1).getString("count");
		String bug = valuesArr.getJSONObject(2).getString("count");
		
		System.out.println("code_smell:" + code_smell);
		System.out.println("vulnerability:" + vulnerability);
		System.out.println("bug:" + bug);
	}

}
