package net.dg.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data

@Entity
@Table( name="user",
        uniqueConstraints={@UniqueConstraint(columnNames={"email"})})
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(min = 2, max = 30, message = "First Name should be between 2 and 30 characters.")
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Size(min = 2, max = 30, message = "Last Name should be between 2 and 30 characters.")
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty(message = "Email should not be empty.")
    @Email(message = "Email should be valid")
    @Column(nullable = false)
    private String email;

    @NotEmpty(message = "Email should not be empty.")
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserRole role = UserRole.USER;

    @Column(name = "isNonLocked")
    @Builder.Default
    private boolean isNonLocked = false;

    @NotEmpty(message = "City should not be empty.")
    private String city;

    @NotEmpty(message = "Street should not be empty.")
    private String street;

    @NotEmpty(message = "Street number should not be empty.")
    @Column(name = "street_Number")
    private String streetNumber;

    @NotEmpty(message = "Phone number should not be empty.")
    @Column(name = "phone_number")
    private String phoneNumber;

    @OneToOne(optional = false, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id")
    private Cart cart;


    @Override
    public List<UserRole> getAuthorities() {
        return new ArrayList<>(Collections.singleton(role));
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
        return isNonLocked;
    }

    public void setAccountLocked() {
        isNonLocked = false;
    }

    public void setAccountUnLocked() {
        isNonLocked = true;
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
