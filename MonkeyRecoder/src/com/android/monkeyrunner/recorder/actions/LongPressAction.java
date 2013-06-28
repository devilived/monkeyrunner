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
public class LongPressAction implements Action {
    private final long timeMs =7000;
    private final int steps = 1000;
    private final int startx;
    private final int starty;

    public LongPressAction(int startx, int starty) {
        this.startx = startx;
        this.starty = starty;
    }
   
    @Override
    public String getDisplayName() {
        return "LongPress ("+startx+","+starty+")";
    }

    @Override
    public String serialize() {
    	 float duration = timeMs / 1000.0f;
         String pydict = PyDictUtilBuilder.newBuilder().
         addTuple("start", startx, starty).
         addTuple("end", startx, starty).
         add("duration", duration).
         add("steps", steps).
         build();
        return "DRAG|" + pydict ;
    }

    @Override
    public void execute(IChimpDevice device) {
        device.drag(startx, starty, startx, starty, steps, timeMs);
    }
}
