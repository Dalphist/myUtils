package gitlab;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class GitLabHttpUtil {

    private static final String CODE = "utf-8";
    private static final String adminToken = "rsn5XoRCSTXpyx_XiufC";

	public static String doGet(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("PRIVATE-TOKEN",adminToken);
        return execute(httpGet);
    }

    public static Map<String,Object> doGetWithPage(String url) throws Exception {
        HttpGet httpGet = new HttpGet(url);
        httpGet.addHeader("PRIVATE-TOKEN",adminToken);
        return executeReturnMap(httpGet);
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

    public static String doPut(String url,Map<String, String> param){
        HttpPut httpput = new HttpPut(url);
        Set<String> keySet = param.keySet();
        for (String key : keySet) {
            httpput.addHeader(key, param.get(key));
        }
        return execute(httpput);
    }

	   private static String execute(HttpRequestBase request) {
	        CloseableHttpClient httpClient = HttpClients.createDefault();
            CloseableHttpResponse response = null;
            try {
                response = httpClient.execute(request);

                if (200 == response.getStatusLine().getStatusCode() || 201 == response.getStatusLine().getStatusCode()) {
                   return EntityUtils.toString(response.getEntity(), Charset.forName(CODE));
                } else {
                   System.out.println(EntityUtils.toString(response.getEntity(), Charset.forName(CODE)));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }finally{
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
           try {
               httpClient.close();  //关闭连接、释放资源
           } catch (IOException e) {
               e.printStackTrace();
           }

	      return "";
	   }

    private static Map<String,Object> executeReturnMap(HttpRequestBase request) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        Map<String,Object> map = new HashMap<>();
        try {
            response = httpClient.execute(request);
            String totalPage = response.getHeaders("X-Total-Pages")[0].getValue();
            if (200 == response.getStatusLine().getStatusCode() || 201 == response.getStatusLine().getStatusCode()) {
                String entries = EntityUtils.toString(response.getEntity(), Charset.forName(CODE));
                map.put("total",totalPage);
                map.put("data",entries);
                return map;
            } else {
                System.out.println(EntityUtils.toString(response.getEntity(), Charset.forName(CODE)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            httpClient.close();  //关闭连接、释放资源
        } catch (IOException e) {
            e.printStackTrace();
        }

        return map;
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






}
