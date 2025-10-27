package racingcar.view;
import racingcar.domain.Car;

import java.util.List;
import java.util.stream.Collectors;

public class OutputView {
    private static final String POSITION_MARKER = "-";
    private static final String WINNER_DELIMITER = ", ";

    private static final String EXECUTION_RESULT_HEADER = "실행 결과";
    private static final String CAR_STATUS_FORMAT = "%s : %s";
    private static final String FINAL_WINNER_HEADER = "최종 우승자 : ";

    public void printExecutionResultHeader() {
        System.out.println();
        System.out.println(EXECUTION_RESULT_HEADER);
    }

    public void printRoundResult(List<Car> cars) {
        for (Car car : cars) {
            String positionString = generatePositionString(car.getPosition());
            System.out.printf(CAR_STATUS_FORMAT + "%n", car.getName(), positionString);
        }
        System.out.println();
    }

    public void printFinalWinners(List<String> winners) {
        String winnerNames = String.join(WINNER_DELIMITER, winners);

        System.out.println(FINAL_WINNER_HEADER + winnerNames);
    }

    private String generatePositionString(int position) {
        return POSITION_MARKER.repeat(position);
    }
}