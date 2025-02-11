package academy.devdojo.user_service.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserPutRequest {
    @NotNull(message = "The field 'id' cannot be null")
    private Long id;
    @JsonProperty("name")
    @NotBlank(message = "The field 'name' is required")
    private String firstName;
    @JsonProperty("second name")
    @NotBlank(message = "The field 'second name' is required")
    private String lastName;
    @JsonProperty("email")
    @NotBlank(message = "The field 'email' is required ")
    @Email(regexp = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "The e-mail is not valid")
    private String email;
}
