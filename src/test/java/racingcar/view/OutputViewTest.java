package racingcar.view;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import racingcar.domain.Car;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class OutputViewTest {

    private final PrintStream standardOut = System.out;
    private ByteArrayOutputStream outContent;
    private OutputView outputView;

    @BeforeEach
    void setUp() {
        // System.out의 출력을 outContent로 리디렉션
        outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        outputView = new OutputView();
    }

    @AfterEach
    void tearDown() {
        // System.out을 원래대로 복원
        System.setOut(standardOut);
    }

    private String getNewLine() {
        return System.lineSeparator();
    }

    /**
     * 테스트를 위해 Car 클래스를 상속받아,
     * 이름과 고정된 위치(position)를 반환하는 가짜(Stub) Car 객체.
     * 이 테스트 클래스 내부에서만 사용됩니다.
     */
    private class StubCar extends Car {
        private final int fixedPosition;

        public StubCar(String name, int fixedPosition) {
            // 부모 Car의 생성자를 호출 (이름 유효성 검사 등)
            // (만약 Car 생성자에 이름 유효성 검사가 있다면, "pobi"처럼 유효한 이름만 넣어야 함)
            super(name);
            this.fixedPosition = fixedPosition;
        }

        // OutputView가 사용하는 getPosition() 메서드를 오버라이드(재정의)
        @Override
        public int getPosition() {
            return this.fixedPosition;
        }

        // getName()은 부모(Car)의 것을 그대로 사용
    }


    @Test
    @DisplayName("실행 결과 헤더를 올바르게 출력한다")
    void printExecutionResultHeader() {
        // when
        outputView.printExecutionResultHeader();

        // then
        String expected = getNewLine() + "실행 결과" + getNewLine();
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("라운드 결과: 여러 대의 차 상태를 하이픈(-)으로 올바르게 출력한다")
    void printRoundResult() {
        // given
        // 가짜 StubCar 객체 생성
        Car pobi = new StubCar("pobi", 3); // 위치 3
        Car woni = new StubCar("woni", 0); // 위치 0

        List<Car> cars = List.of(pobi, woni);

        // when
        outputView.printRoundResult(cars);

        // then
        String expected = "pobi : ---" + getNewLine() +
                "woni : " + getNewLine() +
                getNewLine();

        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("최종 우승자: 한 명일 경우 올바르게 출력한다")
    void printFinalWinners_singleWinner() {
        // given
        List<String> winners = List.of("pobi");

        // when
        outputView.printFinalWinners(winners);

        // then
        String expected = "최종 우승자 : pobi" + getNewLine();
        assertThat(outContent.toString()).isEqualTo(expected);
    }

    @Test
    @DisplayName("최종 우승자: 여러 명일 경우 쉼표(,)로 구분하여 출력한다")
    void printFinalWinners_multipleWinners() {
        // given
        List<String> winners = List.of("pobi", "woni");

        // when
        outputView.printFinalWinners(winners);

        // then
        String expected = "최종 우승자 : pobi, woni" + getNewLine();
        assertThat(outContent.toString()).isEqualTo(expected);
    }
}