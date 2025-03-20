package com.lab.aop.domain.account

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
class Account(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    val id: Long = 0,
    val name: String,
    var money: BigDecimal
) {
}