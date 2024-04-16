package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInDTO {
    private String email;
    private String password;
}
