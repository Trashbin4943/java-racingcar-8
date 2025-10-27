package racingcar.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import racingcar.util.RandomNumberGenerator;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.assertj.core.api.Assertions.assertThat;

class CarsTest {

    @Test
    @DisplayName("자동차 이름 목록으로 Cars 객체를 생성한다")
    void createCars() {
        // given
        List<String> names = List.of("pobi", "woni", "jun");

        // when
        Cars cars = new Cars(names);
        List<Car> carList = cars.getCarList();

        // then
        // 1. 자동차가 3대 생성되었는지 확인
        assertThat(carList).hasSize(3);

        // 2. 이름이 순서대로 들어갔는지 확인
        assertThat(carList)
                .extracting(Car::getName) // 리스트에서 이름만 추출
                .containsExactly("pobi", "woni", "jun");

        // 3. 모든 차의 초기 위치가 0인지 확인
        assertThat(carList)
                .extracting(Car::getPosition) // 리스트에서 위치만 추출
                .allMatch(position -> position == 0); // 모든 값이 0인지 확인
    }

    @Test
    @DisplayName("findWinners: 아무도 움직이지 않았을 때 모두가 우승자이다")
    void findWinners_allStop() {
        // given
        Cars cars = new Cars(List.of("pobi", "woni"));

        // when
        List<String> winners = cars.findWinners(); // moveAll() 호출 안 함

        // then
        // 둘 다 0점이므로 공동 우승
        assertThat(winners).containsExactlyInAnyOrder("pobi", "woni");
    }

    @Test
    @DisplayName("moveAll: 한 명만 전진하고 한 명은 멈추면 한 명만 우승한다")
    void moveAll_and_findWinner() {
        // given
        Cars cars = new Cars(List.of("pobi", "woni"));

        // 1. '가짜' 랜덤 생성기(Stub) 생성
        // "pobi"는 5(전진), "woni"는 3(멈춤)을 순서대로 반환
        Queue<Integer> fakeNumbers = new LinkedList<>(Arrays.asList(5, 3));
        RandomNumberGenerator stubRng = new RandomNumberGenerator() {
            @Override
            public int generate() {
                return fakeNumbers.poll(); // 큐에서 5, 3을 순서대로 꺼내 반환
            }
        };

        // when
        // 2. '가짜' 랜덤 생성기를 moveAll에 '주입(전달)'
        cars.moveAll(stubRng);
        List<String> winners = cars.findWinners();

        // then
        // 3. "pobi"만 전진했는지 확인
        List<Car> carList = cars.getCarList();
        assertThat(carList.get(0).getName()).isEqualTo("pobi");
        assertThat(carList.get(0).getPosition()).isEqualTo(1); // pobi (5) -> 전진

        assertThat(carList.get(1).getName()).isEqualTo("woni");
        assertThat(carList.get(1).getPosition()).isEqualTo(0); // woni (3) -> 멈춤

        // 4. "pobi"만 우승자인지 확인
        assertThat(winners).containsExactly("pobi");
    }
}