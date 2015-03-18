package com.amadornes.framez.util;

import java.lang.reflect.AnnotatedElement;
import java.util.Comparator;

import com.amadornes.framez.api.Priority;
import com.amadornes.framez.api.Priority.PriorityEnum;

public class SorterPriority implements Comparator<AnnotatedElement> {

    @Override
    public int compare(AnnotatedElement o1, AnnotatedElement o2) {

        Priority p1 = o1.getAnnotation(Priority.class);
        Priority p2 = o2.getAnnotation(Priority.class);

        return Integer.compare((p1 != null ? p1.value() : PriorityEnum.MEDIUM).getPriority(), (p2 != null ? p2.value()
                : PriorityEnum.MEDIUM).getPriority());
    }

    public static class SorterPriorityInstance implements Comparator<Object> {

        @Override
        public int compare(Object o1, Object o2) {

            return new SorterPriority().compare(o1.getClass(), o2.getClass());
        }

    }

}
