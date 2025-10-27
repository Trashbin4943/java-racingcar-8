package racingcar;

import camp.nextstep.edu.missionutils.Console; // Console.close()를 위해 필요
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationTest {

    // System.in/out/err 제어를 위한 변수
    private final PrintStream standardOut = System.out;
    private final PrintStream standardErr = System.err;
    private final InputStream standardIn = System.in;
    private ByteArrayOutputStream outContent;

    @BeforeEach
    void setUp() {
        // System.out과 System.err를 모두 캡처
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(outContent));
    }

    @AfterEach
    void tearDown() {
        // System.in/out/err 원상복구
        System.setOut(standardOut);
        System.setErr(standardErr);
        System.setIn(standardIn);
        Console.close();
    }

    private void setSystemIn(String input) {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
    }

    @Test
    @DisplayName("이름 입력 예외 발생 시, [ERROR] 출력 후 재입력을 받는다")
    void run_handlesInputNameError() {
        // given
        // 1. 가짜 입력 준비 (5자 초과 -> 성공 -> 1회)
        String invalidName = "pobitoooolong";
        String validName = "pobi,woni";
        String attempt = "1";
        String input = invalidName + System.lineSeparator()
                + validName + System.lineSeparator()
                + attempt;
        setSystemIn(input);

        // when
        // GameController를 생성하는 대신 Application.main()을 직접 호출
        Application.main(new String[]{});

        // then
        String output = outContent.toString();

        // [검증 가능한 부분]
        // 1. 에러 메시지가 출력되었는가
        assertThat(output).contains("[ERROR] 자동차 이름은 5자 이하만 가능합니다.");

        // 2. 이름 입력 프롬프트가 2번(실패, 성공) 호출되었는가
        int namePromptCount = output.split("경주할 자동차 이름을 입력하세요.").length - 1;
        assertThat(namePromptCount).isEqualTo(2);

        // 3. 결국 게임이 끝까지 실행되었는가
        assertThat(output).contains("최종 우승자 : ");
    }

    @Test
    @DisplayName("시도 횟수 예외 발생 시, [ERROR] 출력 후 재입력을 받는다")
    void run_handlesAttemptCountError() {
        String validName = "pobi";
        String invalidAttempt = "abc";
        String validAttempt = "1";
        String input = validName + System.lineSeparator()
                + invalidAttempt + System.lineSeparator()
                + validAttempt;
        setSystemIn(input);

        // when
        Application.main(new String[]{});

        String output = outContent.toString();

        assertThat(output).contains("[ERROR] 유효한 숫자를 입력해야 합니다.");

        int attemptPromptCount = output.split("시도할 횟수는 몇 회인가요?").length - 1;
        assertThat(attemptPromptCount).isEqualTo(2);
        assertThat(output).contains("최종 우승자 : ");
    }
}