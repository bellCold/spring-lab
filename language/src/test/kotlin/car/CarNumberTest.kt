package car

import com.lab.car.CarNumber
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows


class CarNumberTest {

    @Test
    fun `정상적인 신형 번호판은 생성된다`() {
        val carNumber = CarNumber("123가4567")
        assertEquals("123가4567", carNumber.value)
    }

    @Test
    fun `정상적인 구형 번호판은 생성된다`() {
        val carNumber = CarNumber("서울45가1234")
        assertEquals("서울45가1234", carNumber.value)
    }

    @Test
    fun `구형 번호판이지만 등록 지역이 없으면 예외 발생`() {
        val exception = assertThrows<IllegalArgumentException> {
            CarNumber("무효45가1234")
        }
        assertEquals("알 수 없는 등록 지역입니다.", exception.message)
    }

    @Test
    fun `번호판 형식이 잘못되면 예외 발생`() {
        val exception = assertThrows<IllegalArgumentException> {
            CarNumber("12AB1234")
        }
        assertEquals("자동차 번호 형식을 확인해 주세요.", exception.message)
    }
}