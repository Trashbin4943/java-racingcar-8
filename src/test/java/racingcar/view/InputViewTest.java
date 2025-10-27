package racingcar.view;

import camp.nextstep.edu.missionutils.Console;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class InputViewTest {

    // 표준 입출력을 백업하고 복원하기 위한 변수
    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final InputStream standardIn = System.in;

    // 콘솔 출력을 캡처하기 위한 변수
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    private InputView inputView;

    @BeforeEach
    void setUp() {
        // System.out과 System.err를 ByteArrayOutputStream으로 리디렉션
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));

        inputView = new InputView();
    }

    @AfterEach
    void tearDown() {
        // 표준 입출력을 원래대로 복원
        System.setOut(standardOut);
        System.setErr(standardErr);
        System.setIn(standardIn);
        // missionutils의 Console 리소스를 닫아줌
        Console.close();
    }

    /**
     * 테스트할 입력을 System.in에 주입하는 헬퍼 메서드
     *
     * @param input 시뮬레이션할 사용자 입력 문자열
     */
    private void setSystemIn(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    // --- readCarNames() 테스트 ---

    @Test
    @DisplayName("유효한 자동차 이름들을 입력받는다")
    void readCarNames_success() {
        // given
        String input = "pobi,woni,jun";
        setSystemIn(input);

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni", "jun");
        assertThat(outContent.toString()).contains("경주할 자동차 이름을 입력하세요.");
        assertThat(errContent.toString()).isEmpty(); // 에러 출력 없음
    }

    @Test
    @DisplayName("자동차 이름 입력 시 앞뒤 공백을 제거한다")
    void readCarNames_withWhitespace_trimmed() {
        // given
        String input = "  pobi , woni , jun  ";
        setSystemIn(input);

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni", "jun");
    }

    @Test
    @DisplayName("자동차 이름이 5자를 초과하면 에러 메시지 출력 후 재입력 받는다")
    void readCarNames_nameTooLong_retry() {
        // given
        String invalidInput = "longName,pobi";
        String validInput = "pobi,woni";
        setSystemIn(invalidInput + "\n" + validInput); // 잘못된 입력 후 올바른 입력

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni"); // 최종적으로 올바른 값을 반환
        assertThat(errContent.toString()).contains("[ERROR] 자동차 이름은 5자 이하만 가능합니다.");
        assertThat(outContent.toString().trim().split("\n"))
                .filteredOn(s -> s.contains("경주할 자동차 이름을 입력하세요."))
                .hasSize(2); // 프롬프트가 2번 출력됨
    }

    @Test
    @DisplayName("자동차 이름이 공백이면 에러 메시지 출력 후 재입력 받는다")
    void readCarNames_nameIsBlank_retry() {
        // given
        String invalidInput = "pobi,,woni"; // 중간에 빈 이름
        String validInput = "pobi,woni";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni");
        assertThat(errContent.toString()).contains("[ERROR] 자동차 이름은 공백일 수 없습니다.");
    }

    @Test
    @DisplayName("입력이 아예 없으면(빈 문자열) 에러 메시지 출력 후 재입력 받는다")
    void readCarNames_noCars_retry() {
        // given
        String invalidInput = ""; // 그냥 엔터
        String validInput = "pobi,woni";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni");
        assertThat(errContent.toString()).contains("[ERROR] 1대 이상의 자동차 이름을 입력해야 합니다.");
    }

    // --- readAttemptCount() 테스트 ---

    @Test
    @DisplayName("유효한 시도 횟수를 입력받는다")
    void readAttemptCount_success() {
        // given
        setSystemIn("5");

        // when
        int count = inputView.readAttemptCount();

        // then
        assertThat(count).isEqualTo(5);
        assertThat(outContent.toString()).contains("시도할 횟수는 몇 회인가요?");
        assertThat(errContent.toString()).isEmpty(); // 에러 출력 없음
    }

    @Test
    @DisplayName("시도 횟수가 숫자가 아니면 에러 메시지 출력 후 재입력 받는다")
    void readAttemptCount_notANumber_retry() {
        // given
        String invalidInput = "abc"; // 숫자가 아님
        String validInput = "3";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        int count = inputView.readAttemptCount();

        // then
        assertThat(count).isEqualTo(3);
        assertThat(errContent.toString()).contains("[ERROR] 유효한 숫자를 입력해야 합니다.");
        assertThat(outContent.toString().trim().split("\n"))
                .filteredOn(s -> s.contains("시도할 횟수는 몇 회인가요?"))
                .hasSize(2); // 프롬프트가 2번 출력됨
    }

    @Test
    @DisplayName("시도 횟수가 0이면 에러 메시지 출력 후 재입력 받는다")
    void readAttemptCount_zero_retry() {
        // given
        String invalidInput = "0"; // 0은 유효하지 않음
        String validInput = "3";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        int count = inputView.readAttemptCount();

        // then
        assertThat(count).isEqualTo(3);
        assertThat(errContent.toString()).contains("[ERROR] 시도 횟수는 1 이상의 숫자여야 합니다.");
    }

    @Test
    @DisplayName("시도 횟수가 음수이면 에러 메시지 출력 후 재입력 받는다")
    void readAttemptCount_negative_retry() {
        // given
        String invalidInput = "-5"; // 음수는 유효하지 않음
        String validInput = "3";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        int count = inputView.readAttemptCount();

        // then
        assertThat(count).isEqualTo(3);
        assertThat(errContent.toString()).contains("[ERROR] 시도 횟수는 1 이상의 숫자여야 합니다.");
    }
}