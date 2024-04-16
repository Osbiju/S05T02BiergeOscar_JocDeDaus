package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUpDTO {
    private String name;
    private String lastName;
    private String email;
    private String password;
}
