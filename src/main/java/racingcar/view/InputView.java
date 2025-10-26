package racingcar.view;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class InputView {

    private static final String CAR_NAME_PROMPT = "경주할 자동차 이름을 입력하세요.(이름은 쉼표(,) 기준으로 구분)";
    private static final String ATTEMPT_COUNT_PROMPT = "시도할 횟수는 몇 회인가요?";

    private static final String DELIMITER = ",";
    private static final int MAX_NAME_LENGTH = 5;

    private static final String ERROR_PREFIX = "[ERROR] ";
    private static final String ERROR_NOT_A_NUMBER = ERROR_PREFIX + "유효한 숫자를 입력해야 합니다.";
    private static final String ERROR_NOT_POSITIVE = ERROR_PREFIX + "시도 횟수는 1 이상의 숫자여야 합니다.";
    private static final String ERROR_NAME_TOO_LONG = ERROR_PREFIX + "자동차 이름은 " + MAX_NAME_LENGTH + "자 이하만 가능합니다.";
    private static final String ERROR_NAME_BLANK = ERROR_PREFIX + "자동차 이름은 공백일 수 없습니다.";
    private static final String ERROR_NO_CARS = ERROR_PREFIX + "1대 이상의 자동차 이름을 입력해야 합니다.";


    private final Scanner scanner;

    public InputView() {
        this.scanner = new Scanner(System.in);
    }

    public List<String> readCarNames() {
        while (true) {
            System.out.println(CAR_NAME_PROMPT);
            String input = scanner.nextLine();

            try {
                List<String> names = parseAndTrim(input);
                validateCarNames(names);
                return names;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    public int readAttemptCount() {
        while (true) {
            System.out.println(ATTEMPT_COUNT_PROMPT);
            String input = scanner.nextLine();

            try {
                int count = parsePositiveInt(input);
                return count;
            } catch (IllegalArgumentException e) {
                System.err.println(e.getMessage());
            }
        }
    }

    private List<String> parseAndTrim(String input) {
        return Arrays.stream(input.split(DELIMITER))
                .map(String::trim)
                .collect(Collectors.toList());
    }

    private void validateCarNames(List<String> names) {
        if (names.isEmpty() || names.stream().allMatch(String::isBlank)) {
            throw new IllegalArgumentException(ERROR_NO_CARS);
        }

        for (String name : names) {
            if (name.isBlank()) {
                throw new IllegalArgumentException(ERROR_NAME_BLANK);
            }
            if (name.length() > MAX_NAME_LENGTH) {
                throw new IllegalArgumentException(ERROR_NAME_TOO_LONG);
            }
        }
    }

    private int parsePositiveInt(String input) {
        int number;
        try {
            number = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(ERROR_NOT_A_NUMBER);
        }

        if (number <= 0) {
            throw new IllegalArgumentException(ERROR_NOT_POSITIVE);
        }
        return number;
    }
}