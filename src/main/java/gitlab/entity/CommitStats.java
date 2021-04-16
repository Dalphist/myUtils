package gitlab.entity;

import lombok.Data;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2020-12-11 16:31
 * @since 0.1.0
 **/
@Data
public class CommitStats {
    private Integer additions;
    private Integer deletions;
    private Integer total;

}
