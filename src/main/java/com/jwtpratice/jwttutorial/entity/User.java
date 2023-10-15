package com.jwtpratice.jwttutorial.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tbl_member")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id
    @Column(name="m_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;


}
