package hu.hj.gameoflifefx.simulation;

import hu.hj.gameoflifefx.controller.Controller;
import hu.hj.gameoflifefx.model.GameBoard;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Simulation {
    private static final int SLEEP_MSEC = 200;
    private final IntegerProperty counter = new SimpleIntegerProperty(0);
    private final GameBoard gameBoard;
    private final Controller controller;
    private ScheduledExecutorService scheduler;

    public Simulation(GameBoard gameBoard, Controller controller) {
        this.gameBoard = gameBoard;
        this.controller = controller;
    }

    public IntegerProperty counterProperty() {
        return counter;
    }

    public void start() {
        stop();
        Runnable gotoNextGenerationTask = new Runnable() {
            @Override
            public void run() {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        gameBoard.nextGeneration();
                        controller.updateCanvas();
                        counter.setValue(counter.getValue() + 1);
                    }
                });
            }
        };
        scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(gotoNextGenerationTask, 0, SLEEP_MSEC, TimeUnit.MILLISECONDS);
    }

    public void stop() {
        if (scheduler != null) {
            scheduler.shutdown();
            scheduler = null;
        }
    }

    public void reset() {
        stop();
        counter.setValue(0);
        controller.clear();
        controller.initializeCanvas();
        controller.updateCanvas();
    }
}
