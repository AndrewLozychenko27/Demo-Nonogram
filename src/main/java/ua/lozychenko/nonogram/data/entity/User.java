package ua.lozychenko.nonogram.data.entity;

import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import ua.lozychenko.nonogram.constraint.PasswordConfirmation;
import ua.lozychenko.nonogram.constraint.StrongPassword;
import ua.lozychenko.nonogram.constraint.UniqueEmail;
import ua.lozychenko.nonogram.constraint.group.CredentialsGroup;
import ua.lozychenko.nonogram.constraint.group.PasswordGroup;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Collection;
import java.util.List;

@Entity(name = "users")
@PasswordConfirmation(groups = PasswordGroup.class)
public class User implements UserDetails {
    @Id
    @SequenceGenerator(name = "users_seq", sequenceName = "users_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_seq")
    private Long id;

    @NotEmpty(message = "Nickname is required", groups = CredentialsGroup.class)
    @Length(min = 5, max = 256, message = "Nickname must be from {min} to {max} characters long", groups = CredentialsGroup.class)
    @Pattern(regexp = "^\\w+$", message = "Nickname must contain only a-z, A-Z, 0-9 and _", groups = CredentialsGroup.class)
    private String nickname;

    @NotEmpty(message = "Email is required", groups = CredentialsGroup.class)
    @Length(min = 8, max = 256, message = "Email must be from {min} to {max} characters long", groups = CredentialsGroup.class)
    @Pattern(regexp = "^\\w+@\\w{3,}\\.\\w{2,}$", message = "Email must match \"example@your.org\"", groups = CredentialsGroup.class)
    @UniqueEmail(groups = UniqueEmail.class)
    private String email;

    @NotEmpty(message = "Password is required", groups = PasswordGroup.class)
    @Length(min = 8, max = 256, message = "Password must be from {min} to {max} characters long", groups = PasswordGroup.class)
    @StrongPassword(groups = PasswordGroup.class)
    private String password;

    @Transient
    @NotEmpty(message = "Password confirmation is required", groups = PasswordGroup.class)
    private String passwordConfirmation;
    private Boolean activated;
    @Enumerated(EnumType.STRING)
    private Role role;

    public User() {
    }

    public User(String nickname, String email, String password, String passwordConfirmation, Boolean activated, Role role) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
        this.activated = false;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }

    public Boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(getRole());
    }

    @Override
    public String getUsername() {
        return getNickname();
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