package racingcar.domain;

import racingcar.util.RandomNumberGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Cars {

    private final List<Car> carList;

    public Cars(List<String> carNames) {
        this.carList = new ArrayList<>();
        for (String name : carNames) {
            carList.add(new Car(name));
        }
    }

    public void moveAll(RandomNumberGenerator RNG) {
        for (Car car : carList) {
            car.move(RNG);
        }
    }

    private int findMaxPosition() {
        int maxPosition = 0;
        for (Car car : carList) {
            if (car.getPosition() > maxPosition) {
                maxPosition = car.getPosition();
            }
        }
        return maxPosition;
    }

    public List<String> findWinners() {
        int maxPosition = findMaxPosition();

        return carList.stream()
                .filter(car -> car.getPosition() == maxPosition)
                .map(Car::getName)
                .collect(Collectors.toList());
    }

    public List<Car> getCarList() {
        return carList;
    }
}