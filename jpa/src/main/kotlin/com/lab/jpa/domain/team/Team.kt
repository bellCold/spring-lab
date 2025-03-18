package com.lab.jpa.domain.team

import jakarta.persistence.*

@Entity
class Team(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "team_id")
    val id: Long = 0,
    val name: String
) {
}