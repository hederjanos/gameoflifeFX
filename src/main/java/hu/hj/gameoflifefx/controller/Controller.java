package hu.hj.gameoflifefx.controller;

import hu.hj.gameoflifefx.model.GameBoard;
import hu.hj.gameoflifefx.simulation.Simulation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Controller {

    private int dimension;
    private static final double CANVAS_SIZE = 500;
    private static double CELL_SIZE;
    private GameBoard gameBoard;
    private Simulation simulation;

    @FXML
    private Pane canvas;

    private Rectangle[][] grid;

    @FXML
    private TextField setDimensionField;

    @FXML
    private Button setDimensionButton;

    @FXML
    private Label setDimensionLabel;

    @FXML
    private Label counterLabel;

    @FXML
    private HBox counterBox;

    @FXML
    private Button startButton;

    @FXML
    private Button stopButton;

    @FXML
    private Button stepButton;

    @FXML
    private Button resetButton;

    public void initialize() {
        gameBoard = new GameBoard();
        simulation = new Simulation(gameBoard, this);
        counterLabel.textProperty().bind(simulation.counterProperty().asString());
        setDimensionButton.setDisable(true);
        setVisibleCounterBox(false);
        disableSimulationControlButtons(true, false, true, false);
    }

    private void disableSimulationControlButtons(boolean reset, boolean start, boolean stop, boolean step) {
        resetButton.setDisable(reset);
        startButton.setDisable(start);
        stopButton.setDisable(stop);
        stepButton.setDisable(step);
    }

    private void setVisibleCounterBox(boolean value) {
        for (Node node : counterBox.getChildren()) {
            node.setVisible(value);
        }
    }

    @FXML
    public void onSetDimensionButtonClicked(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(setDimensionButton)) {
            try {
                setDimensionLabel.setVisible(true);
                clear();
                dimension = Integer.parseInt(setDimensionField.getText());
                CELL_SIZE = Math.ceil(CANVAS_SIZE / dimension);
                this.canvas.setMaxSize(CELL_SIZE * dimension, CELL_SIZE * dimension);
                setDimensionLabel.setText("Dimension has been set to " + setDimensionField.getText());
                Thread wait = new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        setDimensionLabel.setVisible(false);
                        setVisibleCounterBox(true);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });
                wait.start();
                setDimensionButton.setDisable(true);
                initializeCanvas();
                updateCanvas();
            } catch (NumberFormatException nfe) {
                canvas.getChildren().removeAll();
                setDimensionLabel.setText("Add a number!");
            }
        }
    }

    public void clear() {
        if (gameBoard.hasMoreCell()) {
            gameBoard.clear();
        }
        if (grid != null) {
            grid = null;
        }
        if (canvas.getChildren() != null) {
            canvas.getChildren().clear();
        }
    }

    public void initializeCanvas() {
        if (dimension != 0) {
            grid = new Rectangle[dimension][dimension];
            for (int i = 0; i < CANVAS_SIZE; i += CELL_SIZE) {
                for (int j = 0; j < CANVAS_SIZE; j += CELL_SIZE) {
                    Rectangle rectangle = createRectangle(i, j);
                    canvas.getChildren().add(rectangle);
                }
            }
        }
    }

    private Rectangle createRectangle(int i, int j) {
        Rectangle rectangle = new Rectangle(i, j, CELL_SIZE, CELL_SIZE);
        grid[(int) (i / CELL_SIZE)][(int) (j / CELL_SIZE)] = rectangle;
        rectangle.setOnMouseClicked(this::handleOnRectangleClicked);
        return rectangle;
    }

    public void updateCanvas() {
        if (grid != null) {
            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[i].length; j++) {
                    Rectangle rectangle = grid[i][j];
                    if (gameBoard.isCellAlive(i, j)) {
                        rectangle.setFill(Color.BLACK);
                    } else {
                        rectangle.setFill(Color.WHITE);
                    }
                    rectangle.setStroke(Color.BLACK);
                    rectangle.setStrokeWidth(0.1);
                }
            }
        }
    }

    @FXML
    private void handleSetDimensionKeyPressed() {
        clear();
        reset();
        setVisibleCounterBox(false);
    }

    @FXML
    private void handleSetDimensionKeyReleased() {
        String text = setDimensionField.getText();
        boolean disableButton = text.isEmpty() || text.trim().isEmpty();
        setDimensionButton.setDisable(disableButton);
    }

    @FXML
    private void handleOnRectangleClicked(MouseEvent event) {
        int x = (int) (event.getX() / CELL_SIZE);
        int y = (int) (event.getY() / CELL_SIZE);
        if (gameBoard.isCellAlive(x, y)) {
            gameBoard.removeCell(x, y);
        } else {
            gameBoard.addCell(x, y);
        }
        updateCanvas();
    }

    @FXML
    public void start(ActionEvent actionEvent) {
        simulation.start();
        disableSimulationControlButtons(false, true, false, true);
    }

    @FXML
    public void stop(ActionEvent actionEvent) {
        if (simulation != null) {
            simulation.stop();
            disableSimulationControlButtons(false, false, true, false);
        }
    }

    @FXML
    public void reset(ActionEvent actionEvent) {
        reset();
    }

    private void reset() {
        if (simulation != null) {
            simulation.reset();
            counterLabel.textProperty().bind(simulation.counterProperty().asString());
            disableSimulationControlButtons(true, false, true, false);
        }
    }

    @FXML
    public void step(ActionEvent actionEvent) {
        increaseCounter();
        gameBoard.nextGeneration();
        updateCanvas();
        disableSimulationControlButtons(false, false, true, false);
    }

    private void increaseCounter() {
        simulation.counterProperty().setValue(simulation.counterProperty().getValue() + 1);
    }

}