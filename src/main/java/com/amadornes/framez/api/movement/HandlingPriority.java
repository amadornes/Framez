package com.amadornes.framez.api.movement;

/**
 * The higher the priority, the higher it'll be in the processing queue (it'll be processed before the rest)
 * 
 * @author amadornes
 * 
 */
public @interface HandlingPriority {

    public Priority value();

    public static enum Priority {
        LOW, MEDIUM, HIGH;
    }

}
