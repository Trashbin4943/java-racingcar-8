package racingcar.domain;

import racingcar.util.RandomNumberGenerator;

public class Car {
    private static final int OFFSET = 4;

    private final String name;
    private int position;

    public Car(String name) {
        this.name = name;
        this.position = 0;
    }

    public void move(RandomNumberGenerator RNG) {
        int RN = RNG.generate();
        if (RN > OFFSET) {
            this.position++;
        }
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }
}
