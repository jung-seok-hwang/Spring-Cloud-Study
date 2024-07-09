package cloud.springcloudstudyuserservice.user.controller.request;

import lombok.*;


@Getter
@ToString
@RequiredArgsConstructor
public class UserDto {

    private String email;

    private String name;

    private String password;

    private String userId;

}
