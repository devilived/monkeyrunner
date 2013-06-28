/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.monkeyrunner.recorder.actions;

import com.android.chimpchat.core.IChimpDevice;

/**
 * Action to drag the "finger" across the device.
 */
public class NewDragAction implements Action {
    private final long timeMs = 4000;
    private final int steps = 10;
    private final int startx;
    private final int starty;
    private final int endx;
    private final int endy;
    private int type = -1;
  

    public NewDragAction( int startx, int starty, int endx, int endy) {
        this.startx = startx;
        this.starty = starty;
        this.endx = endx;
        this.endy = endy;
        
    }
   
    @Override
    public String getDisplayName() {
        return String.format("DRAG ( %s, %s),( %s, %s)",startx,starty,endx,endy );
    }

    @Override
    public String serialize() {
        float duration = timeMs / 1000.0f;
        String pydict = PyDictUtilBuilder.newBuilder().
        addTuple("start", startx, starty).
        addTuple("end", endx, endy).
        add("duration", duration).
        add("steps", steps).
        build();
        return "DRAG|" + pydict;
    }

    @Override
    public void execute(IChimpDevice device) {
        device.drag(startx, starty, endx, endy, steps, timeMs);
    }
}
