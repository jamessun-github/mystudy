package com.java.james.studycamel;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.impl.DefaultCamelContext;

/**
 * TODO: Document me!
 *
 * @author jsun
 *
 */
public class FileCopierWithCamel {

    public static void main(String[] args) throws Exception {

        CamelContext context = new DefaultCamelContext();

        context.addRoutes(new RouteBuilder() {

/* Camel中的路线是这样定义的，它们在阅读时流动。 这条路线可以这样读取：使用设置的noop选项从文件位置数据/收件箱中消息，
 * 并发送到文件位置数据/发件箱。noop选项告诉Camel按原样离开源文件。
 * 如果你没有使用这个选项，文件将被移动。 大多数以前从未见过Camel的人将能够理解这条路线的作用。
 * 您可能还需要注意的是，除了样板代码以外，您只需在一行Java代码中创建一个文件轮询路由
 * (from("file:inbox?noop=true").to("file:outbox");)。
*/            
            @Override
            public void configure() throws Exception {
                from("file:inbox?noop=true").to("file:outbox");
            }
        });

        context.start();

        // 通用没有具体业务意义的代码，只是为了保证主线程不退出
        synchronized (FileCopierWithCamel.class) {
            FileCopierWithCamel.class.wait();
        }

    }

}
