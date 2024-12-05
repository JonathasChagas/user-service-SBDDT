package academy.devdojo.user_service.mapper;

import academy.devdojo.user_service.domain.User;
import academy.devdojo.user_service.request.UserPostRequest;
import academy.devdojo.user_service.request.UserPutRequest;
import academy.devdojo.user_service.response.UserGetResponse;
import academy.devdojo.user_service.response.UserPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserMapper {

    User toUser(UserPostRequest userPostRequest);

    User toUser(UserPutRequest userPutRequest);

    UserPostResponse toUserPostResponse(User user);

    UserGetResponse toUserGetResponse(User user);

    List<UserGetResponse> toUserGetResponseList(List<User> users);
}
