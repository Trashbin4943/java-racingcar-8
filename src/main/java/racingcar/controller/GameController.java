package racingcar.controller;

import racingcar.domain.Cars;
import racingcar.util.RandomNumberGenerator;
import racingcar.view.InputView;
import racingcar.view.OutputView;

import java.util.List;

public class GameController {

    private final InputView inputView;
    private final OutputView outputView;
    private final RandomNumberGenerator generator;

    public GameController() {
        this.inputView = new InputView();
        this.outputView = new OutputView();
        this.generator = new RandomNumberGenerator();
    }

    public void run() {
        List<String> carNames = inputView.readCarNames();
        Cars cars = new Cars(carNames);

        int attemptCount = inputView.readAttemptCount();

        outputView.printExecutionResultHeader();

        for (int i = 0; i < attemptCount; i++) {
            cars.moveAll(generator);
            outputView.printRoundResult(cars.getCarList());
        }

        List<String> winners = cars.findWinners();
        outputView.printFinalWinners(winners);
    }
}
