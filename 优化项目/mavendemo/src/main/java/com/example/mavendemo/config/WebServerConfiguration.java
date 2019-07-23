package com.example.mavendemo.config;

import org.apache.catalina.connector.Connector;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.ConfigurableWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.stereotype.Component;

// 当容器内没有ToncatEmbeddedServletContainerFactory这个bean时
// 会把此类作为bean加载进去
// SpringBoot内嵌的Tomcat就会加载这个Bean
@Component
public class WebServerConfiguration implements WebServerFactoryCustomizer<ConfigurableWebServerFactory> {

    @Override
    public void customize(ConfigurableWebServerFactory factory) {
        // 使用工厂类提供的接口定制化tomcat connector
        ((TomcatServletWebServerFactory)factory).addConnectorCustomizers(new TomcatConnectorCustomizer() {
            @Override
            public void customize(Connector connector) {
                Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
                // 定制化tomcat的keepalive的timeout;30s内无请求，自动断开
                protocol.setKeepAliveTimeout(30*1000);
                // 最大连接请求为 1w 个
                protocol.setMaxKeepAliveRequests(10*1000);
            }
        });
    }
}
