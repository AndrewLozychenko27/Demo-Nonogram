package ua.lozychenko.nonogram.data.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.lozychenko.nonogram.constraint.PasswordConfirmation;
import ua.lozychenko.nonogram.constraint.StrongPassword;
import ua.lozychenko.nonogram.constraint.UniqueEmail;
import ua.lozychenko.nonogram.constraint.group.CredentialsGroup;
import ua.lozychenko.nonogram.constraint.group.PasswordGroup;

import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity(name = "users")
@UniqueEmail(groups = CredentialsGroup.class)
@PasswordConfirmation(groups = PasswordGroup.class)
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private Long id;

    @NotEmpty(message = "Nickname is required", groups = CredentialsGroup.class)
    @Length(max = 256, message = "Nickname must be no longer than {max} characters", groups = CredentialsGroup.class)
    @Pattern(regexp = "^\\w+$", message = "Nickname must contain only a-z, A-Z, 0-9 and _", groups = CredentialsGroup.class)
    private String nickname;

    @NotEmpty(message = "Email is required", groups = CredentialsGroup.class)
    @Length(max = 256, message = "Email must be no longer than {max} characters", groups = CredentialsGroup.class)
    @Pattern(regexp = "^\\w+@\\w{3,}\\.\\w{2,}$", message = "Email must match \"example@your.org\"", groups = CredentialsGroup.class)
    private String email;

    @NotEmpty(message = "Password is required", groups = PasswordGroup.class)
    @Length(min = 8, max = 256, message = "Password must be from {min} to {max} characters long", groups = PasswordGroup.class)
    @StrongPassword(groups = PasswordGroup.class)
    @ToString.Exclude
    private String password;

    @Transient
    @NotEmpty(message = "Password confirmation is required", groups = PasswordGroup.class)
    @ToString.Exclude
    private String passwordConfirmation;
    private Boolean activated = true;

    private Integer score = 0;

    @Enumerated(EnumType.STRING)
    private Role role = Role.PLAYER;

    public User(String nickname, String email, String password, String passwordConfirmation) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    public Boolean isActivated() {
        return activated;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(getRole());
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return isActivated();
    }

    @Override
    public boolean isAccountNonLocked() {
        return isActivated();
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isActivated();
    }

    @Override
    public boolean isEnabled() {
        return isActivated();
    }
}