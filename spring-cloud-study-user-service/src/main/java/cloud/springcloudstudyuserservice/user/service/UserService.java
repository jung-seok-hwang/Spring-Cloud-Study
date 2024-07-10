package cloud.springcloudstudyuserservice.user.service;

import cloud.springcloudstudyuserservice.user.dto.UserDto;
import cloud.springcloudstudyuserservice.user.vo.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserDto userDto);
}
