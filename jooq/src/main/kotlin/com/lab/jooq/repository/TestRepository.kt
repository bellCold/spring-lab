package com.lab.jooq.repository

import org.jooq.DSLContext
import org.springframework.stereotype.Repository

@Repository
class TestRepository(private val dslContext: DSLContext) {
    fun test() {
    }
}