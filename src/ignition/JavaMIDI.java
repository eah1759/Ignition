/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ignition;

import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
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
        
        ListView inputList = new ListView();
        ListView outputList = new ListView();
        ObservableList inputData = FXCollections.observableArrayList();
        ObservableList outputData = FXCollections.observableArrayList();
        
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
        inputList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                MidiDevice.Info devInfo;
                try {
                    devInfo = (MidiDevice.Info) (inputList.getItems().get((Integer) number));
                    ActiveDevices.closeDevice(devInfo);
                } catch (ArrayIndexOutOfBoundsException e) {}
                try {
                    devInfo = (MidiDevice.Info) (inputList.getItems().get((Integer) number2));
                    ActiveDevices.addInputDevice(devInfo);
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        });

        outputList.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number number, Number number2) {
                MidiDevice.Info devInfo;
                try {
                    devInfo = (MidiDevice.Info) (outputList.getItems().get((Integer) number));
                    ActiveDevices.closeDevice(devInfo);
                } catch (ArrayIndexOutOfBoundsException e) {}
                try {
                    devInfo = (MidiDevice.Info) (outputList.getItems().get((Integer) number2));
                    ActiveDevices.addOutputDevice(devInfo);
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        });
        // Now, display strings from synthInfos list in GUI.    
        inputList.setItems(inputData);
        outputList.setItems(outputData);
        
        Label inputlbl = new Label("Input Device");
        Label outputlbl = new Label("Output Device");      
       
        grid.add(inputlbl, 0, 0);
        grid.add(inputList, 0, 1);
        grid.add(outputlbl, 0, 2);
        grid.add(outputList, 0, 3);
        
        Scene scene = new Scene(grid, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
        
        
        Runtime.getRuntime().addShutdownHook(new Thread()
        {
            @Override
            public void run()
            {
               ActiveDevices.closeAllDevices();
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
