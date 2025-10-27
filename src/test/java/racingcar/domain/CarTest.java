package racingcar.domain; // 1. 패키지 선언이 올바른지 확인

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import racingcar.util.RandomNumberGenerator; // 2. RandomNumberGenerator import

import static org.assertj.core.api.Assertions.assertThat; // 3. AssertJ import

class CarTest {

    private Car car;

    @BeforeEach
    void setUp() {
        // 각 테스트 전에 "pobi"라는 이름의 새 차를 생성
        car = new Car("pobi");
    }

    @Test
    @DisplayName("자동차가 생성될 때 이름이 올바르게 설정된다")
    void getName() {
        // when & then
        assertThat(car.getName()).isEqualTo("pobi");
    }

    @Test
    @DisplayName("자동차가 생성될 때 초기 위치는 0이다")
    void getPosition() {
        // when & then
        assertThat(car.getPosition()).isEqualTo(0);
    }

    @Test
    @DisplayName("move: 랜덤 숫자가 4 이상이면 (OFFSET 4 이상) 위치가 1 증가한다")
    void move_forward() {
        // given
        // 5를 반환하는 가짜(Stub) RandomNumberGenerator 생성
        // 익명 클래스 사용: RandomNumberGenerator를 상속받아 generate() 메서드를 오버라이드
        RandomNumberGenerator stubMovingRng = new RandomNumberGenerator() {
            @Override
            public int generate() {
                return 4; // 5 > 4 (OFFSET)
            }
        };

        // when
        car.move(stubMovingRng); // 가짜 RNG 주입

        // then
        assertThat(car.getPosition()).isEqualTo(1);

        // when (한 번 더 이동)
        car.move(stubMovingRng);

        // then
        assertThat(car.getPosition()).isEqualTo(2);
    }

    @Test
    @DisplayName("move: 랜덤 숫자가 4 미만이면 (OFFSET 4 미만) 위치가 변하지 않는다 (경계값 4)")
    void move_stop_boundary() {
        // given
        // 4를 반환하는 가짜(Stub) RandomNumberGenerator 생성
        RandomNumberGenerator stubStoppingRng = new RandomNumberGenerator() {
            @Override
            public int generate() {
                return 3;
            }
        };

        // when
        car.move(stubStoppingRng); // 가짜 RNG 주입

        // then
        assertThat(car.getPosition()).isEqualTo(0); // 멈춤
    }
}