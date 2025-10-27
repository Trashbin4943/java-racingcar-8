package racingcar.util; // 1. 패키지가 일치해야 합니다.

import camp.nextstep.edu.missionutils.test.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

// 2. 필요한 static 메서드를 import 합니다.
import static camp.nextstep.edu.missionutils.test.Assertions.assertRandomNumberInRangeTest;
import static org.assertj.core.api.Assertions.assertThat;

class RandomNumberGeneratorTest {

    @Test
    @DisplayName("generate가 고정된 값을 정확히 반환하는지 확인")
    void generate_returnsFixedNumber() {
        // given
        int expectedNumber = 5; // 5가 반환되도록 고정
        RandomNumberGenerator generator = new RandomNumberGenerator();

        assertRandomNumberInRangeTest(
                () -> {
                    int actualNumber = generator.generate();
                    assertThat(actualNumber).isEqualTo(expectedNumber);
                },
                expectedNumber
        );
    }

    @Test
    @DisplayName("generate가 여러 번 호출될 때 순서대로 값을 반환하는지 확인")
    void generate_returnsMultipleValuesInOrder() {
        // given
        int expectedFirst = 8;
        int expectedSecond = 3;
        RandomNumberGenerator generator = new RandomNumberGenerator();

        // when & then
        assertRandomNumberInRangeTest(
                () -> {
                    int actualFirst = generator.generate();
                    int actualSecond = generator.generate();

                    assertThat(actualFirst).isEqualTo(expectedFirst);
                    assertThat(actualSecond).isEqualTo(expectedSecond);
                },
                expectedFirst,
                expectedSecond
        );
    }
}