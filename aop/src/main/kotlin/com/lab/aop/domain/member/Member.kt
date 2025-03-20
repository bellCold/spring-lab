package com.lab.aop.domain.member

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    val id: Long,
    val money: BigDecimal
) {
}