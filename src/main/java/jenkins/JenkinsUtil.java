package jenkins;

import java.io.IOException;
import java.net.URI;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * Title: JenkinsUtil Description:调用jenkisn API
 * 
 * @author Dalphist
 * @date 2019年8月1日
 */
public class JenkinsUtil {

	public static void createJob(String xmlData) throws Exception {
		String urlString = "http://192.168.251.107:8980/jenkins/createItem?name=test666";
		HttpResponse response = getJenkinsRep(xmlData, urlString);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println("create: "+result);
		return;
	}


	public static void updateJob(String xmlData) throws ClientProtocolException, IOException {
		String urlString = "http://192.168.251.107:8980/jenkins/job/test666/config.xml";
		HttpResponse response = getJenkinsRep(xmlData, urlString);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println("update: "+result);
		return;
	}

	public static void main(String[] args) throws Exception {
		String xmlData = 
				"<project>\r\n" + 
				"    <keepDependencies>false</keepDependencies>\r\n" + 
				"    <properties/>\r\n" + 
				"    <scm class=\"hudson.scm.NullSCM\"/>\r\n" + 
				"    <canRoam>false</canRoam>\r\n" + 
				"    <disabled>false</disabled>\r\n" + 
				"    <blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\r\n" + 
				"    <blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\r\n" + 
				"    <triggers/>\r\n" + 
				"    <concurrentBuild>false</concurrentBuild>\r\n" + 
				"    <builders/>\r\n" + 
				"    <publishers/>\r\n" + 
				"    <buildWrappers/>\r\n" + 
				"</project>";
		createJob(xmlData);
	}

	private static HttpResponse getJenkinsRep(String xmlData, String urlString) throws IOException, ClientProtocolException {
		URI uri = URI.create(urlString);
		HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
		
		// CredentialsProvider接口，凭据提供器，用来提供HTTP方法请求头认证。
		CredentialsProvider credsProvider = new BasicCredentialsProvider();
		// AuthScope类，认证范围，由主机（IP）、端口信息组成。
		credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()),new UsernamePasswordCredentials("admin", "admin"));
		
		AuthCache authCache = new BasicAuthCache();
		BasicScheme basicAuth = new BasicScheme();
		authCache.put(host, basicAuth);
		
		CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
		HttpPost httpPost = new HttpPost(uri);
		httpPost.setEntity(new StringEntity(xmlData, ContentType.create("text/xml", "utf-8")));
		
		httpPost.setHeader("Content-Type", "application/xml;charset=UTF-8");
		HttpClientContext localContext = HttpClientContext.create();
		localContext.setAuthCache(authCache);
		
		HttpResponse response = httpClient.execute(host, httpPost, localContext);
		return response;
	}
}
