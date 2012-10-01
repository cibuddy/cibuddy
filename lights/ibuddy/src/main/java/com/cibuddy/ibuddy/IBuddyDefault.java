/*
 * Copyright (C) 2012 Mirko Jahn <mirkojahn@gmail.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cibuddy.ibuddy;

import com.codeminders.hidapi.HIDDevice;
import com.codeminders.hidapi.HIDDeviceInfo;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Timer;
import java.util.TimerTask;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author mirkojahn
 */
public abstract class IBuddyDefault extends TimerTask implements IBuddyFigure {

    private static final Logger LOG = LoggerFactory.getLogger(IBuddyDefault.class);
    public static short DEVICE_PRODUCT = 0x0000;
    private static final long UPDATE_INTERVAL = 500L; // in ms
    private final HIDDeviceInfo deviceInfo;
    private final String name;
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

    private final Timer timer = new Timer();
    private final long EXECUTION_DELAY = 1000; // 1 Seconds
    
    public IBuddyDefault(HIDDeviceInfo devInfo, String productId) {
        deviceInfo = devInfo;
        name = productId;
    }
    
    public String getName(){
        return name;
    }

    @Override
    public void open() throws IOException {
        synchronized (guard) {
            device = deviceInfo.open();
            enabled = true;
            timer.schedule(this, EXECUTION_DELAY, UPDATE_INTERVAL);
        }
    }

    @Override
    public void close() throws IOException {
        update = true;
        enabled = false; // see Thread.stop()
        updateState();
        timer.cancel();
        try {
            synchronized (guard) {
                if (device != null) {
                    device.close();
                    device = null;
                }
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void run() {
        try {
            // check if device is running (or there are pending actions)
            if (enabled | update) {

                // check complex types
                beatingHeard.check();
                wingsFlapping.check();
                dancing.check();
                if (update) {
                    // actually update the state
                    updateState();
                }

            }
        } catch (IOException ex) {
            LOG.warn("Problem writing state to the device. This most likely "
                    + "depends on the native code part or hardware.", ex);
        }
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
            // attempt 1:
            // Did not work!
//            device.disableBlocking();
//            device.writeTimeout(SETUP_COMMAND,100);
//            writtenBytes = device.writeTimeout(command, 100);
            
            
            // attempt 3:
            // working on windows and mac os (linux seems to have issues)
            device.write(SETUP_COMMAND);
            writtenBytes = device.write(command);
            
            
            // attempt 4:
//            device.sendFeatureReport(SETUP_COMMAND);
//            writtenBytes = device.sendFeatureReport(command);
//            
//            
            // attempt 5:
//            writtenBytes = device.writeTimeout(command, 100);
//            
//            
            // attempt 6:
//            writtenBytes = device.write(command);
//            
//            
            // attempt 7:
//            writtenBytes = device.sendFeatureReport(command);
            
            // adding blocking, then there are even more combinations to test
            
            if(writtenBytes > 0){
                update = false;
                LOG.debug("SUCCESS: command execution: {}", new BigInteger(new byte[]{payload}).toString(16));
            } else {
                LOG.debug("FAILED: command execution based on written bytes {} for command: {}", writtenBytes , new BigInteger(new byte[]{payload}).toString(16));
            }
        } catch (Exception ex) {
            LOG.debug("FAILED: command execution: {}", new BigInteger(new byte[]{payload}).toString(16), ex);
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

    @Override
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
            //System.out.println("heartbeating");
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
