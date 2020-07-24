package com.java.james.studycamel;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Document me!
 *
 * @author jsun
 *
 */
public class TimerMain2 extends Main {

    static Logger LOG = LoggerFactory.getLogger(TimerMain2.class);

    public static void main(String[] args) throws Exception {
        TimerMain2 main = new TimerMain2();
        main.enableHangupSupport();
        main.addRouteBuilder(createRouteBuilder());
        main.run(args);
    }

    static RouteBuilder createRouteBuilder() {
        return new TimerRouteBuilder();
    }
}
