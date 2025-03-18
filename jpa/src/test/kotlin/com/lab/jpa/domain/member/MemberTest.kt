package com.lab.jpa.domain.member

import com.lab.jpa.domain.team.Team
import com.lab.jpa.domain.team.TeamRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.annotation.Rollback
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
@Rollback(false)
class MemberTest {
    @Autowired lateinit var memberRepository: MemberRepository
    @Autowired lateinit var teamRepository: TeamRepository

    @Test
    fun `멤버 저장 및 조회 테스트`() {
        // Given
        val team = teamRepository.save(Team(name = "temA"))
        val member = Member(name = "bellCold", email = "bellCold@example.com", team = team)

        // When
        val savedMember = memberRepository.save(member)
        val foundMember = memberRepository.findById(savedMember.id).orElse(null)

        // Then
        assertThat(foundMember).isNotNull
        assertThat(foundMember?.name).isEqualTo("bellCold")
        assertThat(foundMember?.email).isEqualTo("bellCold@example.com")
        assertThat(foundMember?.team?.name).isEqualTo("temA")
    }
}