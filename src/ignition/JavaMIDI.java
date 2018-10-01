/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javamidi;

import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;


/**
 *
 * @author 19wibohlen
 */
public class JavaMIDI extends Application {
    
    public static List<MidiDevice> openDevices;
    
    @Override
    public void start(Stage primaryStage) {
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        ObservableList inputData = FXCollections.observableArrayList();
        ObservableList outputData = FXCollections.observableArrayList();
        ChoiceBox inputCB = new ChoiceBox(inputData);
        ChoiceBox outputCB = new ChoiceBox(outputData);
        
        // Obtain information about all the installed synthesizers.
        MidiDevice device = null;
        MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
        for (int i = 0; i < infos.length; i++) {
            try {
                device = MidiSystem.getMidiDevice(infos[i]);
            } catch (MidiUnavailableException e) {
                  // Handle or throw exception...
            }
            if (device.getMaxTransmitters() == -1) {
                inputData.add(infos[i]);
            }
            if (device.getMaxReceivers() == -1) {
                outputData.add(infos[i]);
            }
        }
        inputCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                try {
                    for (MidiDevice dev : openDevices) {
                        MidiDevice.Info closeInfo = (MidiDevice.Info) (inputCB.getItems().get((Integer) number));
                        if (dev == closeInfo) {
                            dev.close();
                        }
                    }
                    
                    MidiDevice.Info devInfo = (MidiDevice.Info) (inputCB.getItems().get((Integer) number2));
                    MidiDevice device = MidiSystem.getMidiDevice(devInfo);
                    device.open();
                    openDevices.add(device);
                    System.out.println("Device opened!");
                } catch (MidiUnavailableException e) {
                    System.out.println("Device already in use!");
                }
            }
        });

        outputCB.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                System.out.println(outputCB.getItems().get((Integer) number2));
            }
        });
        // Now, display strings from synthInfos list in GUI.    
        inputCB.setItems(inputData);
        outputCB.setItems(outputData);
        
        Label inputlbl = new Label("Input Device");
        Label outputlbl = new Label("Output Device");      
       
        grid.add(inputlbl, 0, 0);
        grid.add(inputCB, 0, 1);
        grid.add(outputlbl, 0, 2);
        grid.add(outputCB, 0, 3);
        
        Scene scene = new Scene(grid, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
                for (MidiDevice dev : openDevices) {
                    dev.close();
                }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
