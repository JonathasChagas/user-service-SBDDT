package academy.devdojo.user_service.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserGetResponse {
    @JsonProperty("name")
    private String firstName;
    @JsonProperty("second name")
    private String lastName;
    @JsonProperty("email")
    private String email;
}
