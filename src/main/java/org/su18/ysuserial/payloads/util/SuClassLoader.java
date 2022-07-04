package org.su18.ysuserial.payloads.util;

/**
 * @author su18
 */
public class SuClassLoader extends ClassLoader {

    public SuClassLoader() {
        super(Thread.currentThread().getContextClassLoader());
    }
}
