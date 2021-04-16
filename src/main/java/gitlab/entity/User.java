package gitlab.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author DJF
 * @version 0.1.0
 * @Description
 * @create 2020-10-16 14:44
 * @since 0.1.0
 **/
@Data
@Builder
@AllArgsConstructor
public class User extends BaseEntity implements Serializable {

    private String id;
    private String username;
    private String password;
    private String email;
    private String name;
    private String private_token;
    private String state;
    private String created_at;
    private String bio;
    private String skype;
    private String linkedin;
    private String twitter;
    private String website_url;
    private String extern_uid;
    private String provider;
    private String theme_id;
    private String color_scheme_id;
    private String is_admin;
    private String avatar_url;
    private String can_create_group;
    private String can_create_project;
    private String projects_limit;
    private String current_sign_in_at;
    private String two_factor_enabled;
}
