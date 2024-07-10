package cloud.springcloudstudyuserservice.user.dto;

import lombok.*;


@Getter
@ToString
@Setter
@RequiredArgsConstructor
public class UserDto {

    private String email;

    private String name;

    private String password;

    private String userId;

    private String encryptedPassword;

    public void userSecretId(String userId) {
        this.userId = userId;
    }

    public void secretEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
}
