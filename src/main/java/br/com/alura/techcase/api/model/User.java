package br.com.alura.techcase.api.model;

import br.com.alura.techcase.api.dto.user.CreateUserForm;
import br.com.alura.techcase.core.enums.Role;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Objects;

@Entity
@Getter
@NoArgsConstructor
public class User {

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

    public User(CreateUserForm form) {
        this.username = form.username();
        this.name = form.name();
        this.email = form.email();
        this.role = setRoleByString(form.role());
        this.password = form.password();
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
}
