/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ignition;

import java.util.ArrayList;
import java.util.List;
import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

/**
 *
 * @author 19wibohlen
 */
public class ActiveDevices {
    public static List<MidiDevice> midiInputDevices = new ArrayList<>();
    public static List<MidiDevice> midiOutputDevices = new ArrayList<>();
    
    static void addInputDevice(MidiDevice.Info deviceInfo) {
        try {
            MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
            device.open();
            midiInputDevices.add(device);
            System.out.println("Device opened!");
        } catch (MidiUnavailableException e) {
            System.out.println("Device already in use!");
        }
    }
    
    static void addOutputDevice(MidiDevice.Info deviceInfo) {
        try {
            MidiDevice device = MidiSystem.getMidiDevice(deviceInfo);
            device.open();
            midiOutputDevices.add(device);
            System.out.println("Device opened!");
        } catch (MidiUnavailableException e) {
            System.out.println("Device already in use!");
        }
    }
    
    static void closeDevice(MidiDevice.Info deviceInfo) {
        for (MidiDevice dev : midiInputDevices ) {
            if (deviceInfo == dev.getDeviceInfo()) dev.close();
        }
        for (MidiDevice dev : midiOutputDevices ) {
            if (deviceInfo == dev.getDeviceInfo()) dev.close();
        }
    }
    
    static void closeAllDevices() {
        for (MidiDevice dev : midiInputDevices ) {
            dev.close();
        }
        for (MidiDevice dev : midiOutputDevices ) {
            dev.close();
        }
    }
}
