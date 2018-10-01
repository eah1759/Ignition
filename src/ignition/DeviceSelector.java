/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ignition;

import static javafx.application.Application.launch;
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
public class DeviceSelector {
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
            System.out.println(infos[i].toString());
            System.out.println(device.getMaxReceivers());
            System.out.println(device.getMaxTransmitters());
            if (device.getMaxReceivers() == -1) {
                outputData.add(infos[i].toString());
            }
            if (device.getMaxTransmitters() == -1) {
                inputData.add(infos[i].toString());
            }
        }
        // Now, display strings from synthInfos list in GUI.    
        inputCB.setItems(inputData);
        outputCB.setItems(outputData);
        
        Label inputlbl = new Label("Input Devices");
        Label outputlbl = new Label("Output Devices");      
        
        grid.add(inputlbl, 0, 0);
        grid.add(inputCB, 0, 1);
        grid.add(outputlbl, 0, 2);
        grid.add(outputCB, 0, 3);
        
        Scene scene = new Scene(grid, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
}
