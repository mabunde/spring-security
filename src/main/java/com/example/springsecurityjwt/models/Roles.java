package com.example.springsecurityjwt.models;

import lombok.*;

import javax.persistence.*;

@Entity
@Data @NoArgsConstructor @AllArgsConstructor
@Setter @Getter
public class Roles {
    @Id
    @SequenceGenerator(
            name = "role_sequence",
            sequenceName = "role_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "role_sequence"
    )
    private Long id;
    private String name;
}
