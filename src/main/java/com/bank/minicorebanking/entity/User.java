package com.bank.minicorebanking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
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

    private Role role;

    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
}