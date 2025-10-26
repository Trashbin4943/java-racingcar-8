package racingcar.util;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNumberGenerator {
    private static final int Min_num = 0;
    private static final int Max_num = 9;

    public int generate() {
        return ThreadLocalRandom.current().nextInt(Min_num, Max_num+1);
    }
}
