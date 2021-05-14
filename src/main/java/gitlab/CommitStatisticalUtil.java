package gitlab;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import gitlab.entity.CommitEntity;
import gitlab.entity.GitlabBranch;
import gitlab.entity.GitlabCommit;
import org.apache.commons.codec.binary.StringUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
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
    private static String gitlabId = "46";


    public static void main(String[] args) throws Exception {
        List<GitlabBranch> branchList = getAllBranch();
        for(GitlabBranch branch : branchList){
            System.out.println("分支--------"+ branch.getName());
            getCommitByBranch(branch.getName());
        }

    }
    public static List<GitlabBranch> getAllBranch() throws Exception {
        String branches = GitLabHttpUtil.doGet(baseUrl + "projects/"+ gitlabId +"/repository/branches");
        List<GitlabBranch> branchList = JSONObject.parseArray(branches, GitlabBranch.class);
        return branchList;
    }

    public static List<CommitEntity> getCommitByBranch(String branchName) throws Exception {
        List<CommitEntity> commitEntityList = new ArrayList<>();
        //获取上一次最后统计的日期
        String lastDate = "2021-04-06T02:46:32";
        String commitUrl = baseUrl + "projects/"+ gitlabId +"/repository/commits?ref_name=%s&per_page=50&page=%s";
        if(lastDate != null){
            commitUrl += ("&since=" + lastDate);
        }
        int totalPage = 1;
        String url = String.format(commitUrl,branchName,1);
        Map<String,Object> map = GitLabHttpUtil.doGetWithPage(url);
        String commits = map.get("data").toString();
        totalPage = Integer.valueOf(map.get("total").toString());
        List<GitlabCommit> commitList = JSONObject.parseArray(commits, GitlabCommit.class);
        commitEntityList = getCommitStats(commitList);
        if(totalPage > 1){
            for(int i = 2 ; i <= totalPage ; i++ ){
                String pageUrl = String.format(commitUrl,branchName,i);
                String commitListStr = GitLabHttpUtil.doGet(pageUrl);
                List<GitlabCommit> commitListPage = JSONObject.parseArray(commitListStr, GitlabCommit.class);
                getCommitStats(commitListPage);
            }
        }
        return commitEntityList;
    }

    private static List<CommitEntity> getCommitStats(List<GitlabCommit> commitList) throws Exception {
        List<CommitEntity> commitEntityList = new ArrayList<>();
        int count = 0;
        for(GitlabCommit gitlabCommit : commitList){
            CommitEntity commitEntity = new CommitEntity();
            String message = gitlabCommit.getMessage();
            if(message.indexOf("Merge") >= 0){
                commitEntity.setMergeCommit(true);
            }
            count ++;
            String commitId = gitlabCommit.getId();
            String commitUrl = String.format("projects/"+ gitlabId +"/repository/commits/%s",commitId);
            String commitInfo = GitLabHttpUtil.doGet(baseUrl + commitUrl);
            GitlabCommit gitlabCommitInfo = JSON.parseObject(commitInfo,GitlabCommit.class);
            //赋值给实体类
            commitEntity.setHashCode(gitlabCommitInfo.getId());
            commitEntity.setGitlabProjectId("45");
            commitEntity.setMessage(gitlabCommitInfo.getMessage());
            commitEntity.setSubmitterUsername(gitlabCommitInfo.getCommitter_name());
            commitEntity.setSubmitTm(chrans(gitlabCommitInfo.getCommitted_date()));
            commitEntity.setAuthorTm(chrans(gitlabCommitInfo.getAuthored_date()));
            commitEntity.setLineNumAdd(gitlabCommitInfo.getStats().getAdditions());
            commitEntity.setLineNumSub(gitlabCommitInfo.getStats().getDeletions());
            commitEntity.setLineNumChange(gitlabCommitInfo.getStats().getTotal());
            commitEntity.setLineNumNetAdd(gitlabCommitInfo.getStats().getAdditions() - gitlabCommitInfo.getStats().getDeletions());
            commitEntity.setTaskId(getTaskIdFromCommit(gitlabCommitInfo.getMessage()));
            commitEntity.setGitlabProjectId(gitlabCommitInfo.getProject_id());
            commitEntityList.add(commitEntity);
//            System.out.println(commitEntity);
//            System.out.println(String.format("add:%s,del:%s",gitlabCommitInfo.getStats().getAdditions(),gitlabCommitInfo.getStats().getDeletions()));
        }
        System.out.println("count===================================="+count);
        return commitEntityList;
    }

    private static String getTaskIdFromCommit(String message) {
        String taskId = "";
        if(message.startsWith("[") && message.indexOf("]") > 0){
            taskId = message.substring(1,message.indexOf("]"));
        }
        return taskId;
    }

    private static LocalDateTime chrans(String date) {
        String s = date.substring(0,19);
        s = s.replace("T"," ");
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(s,df);
        return localDateTime;

    }
}
