package com.megabyte6.wordle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import com.megabyte6.wordle.util.SceneManager;
import com.megabyte6.wordle.util.Stats;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static final Color BACKGROUND_COLOR = Color.web("#0b2b37");

    public static Stats stats;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        App.stats = readStats();

        Scene scene = new Scene(FXMLLoader.load(getClass().getResource("view/Start.fxml")));

        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setTitle("Wordle");
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneManager.init(primaryStage, scene, BACKGROUND_COLOR);
    }

    @Override
    public void stop() throws Exception {
        writeStats(stats);
    }

    private Stats readStats() {
        Stats stats;
        try {
            final String fileName = "stats.dat";
            final FileInputStream fileInputStream = new FileInputStream(fileName);
            final ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            stats = (Stats) objectInputStream.readObject();
            objectInputStream.close();
        } catch (Exception e) {
            stats = new Stats(0, 0, 0, 0,
                    new int[] { 0, 0, 0, 0, 0, 0 });
        }
        return stats;
    }

    private void writeStats(Stats updatedStats) {
        try {
            final String fileName = "stats.dat";
            final FileOutputStream fileOutputStream = new FileOutputStream(fileName);
            final ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(updatedStats);
            objectOutputStream.close();
        } catch (IOException e) {
            System.err.println("Could not write to stats file.");
            e.printStackTrace();
        }
    }

}
