package academy.devdojo.user_service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPutRequest {
    private Long id;
    @JsonProperty("name")
    private String firstName;
    @JsonProperty("second name")
    private String lastName;
    @JsonProperty("email")
    private String email;
}
