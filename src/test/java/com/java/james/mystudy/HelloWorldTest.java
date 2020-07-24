package com.java.james.mystudy;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * TODO: Document me!
 *
 * @author jsun
 *
 */
public class HelloWorldTest {
    @Test
    public void testSayHello() {
        HelloWorld helloWorld = new HelloWorld();
        String result = helloWorld.sayHello();
        assertEquals("Hello World", result);
    }
}
