package com.lab.car

@JvmInline
value class CarNumber private constructor(val value: String) {
    companion object {
        private val CAR_NUMBER_REGEX = Regex("(\\d{2,3})([가-힣])(\\d{4})")
        private val OLD_CAR_NUMBER_REGEX = Regex("^([가-힣]{1,2})?(\\d{1,2})([가-힣])(\\d{4})\$")
        private val LOCATION_NAMES =
            setOf("서울", "부산", "대구", "인천", "광주", "대전", "울산", "경기", "강원", "충북", "충남", "전북", "전남", "경북", "경남", "제주")

        fun from(carNumber: String): CarNumber {
            return CarNumber(carNumber)
        }

        operator fun invoke(carNumber: String): CarNumber = from(carNumber)
    }

    init {
        validateCarNumber(value)
    }

    private fun validateCarNumber(number: String) {
        val oldCarNumberMatch = OLD_CAR_NUMBER_REGEX.matchEntire(number)
        if (oldCarNumberMatch != null) {
            val (location, _, _) = oldCarNumberMatch.destructured
            require(location in LOCATION_NAMES) { "알 수 없는 등록 지역입니다." }
        } else {
            require(CAR_NUMBER_REGEX.matches(number)) { "자동차 번호 형식을 확인해 주세요." }
        }
    }
}

