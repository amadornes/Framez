package com.amadornes.framez.api.movement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * The higher the priority, the higher it'll be in the processing queue (it'll be processed before the rest)
 * 
 * @author amadornes
 * 
 */
@Target(ElementType.METHOD)
public @interface HandlingPriority {

    public Priority value();

    public static enum Priority {
        LOW, MEDIUM, HIGH;
    }

}
