// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0

package org.terasology.dialogs.notify.ui;

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

