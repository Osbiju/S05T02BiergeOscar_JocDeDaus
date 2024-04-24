package cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.domain;

import cat.itacademy.barcelonactiva.BiergeOscar.s05t02.model.enums.Role;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Document(collection = "Users")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails{
    @Id
    private String email;
    @JsonProperty(access =  JsonProperty.Access.WRITE_ONLY)
    private String name;
    private String lastName;
    private String password;

    private Role role;

    // this returns a list of roles
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
