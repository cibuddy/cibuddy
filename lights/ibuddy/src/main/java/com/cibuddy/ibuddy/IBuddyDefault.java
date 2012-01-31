/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.cibuddy.ibuddy;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import java.io.IOException;
import java.math.BigInteger;

/**
 *
 * @author mirkojahn
 */
public class IBuddyDefault extends Thread implements IBuddyFigure {

    public static final short DEVICE_PRODUCT = 0x0002;
    private static final long UPDATE_INTERVAL = 50L; // in ms
    private final HIDDeviceInfo deviceInfo;
    private HIDDevice device;
    
    // default values for the hardware state(s)
    private Color currentColor = Color.BLACK;
    private boolean heart = false;
    private Wings wings = Wings.NEUTRAL;
    private Turn direction = Turn.NEUTRAL;
    
    // complex state manipulation
    private final HeartBeat beatingHeard = new HeartBeat();
    private final WingFlapping wingsFlapping = new WingFlapping();
    private final Dancing dancing = new Dancing();
    
    // the current thread state(s)
    private final Object guard = new Object();
    private volatile boolean update = false;
    private volatile boolean enabled = false;

    public IBuddyDefault(HIDDeviceInfo devInfo) {
        deviceInfo = devInfo;
    }

    @Override
    public void open() throws IOException {
        synchronized (guard) {
            device = deviceInfo.open();
            enabled = true;
            this.start();
        }
    }

    @Override
    public void close() throws IOException {
        update = true;
        enabled = false; // see Thread.stop()
        updateState();
    }

    @Override
    public void run() {
        // check if device is running (or there are pending actions)
        while (enabled || update) {
            try {
                Thread.sleep(UPDATE_INTERVAL);
                // check complex types
                beatingHeard.check();
                wingsFlapping.check();
                dancing.check();
                if (update) {
                    // actually update the state
                    updateState();
                }
            } catch (InterruptedException ex) {
            } catch (IOException ex) {
            }
        }
        try {
            synchronized (guard) {
                if (device != null) {
                    device.close();
                    device = null;
                }
            }
        } catch (IOException ex) {
        }
        System.out.println("Goodbye...");
    }

    private int updateState() throws IOException {
        byte state = 0;
        state |= (byte) ((int) currentColor.getCode() << 4);
        state |= (byte) (heart ? 0 : 0x80);
        state |= (byte) ((int) wings.getCode() << 2);
        state |= (byte) (direction.getCode());
        return write(state);
    }

    @Override
    public void resetEverything() {
        setCurrentColor(Color.BLACK);
        setHeart(false);
        setWings(Wings.NEUTRAL);
        setDirection(Turn.NEUTRAL);
        beatingHeard.disable();
        wingsFlapping.disable();
        dancing.disable();
    }

    private int write(byte payload) throws IOException {
        int writtenBytes = 0;
        byte[] command = SET_COMMAND.clone();
        command[8] = payload;
        try {
            //writtenBytes = device.write(command);
            writtenBytes = device.sendFeatureReport(command);
            if(writtenBytes > 0){
                update = false;
            }
            System.out.println("SUCCESS: command execution: " + new BigInteger(new byte[]{payload}).toString(16));
        } catch (IOException ex) {
            System.out.println("FAILED: command execution: " + new BigInteger(new byte[]{payload}).toString(16));
        }
        return writtenBytes;
    }

    @Override
    public void setCurrentColor(Color currentColor) {
        if(!enabled){
            return;
        }
        this.currentColor = currentColor;
        update = true;
    }

    public void setDirection(Turn direction) {
        if(!enabled){
            return;
        }
        this.direction = direction;
        update = true;
    }

    public void setHeart(boolean heart) {
        if(!enabled){
            return;
        }
        this.heart = heart;
        update = true;
    }
    
    public void enableBeatingHeart(){
        if(!enabled){
            return;
        }
        beatingHeard.enable();
    }
    
    public void enableBeatingHeartForTime(int ms){
        if(!enabled){
            return;
        }
        beatingHeard.enableForTime(ms);
    }
    
    public void enableWingFlapping(){
        if(!enabled){
            return;
        }
        wingsFlapping.enable();
    }
    
    public void enableWingFlappingForTime(int ms){
        if(!enabled){
            return;
        }
        wingsFlapping.enableForTime(ms);
    }
    
    public void enableDancing(){
        if(!enabled){
            return;
        }
        dancing.enable();
    }
    
    public void enableDancingForTime(int ms){
        if(!enabled){
            return;
        }
        dancing.enableForTime(ms);
    }

    public void setWings(Wings position) {
        if(!enabled){
            return;
        }
        this.wings = position;
        update = true;
    }

    public void toggleWings() {
        if(!enabled){
            return;
        }
        // doesn't matter in case the wings are neutral
        wings = (wings == Wings.DOWN ? Wings.UP : Wings.DOWN);
        update = true;
    }

    public void toggleDirection() {
        if(!enabled){
            return;
        }
        // doesn't matter in case the direction is neutral
        direction = (direction == Turn.LEFT ? Turn.RIGHT : Turn.LEFT);
        update = true;
    }

    public void toggleHeart() {
        if(!enabled){
            return;
        }
        heart = (heart ? false : true);
        update = true;
    }
    class HeartBeat extends ComplexState {

        @Override
        public void doExecute() {
            System.out.println("heartbeating");
            toggleHeart();
        }
        @Override
        public void doCleanUp(){
            setHeart(false);
        }
    }
    
    class WingFlapping extends ComplexState {
        
        @Override
        public void doExecute() {
            System.out.println("flying");
            toggleWings();
        }
        @Override
        public void doCleanUp(){
            setWings(Wings.NEUTRAL);
        }
        
        @Override
        public int getUpdateInterval(){
            return 350;
        }
    }
    
    class Dancing extends ComplexState {
        @Override
        public void doExecute() {
            System.out.println("dancing");
            toggleDirection();
        }
        @Override
        public void doCleanUp(){
            setDirection(Turn.NEUTRAL);
        }
        
        @Override
        public int getUpdateInterval(){
            return 250;
        }
    }

    abstract class ComplexState {

        private long nextToggle = 0;
        private boolean enabled = false;
        
        private boolean timeLimit = false;
        private long lastExecution;
        
        private boolean executionLimit = false;
        private int remainingExecutions = 0;

        boolean isEnabled() {
            return enabled;
        }

        public void enable() {
            enabled = true;
            nextToggle = System.currentTimeMillis() + getUpdateInterval();
        }
        
        public void enableForTime(int time){
            lastExecution += System.currentTimeMillis() + time;
            timeLimit = true;
            enable();
        }
        
        public void enableForCount(int count) {
            remainingExecutions = count;
            executionLimit = true;
            enable();
        }
        public void disable(){
            enabled = false;
            executionLimit = false;
            timeLimit = false;
            doCleanUp();
        }

        public void check() {
            if(enabled) {
                long now = System.currentTimeMillis();
                if (now >= nextToggle) {
                    // time base complex state
                    if(timeLimit) {
                        if(now <= lastExecution) {
                            doExecute();
                        } else {
                            disable();
                        }
                    // execution base complex state
                    } else if(executionLimit) {
                        if(remainingExecutions >0) {
                            remainingExecutions--;
                            doExecute();
                        } else {
                            disable();
                        }
                    // endless execution (until disabled)
                    } else {
                        doExecute();
                    }

                    // don't use now, to give the execution time to take longer than 
                    // the beat of the system.
                    nextToggle = System.currentTimeMillis() + getUpdateInterval();
                }
            }
        }
        
        public int getUpdateInterval(){
            return 1000;
        }

        public abstract void doExecute();
        
        /**
         * empty default implementation
         */
        public void doCleanUp(){
            // usually overwritten by parent
        }
    }
}
