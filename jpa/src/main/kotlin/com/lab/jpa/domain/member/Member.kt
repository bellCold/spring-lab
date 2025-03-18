package com.lab.jpa.domain.member

import com.lab.jpa.domain.team.Team
import jakarta.persistence.*

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "member_id")
    val id: Long = 0,
    val name: String,
    val email: String,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    val team: Team
)