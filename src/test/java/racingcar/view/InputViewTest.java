package racingcar.view; // 1. InputView와 동일한 패키지

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

    // 표준 입출력 스트림을 백업하기 위한 변수
    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final InputStream standardIn = System.in;

    // 콘솔 출력을 캡처하기 위한 스트림
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

        // 테스트 대상 객체 생성
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
     * System.in에 테스트할 입력을 주입하는 헬퍼 메서드
     *
     * @param input 시뮬레이션할 사용자 입력 문자열 (개행 문자로 여러 줄 입력 가능)
     */
    private void setSystemIn(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    /**
     * outContent(표준 출력)에 특정 문자열이 몇 번 출력되었는지 확인하는 헬퍼 메서드
     */
    private int getPromptCount(String prompt) {
        return (int) outContent.toString().lines()
                .filter(line -> line.contains(prompt))
                .count();
    }

    // --- readCarNames() 예외 처리 테스트 ---

    @Test
    @DisplayName("자동차 이름이 5자를 초과하면 [ERROR] 출력 후 재입력 받는다")
    void readCarNames_fail_nameTooLong() {
        // given
        String invalidInput = "pobi,woni,longname"; // "longname"이 5자 초과
        String validInput = "pobi,woni,jun";
        setSystemIn(invalidInput + "\n" + validInput); // 잘못된 입력 후 올바른 입력

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni", "jun"); // 1. 최종적으로 올바른 값을 반환
        assertThat(errContent.toString())
                .contains("[ERROR] 자동차 이름은 5자 이하만 가능합니다."); // 2. 에러 메시지 출력 확인
        assertThat(getPromptCount("경주할 자동차 이름을 입력하세요."))
                .isEqualTo(2); // 3. 프롬프트가 2번(실패 1, 성공 1) 출력됨
    }

    @Test
    @DisplayName("자동차 이름이 공백이면 [ERROR] 출력 후 재입력 받는다")
    void readCarNames_fail_nameIsBlank() {
        // given
        String invalidInput = "pobi,,woni"; // 중간에 빈 이름
        String validInput = "pobi,woni";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni");
        assertThat(errContent.toString())
                .contains("[ERROR] 자동차 이름은 공백일 수 없습니다.");
        assertThat(getPromptCount("경주할 자동차 이름을 입력하세요."))
                .isEqualTo(2);
    }

    @Test
    @DisplayName("입력이 아예 없으면 (빈 문자열) [ERROR] 출력 후 재입력 받는다")
    void readCarNames_fail_noCars() {
        // given
        String invalidInput = ""; // 그냥 엔터
        String validInput = "pobi,woni";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni");
        assertThat(errContent.toString())
                .contains("[ERROR] 1대 이상의 자동차 이름을 입력해야 합니다.");
        assertThat(getPromptCount("경주할 자동차 이름을 입력하세요."))
                .isEqualTo(2);
    }

    @Test
    @DisplayName("입력이 공백만 있으면 (trim 후 빈 문자열) [ERROR] 출력 후 재입력 받는다")
    void readCarNames_fail_allBlank() {
        // given
        String invalidInput = "  ,   ,  "; // 모두 공백
        String validInput = "pobi";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi");
        assertThat(errContent.toString())
                .contains("[ERROR] 1대 이상의 자동차 이름을 입력해야 합니다.");
        assertThat(getPromptCount("경주할 자동차 이름을 입력하세요."))
                .isEqualTo(2);
    }

    // --- readAttemptCount() 예외 처리 테스트 ---

    @Test
    @DisplayName("시도 횟수가 숫자가 아니면 [ERROR] 출력 후 재입력 받는다")
    void readAttemptCount_fail_notANumber() {
        // given
        String invalidInput = "abc"; // 숫자가 아님
        String validInput = "5";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        int count = inputView.readAttemptCount();

        // then
        assertThat(count).isEqualTo(5); // 1. 최종적으로 올바른 값을 반환
        assertThat(errContent.toString())
                .contains("[ERROR] 유효한 숫자를 입력해야 합니다."); // 2. 에러 메시지 출력 확인
        assertThat(getPromptCount("시도할 횟수는 몇 회인가요?"))
                .isEqualTo(2); // 3. 프롬프트가 2번 출력됨
    }

    @Test
    @DisplayName("시도 횟수가 0이면 [ERROR] 출력 후 재입력 받는다")
    void readAttemptCount_fail_zero() {
        // given
        String invalidInput = "0"; // 0은 1 이상이 아님
        String validInput = "1";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        int count = inputView.readAttemptCount();

        // then
        assertThat(count).isEqualTo(1);
        assertThat(errContent.toString())
                .contains("[ERROR] 시도 횟수는 1 이상의 숫자여야 합니다.");
        assertThat(getPromptCount("시도할 횟수는 몇 회인가요?"))
                .isEqualTo(2);
    }

    @Test
    @DisplayName("시도 횟수가 음수이면 [ERROR] 출력 후 재입력 받는다")
    void readAttemptCount_fail_negative() {
        // given
        String invalidInput = "-5"; // 음수
        String validInput = "3";
        setSystemIn(invalidInput + "\n" + validInput);

        // when
        int count = inputView.readAttemptCount();

        // then
        assertThat(count).isEqualTo(3);
        assertThat(errContent.toString())
                .contains("[ERROR] 시도 횟수는 1 이상의 숫자여야 합니다.");
        assertThat(getPromptCount("시도할 횟수는 몇 회인가요?"))
                .isEqualTo(2);
    }

    // --- (참고) 정상 케이스 테스트 ---

    @Test
    @DisplayName("정상적인 자동차 이름을 입력받는다 (공백 제거 포함)")
    void readCarNames_success_withTrim() {
        // given
        setSystemIn("  pobi , woni , jun  ");

        // when
        List<String> names = inputView.readCarNames();

        // then
        assertThat(names).containsExactly("pobi", "woni", "jun");
        assertThat(errContent.toString()).isEmpty(); // 에러 출력 없음
        assertThat(getPromptCount("경주할 자동차 이름을 입력하세요."))
                .isEqualTo(1); // 프롬프트 1번 출력
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
        assertThat(errContent.toString()).isEmpty(); // 에러 출력 없음
        assertThat(getPromptCount("시도할 횟수는 몇 회인가요?"))
                .isEqualTo(1); // 프롬프트 1번 출력
    }
}