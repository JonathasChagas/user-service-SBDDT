package academy.devdojo.user_service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPostRequest {
    @JsonProperty("name")
    @NotBlank(message = "The field 'name' is required")
    private String firstName;
    @JsonProperty("second name")
    @NotBlank(message = "The field 'second name' is required")
    private String lastName;
    @JsonProperty("email")
    @NotBlank(message = "The field 'email' is required")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "e-mail is not valid")
    private String email;
}
