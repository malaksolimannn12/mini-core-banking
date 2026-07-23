package com.bank.minicorebanking.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public User(String username, String password, Role role, Customer customer) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.customer = customer;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}




//Simple version:
//
//`Role role;` is just a field that can only be `ADMIN` or `CUSTOMER` — nothing else.
//
//MySQL doesn't understand Java enums, so Hibernate has to save it as *something*. Without `@Enumerated(EnumType.STRING)`, it would save it as a hidden number (`0` or `1`) — hard to read, and risky if you ever add more roles later.
//
//`@Enumerated(EnumType.STRING)` just says: **"save it as the actual word — `'ADMIN'` or `'CUSTOMER'` — in the database, not a number."**
//
//That's it. One line, one job: makes your database readable and safe.
//what is the haibrnate
//You speak Java. MySQL only speaks SQL. Hibernate is the translator standing between you two, so you never have to write raw SQL yourself — you just work with normal Java objects (Customer customer = new Customer()), and Hibernate handles turning that into the right SQL behind the scenes.
