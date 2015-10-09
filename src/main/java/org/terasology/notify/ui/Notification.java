/*
 * Copyright 2015 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.terasology.notify.ui;

/**
 *
 */
final class Notification {

    final static long FADE_IN_TIME = 300;
    final static long FADE_OUT_TIME = 300;

    private final String text;
    private long timeAdded;
    private long timeRemoved = Long.MAX_VALUE;

    public Notification(String text, long timeAdded) {
        this.text = text;
        this.timeAdded = timeAdded;
    }

    /**
     * @return
     */
    public String getText() {
        return text;
    }

    public long getTimeAdded() {
        return timeAdded;
    }

    public void setTimeAdded(long timeAdded) {
        this.timeAdded = timeAdded;
    }

    public long getTimeRemoved() {
        return timeRemoved;
    }

    public void setTimeRemoved(long timeRemoved) {
        this.timeRemoved = timeRemoved;
    }
}

