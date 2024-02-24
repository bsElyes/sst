package tn.example.sst.rest.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoginDTO {
    @NotNull
    @Size(min = 1, max = 50)
    private String username;
    @NotNull
    @Size(min = 4, max = 100)
    private String password;

    private boolean rememberMe;
}
