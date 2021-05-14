package gitlab.entity;

import lombok.Data;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2020-12-10 16:52
 * @since 0.1.0
 **/
@Data
public class GitlabCommit {
    private String id;
    private String short_id;
    private String author_name;
    private String authored_date;
    private String committer_name;
    private String committed_date;
    private String title;
    private String message;
    private CommitStats stats;
    private String project_id;

}
