package cloud.springcloudstudyuserservice.user.service;

import cloud.springcloudstudyuserservice.user.controller.request.UserDto;
import cloud.springcloudstudyuserservice.user.controller.request.UserRequest;
import cloud.springcloudstudyuserservice.user.controller.response.UserResponse;

public interface UserService {

    UserResponse createUser(UserDto userDto);
}
