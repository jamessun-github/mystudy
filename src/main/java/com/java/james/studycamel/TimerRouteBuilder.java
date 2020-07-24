package com.java.james.studycamel;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * TODO: Document me!
 *
 * @author jsun
 *
 */
public class TimerRouteBuilder extends RouteBuilder {
    static Logger LOG = LoggerFactory.getLogger(TimerRouteBuilder.class);

    public void configure() {
        from("timer://timer1?period=1000").process(new Processor() {
            public void process(Exchange msg) {
                LOG.info("Processing {}", msg);
            }
        });
    }
}
