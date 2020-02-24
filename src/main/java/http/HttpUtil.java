package http;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class HttpUtil {

	public static String getHttpInfo(String urlString)	throws IOException, ClientProtocolException {
		URI uri = URI.create(urlString);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CloseableHttpClient httpClient = HttpClients.custom().build();
        HttpGet httpGet = new HttpGet(uri);
        HttpClientContext localContext = HttpClientContext.create();
        HttpResponse response = httpClient.execute(host, httpGet, localContext);
    	return EntityUtils.toString(response.getEntity());
	}
	
	public static void main(String[] args) throws ClientProtocolException, IOException {
		String url = "http://192.168.251.107:9000/api/issues/search?componentKeys=dev_cis&s=FILE_LINE&resolved=false&ps=1&facets=types";
		String json = getHttpInfo(url);
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
