package com.lab.basic.conditional

import org.springframework.context.annotation.Condition
import org.springframework.context.annotation.ConditionContext
import org.springframework.core.type.AnnotatedTypeMetadata

abstract class BaseProfileCondition : Condition {
    override fun matches(context: ConditionContext, metadata: AnnotatedTypeMetadata): Boolean {
        val activeProfiles = context.environment.activeProfiles
        val targetProfiles = getTargetProfiles()

        return activeProfiles.any {
            isActiveProfileMatchedWithTargetProfiles(it, targetProfiles)
        }
    }

    abstract fun getTargetProfiles(): List<Profile>

    private fun isActiveProfileMatchedWithTargetProfiles(activeProfile: String, targetProfile: List<Profile>): Boolean =
        targetProfile.any { it.isEqualTo(activeProfile) }
}

class LocalProfileCondition : BaseProfileCondition() {
    override fun getTargetProfiles(): List<Profile> = listOf(Profile.LOCAL)
}

enum class Profile(
    val value: String
) {
    LOCAL("local"),
    DEVELOP("develop"),
    QA("qa"),
    STAGE("stage"),
    PROD("prod");

    fun isEqualTo(profile: String): Boolean = (this.value == profile)
}