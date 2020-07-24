package com.java.james.studycamel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
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
public class TimerBeansMain extends Main {

    static Logger LOG = LoggerFactory.getLogger(TimerBeansMain.class);

    public static void main(String[] args) throws Exception {
        TimerBeansMain main = new TimerBeansMain();
        main.enableHangupSupport();
        main.bind("processByBean1", new Bean1());
        main.bind("processAgainByBean2", new Bean2());
        main.addRouteBuilder(createRouteBuilder());
        main.run(args);
    }

    static RouteBuilder createRouteBuilder() {
        return new RouteBuilder() {
            public void configure() {
                from("timer://timer1?period=1000").to("bean:processByBean1").to("bean:processAgainByBean2");
            }
        };
    }

    // Processor beans
    static class Bean1 implements Processor {
        public void process(Exchange msg) {
            LOG.info("First process {}", msg);
        }
    }

    static class Bean2 implements Processor {
        public void process(Exchange msg) {
            LOG.info("Second process {}", msg);
        }
    }
}
