package com.lab.basic.aware.environment

import org.springframework.core.env.ConfigurableEnvironment
import org.springframework.core.env.MapPropertySource
import org.springframework.stereotype.Component

@Component
class DynamicEnvironmentUpdater(private val environment: ConfigurableEnvironment) {

    fun updateProperty(key: String, value: String) {
        val propertySources = environment.propertySources
        val targetPropertySourceName = "dynamicProperties"

        //  기존에 등록된 동적 프로퍼티가 있는지 확인
        val existingSource = propertySources.get(targetPropertySourceName) as? MapPropertySource

        if (existingSource != null) {
            //  기존 맵을 변경 가능한 맵으로 변환하여 업데이트
            val mutableMap = existingSource.source.toMutableMap()
            mutableMap[key] = value

            //  업데이트된 맵을 다시 설정
            propertySources.replace(targetPropertySourceName, MapPropertySource(targetPropertySourceName, mutableMap))
        } else {
            //  새로운 동적 프로퍼티 소스를 추가
            val newSource = MapPropertySource(targetPropertySourceName, mutableMapOf(key to value) as Map<String, Any>)
            propertySources.addFirst(newSource) // 최우선 순위로 등록
        }

        println("환경 변수 업데이트 완료: $key = $value")
    }
}