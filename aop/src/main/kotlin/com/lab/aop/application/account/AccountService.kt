package com.lab.aop.application.account

import com.lab.aop.domain.account.Account
import com.lab.aop.domain.account.AccountRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class AccountService(private val accountRepository: AccountRepository) {

    // 기본 트랜잭션 설정
    @Transactional
    fun createAccount(name: String, money: BigDecimal): Account {
        val account = Account(name = name, money = money)
        return accountRepository.save(account)
    }

    // 읽기 전용 트랜잭션 설정
    @Transactional(readOnly = true)
    fun getAccount(id: Long): Account {
        return accountRepository.findById(id).orElseThrow { NoSuchElementException("Account not found") }
    }

    // 격리 수준 설정
    @Transactional(isolation = Isolation.SERIALIZABLE)
    fun transferMoney(fromId: Long, toId: Long, amount: BigDecimal) {
        val fromAccount = accountRepository.findById(fromId).orElseThrow()
        val toAccount = accountRepository.findById(toId).orElseThrow()

        if (fromAccount.money < amount) {
            throw IllegalStateException("잔액이 부족합니다")
        }

        fromAccount.money -= amount
        toAccount.money += amount
    }

    // 전파 속성 설정
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun withdrawMoney(id: Long, amount: BigDecimal) {
        val account = accountRepository.findById(id).orElseThrow()

        if (account.money < amount) {
            throw IllegalStateException("잔액이 부족합니다")
        }

        account.money -= amount
    }

    // 롤백 예외 설정
    @Transactional
    fun depositWithValidation(id: Long, amount: BigDecimal): Account {
        if (amount <= BigDecimal.ZERO) {
            throw IllegalArgumentException("입금액은 0보다 커야 합니다")
        }

        val account = accountRepository.findById(id).orElseThrow()
        account.money += amount
        return account
    }

    // 타임아웃 설정
    @Transactional(timeout = 10)
    fun performLongRunningTransaction(id: Long): Account {
        val account = accountRepository.findById(id).orElseThrow()
        // 장시간 실행되는 로직 (10초가 넘으면 트랜잭션이 종료됨)
        Thread.sleep(5000) // 실제 프로덕션에서는 이런 방식으로 사용하지 않음
        account.money += BigDecimal(100)
        return account
    }

    // 중첩 트랜잭션 예제
    @Transactional
    fun complexOperation(fromId: Long, toId: Long, amount: BigDecimal) {
        // 이 메서드는 기본 트랜잭션 속성을 가짐
        val fromAccount = getAccount(fromId) // readOnly = true 트랜잭션 메서드 호출

        try {
            withdrawMoney(fromId, amount) // REQUIRES_NEW 트랜잭션 메서드 호출
            depositMoney(toId, amount)
        } catch (e: Exception) {
            // 트랜잭션이 롤백됨
            println("트랜잭션 실패: ${e.message}")
            throw e
        }
    }

    @Transactional
    fun depositMoney(id: Long, amount: BigDecimal): Account {
        val account = accountRepository.findById(id).orElseThrow()
        account.money += amount
        return account
    }
}