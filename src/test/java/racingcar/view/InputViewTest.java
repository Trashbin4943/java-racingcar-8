package racingcar.view;

import camp.nextstep.edu.missionutils.Console;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class InputViewTest {

    private final InputStream standardIn = System.in;
    private InputView inputView;

    @BeforeEach
    void setUp() {
        inputView = new InputView();
    }

    @AfterEach
    void tearDown() {
        System.setIn(standardIn);
        Console.close();
    }


    private void setSystemIn(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }


    @Test
    @DisplayName("정상적인 자동차 이름을 입력받는다")
    void readCarNames_success() {
        // given
        setSystemIn("pobi,woni,jun");

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni", "jun");
    }

    @Test
    @DisplayName("자동차 이름이 5자를 초과하면 IllegalArgumentException을 발생시킨다")
    void readCarNames_fail_nameTooLong() {
        // given
        String invalidInput = "pobi,woni,longname"; // "longname"이 5자 초과
        setSystemIn(invalidInput);

        // when & then
        // 3. inputView.readCarNames()를 실행할 때
        assertThatThrownBy(() -> inputView.readCarNames())
                .isInstanceOf(IllegalArgumentException.class) // 4. 예외가 발생하는지 확인
                .hasMessageContaining("[ERROR] 자동차 이름은 5자 이하만 가능합니다."); // 5. 에러 메시지 확인
    }

    @Test
    @DisplayName("자동차 이름이 공백이면 IllegalArgumentException을 발생시킨다")
    void readCarNames_fail_nameIsBlank() {
        // given
        String invalidInput = "pobi,,woni"; // 중간에 빈 이름
        setSystemIn(invalidInput);

        // when & then
        assertThatThrownBy(() -> inputView.readCarNames())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 자동차 이름은 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("입력이 아예 없으면 (빈 문자열) IllegalArgumentException을 발생시킨다")
    void readCarNames_fail_noCars() {
        // given
        String invalidInput = "\n"; // 그냥 엔터
        setSystemIn(invalidInput);

        // when & then
        assertThatThrownBy(() -> inputView.readCarNames())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 1대 이상의 자동차 이름을 입력해야 합니다.");
    }


    @Test
    @DisplayName("정상적인 시도 횟수를 입력받는다")
    void readAttemptCount_success() {
        // given
        setSystemIn("5");

        // when
        int count = inputView.readAttemptCount();

        // then
        assertThat(count).isEqualTo(5);
    }

    @Test
    @DisplayName("시도 횟수가 숫자가 아니면 IllegalArgumentException을 발생시킨다")
    void readAttemptCount_fail_notANumber() {
        // given
        String invalidInput = "abc"; // 숫자가 아님
        setSystemIn(invalidInput);

        // when & then
        assertThatThrownBy(() -> inputView.readAttemptCount())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 유효한 숫자를 입력해야 합니다.");
    }

    @Test
    @DisplayName("시도 횟수가 0이면 IllegalArgumentException을 발생시킨다")
    void readAttemptCount_fail_zero() {
        // given
        String invalidInput = "0"; // 0은 1 이상이 아님
        setSystemIn(invalidInput);

        // when & then
        assertThatThrownBy(() -> inputView.readAttemptCount())
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 시도 횟수는 1 이상의 숫자여야 합니다.");
    }
}