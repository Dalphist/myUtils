package jenkins;

import java.util.concurrent.TimeUnit;

import org.apache.commons.codec.binary.StringUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSONObject;

public class JenkinsTest {
	private static String jenkinsUrl = "http://192.168.46.22:8980/jenkins/";
	private static String jobName = "board_board";
	
	
	//获取jenkins任务执行结果
	@Test
	public void getJobJsonTest() throws Exception{
		String urlString = jenkinsUrl + "/job/" + jobName + "/api/json";
		String lastBuildJson;
		do {
			String jobInfo = JenkinsUtil.jenkinsInfoQuery("admin", "admin", urlString);
			JSONObject job = JSONObject.parseObject(jobInfo);
			lastBuildJson = job.getString("lastBuild");
		}while(lastBuildJson == null);
		JSONObject lastBuild = JSONObject.parseObject(lastBuildJson);
    	String lastBuildNum = lastBuild.getString("number");
    	System.out.println("lastJobId = "+ lastBuildNum);
    	
    	String result = null;
    	while(result == null || "".equals(result)) {
			String lastJobInfoStr = JenkinsUtil.getBuildInfoByBuildId(jenkinsUrl, jobName, lastBuildNum, "admin", "admin");
			JSONObject lastJobInfo = JSONObject.parseObject(lastJobInfoStr);
			System.out.println("lastJobInfo:" + lastJobInfo);
			result = lastJobInfo.getString("result");
			System.out.println("lastJobResult:" + result);
			String building = lastJobInfo.getString("building");
			System.out.println("building:" + building);
			TimeUnit.SECONDS.sleep(1);
        }
		
	}
}
