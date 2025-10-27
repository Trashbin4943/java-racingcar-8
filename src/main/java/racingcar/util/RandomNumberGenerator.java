package racingcar.util;

import camp.nextstep.edu.missionutils.Randoms;

public class RandomNumberGenerator {
    private static final int Min_num = 0;
    private static final int Max_num = 9;

    public int generate() {
        return Randoms.pickNumberInRange(Min_num, Max_num);
    }
}
