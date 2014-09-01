package com.amadornes.framez.util;

import java.lang.reflect.Field;

public class ReflectUtils {
    
    @SuppressWarnings("unchecked")
    public static final <T> T get(Object o, String field) {
    
        Field f = null;
        try {
            f = o.getClass().getDeclaredField(field);
            if (f == null) return null;
            
            boolean accessible = f.isAccessible();
            if (!accessible) f.setAccessible(true);
            
            Object obj = f.get(o);
            
            f.setAccessible(accessible);
            
            return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public static final void set(Object o, String field, Object val) {
    
        Field f = null;
        try {
            f = o.getClass().getDeclaredField(field);
            if (f == null) return;
            
            boolean accessible = f.isAccessible();
            if (!accessible) f.setAccessible(true);
            
            f.set(o, val);
            
            f.setAccessible(accessible);
        } catch (Exception e) {
        }
    }
    
}
