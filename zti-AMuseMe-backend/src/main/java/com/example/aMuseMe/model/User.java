package com.example.aMuseMe.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.Period;

@Entity
@Table(name = "user_account",
    uniqueConstraints = {
            @UniqueConstraint(columnNames = "email")
    })
@Data
@NoArgsConstructor
public class User {
    @Id
    @SequenceGenerator(
            name = "user_sequence",
            sequenceName = "user_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "user_sequence"
    )
    private Long id;
    @NotBlank
    @Size(max = 50)
    @Email
    private String email;
    @NotBlank
    @Size(max = 150)
    private String password;
    @NotBlank
    @Size(max = 30)
    private String nickname;
    private LocalDate birthdate;
    @Transient
    private int age;

    public User(Long id, String email, String password, String nickname) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }


//    @Override
    public int getAge() {
        if (this.birthdate != null) {
            return Period.between(this.birthdate, LocalDate.now()).getYears();
        }
        return 0;
    }

}
