package jenkins;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.methods.HttpGet;
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
	private static String jenkinsUrl = "http://192.168.251.107:8980/jenkins/";

	public static String getCoberturaReport(String jenkinsUrl, String jobName, String username, String password) throws IOException {
        //这里需要CIS_SERVER_INFO的构建服务器地址 CIS_SERVER_INFO_ADDITIONAL的jenkins用户名密码
//        ServerInfo buildServerInfo = serverInfoService.getServerInfoListByType("01").get(0);
//        String url= "http://" + buildServerInfo.getServerIp() + ":" + buildServerInfo.getServerinfoAdditional().getPort() + "/jenkins";
        String jkLogUrlFormat=jenkinsUrl.concat("/job/%s/lastCompletedBuild/");
        String jkLogUrl=String.format(jkLogUrlFormat, jobName);
        String jkContent = jenkinsInfoQuery(username,password,jkLogUrl);
        String pattern = "[\\s\\S]*?<b>Lines</b>[\\s\\S]*?(\\d+)?%[\\s\\S]*";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(jkContent);
        if(m.find()){
            return m.group(1);
        }else {
            return "0%";
        }
    }
	
	public static void createJob(String xmlData,String jobName) throws Exception {
		String urlString = jenkinsUrl + "createItem?name=" + jobName;
		HttpResponse response = getJenkinsRep(xmlData, urlString);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println("create: "+result);
		return;
	}


	public static void updateJob(String xmlData,String jobName) throws ClientProtocolException, IOException {
		String urlString = jenkinsUrl + "job/"+ jobName +"/config.xml";
		HttpResponse response = getJenkinsRep(xmlData, urlString);
		String result = EntityUtils.toString(response.getEntity());
		System.out.println("update: "+result);
		return;
	}

	public static void main(String[] args) throws Exception {
		
		createJob(mavenDataXml,"job1");
//		updateJob(xmlData1,"job1");
//		build("mavenTest1");
//		String s = getCoberturaReport("http://192.168.251.107:8980/jenkins/","cis定时扫描","admin","admin");
//		String s = jenkinsInfoQuery("admin","admin","http://192.168.251.107:8980/jenkins/job/730_devops_bak/73/console");
//		System.out.println(s);
	}

	private static void build(String jobName) throws ClientProtocolException, IOException {
		String urlString = jenkinsUrl + "job/"+ jobName +"/build";
		URI uri = URI.create(urlString);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials("admin", "admin"));
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        HttpPost httpPost = new HttpPost(uri);
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);
        HttpResponse response = httpClient.execute(host, httpPost, localContext);
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
	
	public static String jenkinsInfoQuery(String username, String password, String urlString)
			throws IOException, ClientProtocolException {
		URI uri = URI.create(urlString);
        HttpHost host = new HttpHost(uri.getHost(), uri.getPort(), uri.getScheme());
        CredentialsProvider credsProvider = new BasicCredentialsProvider();
        credsProvider.setCredentials(new AuthScope(uri.getHost(), uri.getPort()), new UsernamePasswordCredentials(username, password));
        AuthCache authCache = new BasicAuthCache();
        BasicScheme basicAuth = new BasicScheme();
        authCache.put(host, basicAuth);
        CloseableHttpClient httpClient = HttpClients.custom().setDefaultCredentialsProvider(credsProvider).build();
        HttpGet httpGet = new HttpGet(uri);
        HttpClientContext localContext = HttpClientContext.create();
        localContext.setAuthCache(authCache);
        HttpResponse response = httpClient.execute(host, httpGet, localContext);
    	return EntityUtils.toString(response.getEntity());
	}

	public static String getBuildInfoByBuildId(String jenkinsUrl, String jobName, String buildId, String username, String password) throws ClientProtocolException, IOException{
    	String urlString  = jenkinsUrl + "/job/" + jobName + "/" + buildId + "/api/json";
    	System.out.println(urlString + " 任务构建中……");
    	return jenkinsInfoQuery(username, password, urlString);
    }
	
	private static String xmlData = 
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
	private static String xmlData1 = 
			"<project>\r\n" + 
			"<actions/>\r\n" + 
			"<description/>\r\n" + 
			"<keepDependencies>false</keepDependencies>\r\n" + 
			"<properties/>\r\n" + 
			"<scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.6.4\">\r\n" + 
			"<configVersion>2</configVersion>\r\n" + 
			"<userRemoteConfigs>\r\n" + 
			"<hudson.plugins.git.UserRemoteConfig>\r\n" + 
			"<url>\r\n" + 
			"http://djf:f27d01d26625411aaeb9ec1110cd1661@192.168.251.101/TCTS\r\n" + 
			"</url>\r\n" + 
			"<credentialsId>192.168.251.101_djf</credentialsId>\r\n" + 
			"</hudson.plugins.git.UserRemoteConfig>\r\n" + 
			"</userRemoteConfigs>\r\n" + 
			"<branches>\r\n" + 
			"<hudson.plugins.git.BranchSpec>\r\n" + 
			"<name>TCTS_DEV</name>\r\n" + 
			"</hudson.plugins.git.BranchSpec>\r\n" + 
			"</branches>\r\n" + 
			"<doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\r\n" + 
			"<submoduleCfg class=\"list\"/>\r\n" + 
			"<extensions/>\r\n" + 
			"</scm>\r\n" + 
			"<canRoam>true</canRoam>\r\n" + 
			"<disabled>false</disabled>\r\n" + 
			"<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\r\n" + 
			"<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\r\n" + 
			"<jdk>(System)</jdk>\r\n" + 
			"<triggers/>\r\n" + 
			"<concurrentBuild>false</concurrentBuild>\r\n" + 
			"<builders/>\r\n" + 
			"<publishers/>\r\n" + 
			"<buildWrappers/>\r\n" + 
			"</project>";
	
	private static String mavenDataXml = "<maven2-moduleset plugin=\"maven-plugin@3.0\">\r\n" + 
			"<actions/>\r\n" + 
			"<description/>\r\n" + 
			"<keepDependencies>false</keepDependencies>\r\n" + 
			"<properties/>\r\n" + 
			"<scm class=\"hudson.plugins.git.GitSCM\" plugin=\"git@3.6.4\">\r\n" + 
			"<configVersion>2</configVersion>\r\n" + 
			"<userRemoteConfigs>\r\n" + 
			"<hudson.plugins.git.UserRemoteConfig>\r\n" + 
			"<url>https://github.com/Dalphist/blog.git</url>\r\n" + 
			"<credentialsId>c7c1daae-0448-41a4-87c0-4f9143781fdc</credentialsId>\r\n" + 
			"</hudson.plugins.git.UserRemoteConfig>\r\n" + 
			"</userRemoteConfigs>\r\n" + 
			"<branches>\r\n" + 
			"<hudson.plugins.git.BranchSpec>\r\n" + 
			"<name>*/master</name>\r\n" + 
			"</hudson.plugins.git.BranchSpec>\r\n" + 
			"</branches>\r\n" + 
			"<doGenerateSubmoduleConfigurations>false</doGenerateSubmoduleConfigurations>\r\n" + 
			"<submoduleCfg class=\"list\"/>\r\n" + 
			"<extensions/>\r\n" + 
			"</scm>\r\n" + 
			"<canRoam>true</canRoam>\r\n" + 
			"<disabled>false</disabled>\r\n" + 
			"<blockBuildWhenDownstreamBuilding>false</blockBuildWhenDownstreamBuilding>\r\n" + 
			"<blockBuildWhenUpstreamBuilding>false</blockBuildWhenUpstreamBuilding>\r\n" + 
			"<jdk>1.8</jdk>\r\n" + 
			"<triggers/>\r\n" + 
			"<concurrentBuild>false</concurrentBuild>\r\n" + 
			"<rootPOM>blog-api/pom.xml</rootPOM>\r\n" + 
			"<goals>clean package -Dmaven.test.skip=true</goals>\r\n" + 
			"<aggregatorStyleBuild>true</aggregatorStyleBuild>\r\n" + 
			"<incrementalBuild>false</incrementalBuild>\r\n" + 
			"<ignoreUpstremChanges>false</ignoreUpstremChanges>\r\n" + 
			"<ignoreUnsuccessfulUpstreams>false</ignoreUnsuccessfulUpstreams>\r\n" + 
			"<archivingDisabled>false</archivingDisabled>\r\n" + 
			"<siteArchivingDisabled>false</siteArchivingDisabled>\r\n" + 
			"<fingerprintingDisabled>false</fingerprintingDisabled>\r\n" + 
			"<resolveDependencies>false</resolveDependencies>\r\n" + 
			"<processPlugins>false</processPlugins>\r\n" + 
			"<mavenValidationLevel>-1</mavenValidationLevel>\r\n" + 
			"<runHeadless>false</runHeadless>\r\n" + 
			"<disableTriggerDownstreamProjects>false</disableTriggerDownstreamProjects>\r\n" + 
			"<blockTriggerWhenBuilding>true</blockTriggerWhenBuilding>\r\n" + 
			"<settings class=\"jenkins.mvn.DefaultSettingsProvider\"/>\r\n" + 
			"<globalSettings class=\"jenkins.mvn.DefaultGlobalSettingsProvider\"/>\r\n" + 
			"<reporters/>\r\n" + 
			"<publishers/>\r\n" + 
			"<buildWrappers/>\r\n" + 
			"<prebuilders/>\r\n" + 
			"<postbuilders>\r\n" + 
			"<hudson.tasks.Shell>\r\n" + 
			"<command>\r\n" + 
			"cp ${JENKINS_HOME}/workspace/test1/blog-api/target/blog.jar /opt/yusys/jenkins/save/blog.jar\r\n" + 
			"</command>\r\n" + 
			"</hudson.tasks.Shell>\r\n" + 
			"</postbuilders>\r\n" + 
			"<runPostStepsIfResult>\r\n" + 
			"<name>SUCCESS</name>\r\n" + 
			"<ordinal>0</ordinal>\r\n" + 
			"<color>BLUE</color>\r\n" + 
			"<completeBuild>true</completeBuild>\r\n" + 
			"</runPostStepsIfResult>\r\n" + 
			"</maven2-moduleset>";

	public static String getJenkinsUrl() {
		return jenkinsUrl;
	}


	public static void setJenkinsUrl(String jenkinsUrl) {
		JenkinsUtil.jenkinsUrl = jenkinsUrl;
	}


	public static String getXmlData() {
		return xmlData;
	}


	public static void setXmlData(String xmlData) {
		JenkinsUtil.xmlData = xmlData;
	}


	public static String getXmlData1() {
		return xmlData1;
	}


	public static void setXmlData1(String xmlData1) {
		JenkinsUtil.xmlData1 = xmlData1;
	}


	public static String getMavenDataXml() {
		return mavenDataXml;
	}


	public static void setMavenDataXml(String mavenDataXml) {
		JenkinsUtil.mavenDataXml = mavenDataXml;
	}


	
	
	
}
