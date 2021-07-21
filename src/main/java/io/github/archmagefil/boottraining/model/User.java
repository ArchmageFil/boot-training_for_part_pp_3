package io.github.archmagefil.boottraining.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String surname;
    @Column(nullable = false, unique = true)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(name = "isGoodAcc")
    private Boolean GoodAcc = null;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "roles_id"))
    private List<Role> roles = new ArrayList<>();


    public User(String name, String surname, String email, String password, boolean isGoodAcc, List<Role> roles) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
        this.GoodAcc = isGoodAcc;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return GoodAcc;
    }

    @Override
    public boolean isAccountNonLocked() {
        return GoodAcc;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return GoodAcc;
    }

    @Override
    public boolean isEnabled() {
        return GoodAcc;
    }
}