package gitlab;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gitlab.entity.GitlabBranch;
import gitlab.entity.GitlabCommit;
import org.apache.commons.codec.binary.StringUtils;

import java.util.List;
import java.util.Map;

/**
 * @author DJF
 * @version 0.1.0
 * @Description 统计提交信息
 * @create 2020-12-10 15:50
 * @since 0.1.0
 **/
public class CommitStatisticalUtil {
    private String adminToken = "rsn5XoRCSTXpyx_XiufC";
    private static final String baseUrl = "http://192.168.251.101:8100/api/v4/";



    public static void main(String[] args) throws Exception {
//        List<GitlabBranch> branchList = getAllBranch();
//        for(GitlabBranch branch : branchList){
//
//        }
        getCommitByBranch("common");
    }
    public static List<GitlabBranch> getAllBranch() throws Exception {
        String branches = GitLabHttpUtil.doGet(baseUrl + "projects/14/repository/branches");
        List<GitlabBranch> branchList = JSONObject.parseArray(branches, GitlabBranch.class);
        return branchList;
    }

    public static List<GitlabCommit> getCommitByBranch(String branchName) throws Exception {
        //获取上一次最后统计的日期
        String lastDate = "2020-01-01";
        String commitUrl = baseUrl + "projects/14/repository/commits?ref_name=%s&per_page=50&page=%s";
        if(lastDate != null){
            commitUrl += ("&since=" + lastDate);
        }
        int totalPage = 1;
        String url = String.format(commitUrl,branchName,1);
        Map<String,Object> map = GitLabHttpUtil.doGetWithPage(url);
        String commits = map.get("data").toString();
        totalPage = Integer.valueOf(map.get("total").toString());
        List<GitlabCommit> commitList = JSONObject.parseArray(commits, GitlabCommit.class);
        getCommitStats(commitList);
        if(totalPage > 1){
            for(int i = 2 ; i <= totalPage ; i++ ){
                String pageUrl = String.format(commitUrl,branchName,i);
                String commitListStr = GitLabHttpUtil.doGet(pageUrl);
                List<GitlabCommit> commitListPage = JSONObject.parseArray(commitListStr, GitlabCommit.class);
                getCommitStats(commitListPage);
            }
        }
        return commitList;
    }

    private static void getCommitStats(List<GitlabCommit> commitList) throws Exception {
        int count = 0;
        for(GitlabCommit gitlabCommit : commitList){
            String message = gitlabCommit.getMessage();
            if(message.indexOf("Merge") >= 0){
                continue;
            }
            count ++;
            String commitId = gitlabCommit.getId();
            String commitUrl = String.format("projects/14/repository/commits/%s",commitId);
            String commitInfo = GitLabHttpUtil.doGet(baseUrl + commitUrl);
            GitlabCommit gitlabCommitInfo = JSON.parseObject(commitInfo,GitlabCommit.class);
            System.out.println(gitlabCommit);
            System.out.println(String.format("add:%s,del:%s",gitlabCommitInfo.getStats().getAdditions(),gitlabCommitInfo.getStats().getDeletions()));
        }
        System.out.println("count===================================="+count);
    }
}
