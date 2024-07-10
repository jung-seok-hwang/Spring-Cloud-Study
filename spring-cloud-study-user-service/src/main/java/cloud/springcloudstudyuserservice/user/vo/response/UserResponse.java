package cloud.springcloudstudyuserservice.user.vo.response;


import cloud.springcloudstudyuserservice.user.entity.User;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserResponse {

    private String email;

    private String userId;

    private String name;

    private String password;

    private String encryptedPassword;

    public static UserResponse of(User user) {
        return UserResponse.builder()
                .email(user.getEmail())
                .userId(user.getUserId())
                .name(user.getName())
                .password(user.getEncryptedPassword())
                .encryptedPassword(user.getEncryptedPassword()).build();

    }
}
