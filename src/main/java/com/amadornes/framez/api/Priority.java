package com.amadornes.framez.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(value = { ElementType.METHOD, ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
public @interface Priority {

    public PriorityEnum value() default PriorityEnum.MEDIUM;

    public static enum PriorityEnum {
        OVERRIDE(Integer.MIN_VALUE), VERY_HIGH(0), HIGH(1), MEDIUM(2), LOW(3), VERY_LOW(4);

        private int priority;

        private PriorityEnum(int priority) {

            this.priority = priority;
        }

        public int getPriority() {

            return priority;
        }

    }
}
