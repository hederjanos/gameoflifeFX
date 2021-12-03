package hu.hj.gameoflifefx.controller;

import hu.hj.gameoflifefx.exception.DimensionException;
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
    private int cellSize;
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

    private void setVisibleCounterBox(boolean value) {
        for (Node node : counterBox.getChildren()) {
            node.setVisible(value);
        }
    }

    private void disableSimulationControlButtons(boolean reset, boolean start, boolean stop, boolean step) {
        resetButton.setDisable(reset);
        startButton.setDisable(start);
        stopButton.setDisable(stop);
        stepButton.setDisable(step);
    }

    @FXML
    public void onSetDimensionButtonClicked(ActionEvent actionEvent) {
        if (actionEvent.getSource().equals(setDimensionButton)) {
            try {
                setDimensionLabel.setVisible(true);
                clear();
                parseDimension();
                calculateCellSize();
                displayInfos();
                prepareForSimulation();
            } catch (NumberFormatException | DimensionException e) {
                canvas.getChildren().removeAll();
                setDimensionLabel.setText("Add a whole number between 5 and 50!");
            }
        }
    }

    private void parseDimension() throws DimensionException {
        int input = Integer.parseInt(setDimensionField.getText());
        if (input < 5 || input > 50) {
            throw new DimensionException();
        } else {
            dimension = input;
        }
    }

    private void calculateCellSize() {
        cellSize = (int) CANVAS_SIZE / dimension;
        this.canvas.setMaxSize((double) cellSize * dimension, (double) cellSize * dimension);
    }

    private void displayInfos() {
        setDimensionLabel.setText("Dimension has been set to " + setDimensionField.getText());
        Thread wait = new Thread(() -> {
            try {
                Thread.sleep(1000);
                setDimensionLabel.setVisible(false);
                setVisibleCounterBox(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                e.printStackTrace();
            }
        });
        wait.start();
    }

    private void prepareForSimulation() {
        setDimensionButton.setDisable(true);
        initializeCanvas();
        updateCanvas();
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
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    Rectangle rectangle = createRectangle(i * cellSize, j * cellSize);
                    canvas.getChildren().add(rectangle);
                }
            }
        }
    }

    private Rectangle createRectangle(int i, int j) {
        Rectangle rectangle = new Rectangle(i, j, cellSize, cellSize);
        grid[i / cellSize][j / cellSize] = rectangle;
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
        int x = (int) (event.getX() / cellSize);
        int y = (int) (event.getY() / cellSize);
        if (gameBoard.isCellAlive(x, y)) {
            gameBoard.removeCell(x, y);
        } else {
            gameBoard.addCell(x, y);
        }
        updateCanvas();
    }

    @FXML
    public void start() {
        simulation.start();
        disableSimulationControlButtons(false, true, false, true);
    }

    @FXML
    public void stop() {
        if (simulation != null) {
            simulation.stop();
            disableSimulationControlButtons(false, false, true, false);
        }
    }

    @FXML
    public void reset() {
        if (simulation != null) {
            simulation.reset();
            counterLabel.textProperty().bind(simulation.counterProperty().asString());
            disableSimulationControlButtons(true, false, true, false);
        }
    }

    @FXML
    public void step() {
        increaseCounter();
        gameBoard.nextGeneration();
        updateCanvas();
        disableSimulationControlButtons(false, false, true, false);
    }

    private void increaseCounter() {
        simulation.counterProperty().setValue(simulation.counterProperty().getValue() + 1);
    }
}