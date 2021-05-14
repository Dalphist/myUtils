package gitlab.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class CommitEntity implements Serializable {

 /**
  * gitlab_代码工程id
  */
 private String gitlabProjectId;

 /**
  * 代码工程id
  */
 private String projectId;

 /**
  * 系统简码
  */
 private String sysCode;

 /**
  * 提交的Hashcode
  */
 private String hashCode;

 /**
  * 评论信息
  */
 private String message;

 /**
  * 提交人用户名
  */
 private String submitterUsername;

 /**
  * 提交时间
  */
 private LocalDateTime submitTm;

 /**
  * 作者姓名
  */
 private String authorUsername;

 /**
  * 作者时间
  */
 private LocalDateTime authorTm;

 /**
  * 减少行数
  */
 private Integer lineNumSub;

 /**
  * 新增行数
  */
 private Integer lineNumAdd;

 /**
  * 净新增行数
  */
 private Integer lineNumNetAdd;

 /**
  * 总变更行数
  */
 private Integer lineNumChange;

 /**
  * merge造成的提交1是 0否
  */
 private Boolean mergeCommit;

 /**
  * 任务ID
  */
 private String taskId;

}
