package br.com.alura.techcase.api.model;

import br.com.alura.techcase.api.dto.user.CreateUserForm;
import br.com.alura.techcase.core.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, length = 20, nullable = false)
    private String username;

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private Role role;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private LocalDate creationDate;

    public User(CreateUserForm form, PasswordEncoder encoder) {
        this.username = form.username();
        this.name = form.name();
        this.email = form.email();
        this.role = setRoleByString(form.role());
        this.password = encoder.encode(form.password());
        this.creationDate = LocalDate.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id == user.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    private Role setRoleByString(String formRole) {
        return Arrays.stream(Role.values()).filter(roleValue -> roleValue.name().equals(formRole)).findFirst().orElseThrow(() -> new IllegalArgumentException("Invalid role " + formRole));
    }

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
