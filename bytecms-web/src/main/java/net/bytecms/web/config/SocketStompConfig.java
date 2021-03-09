package net.bytecms.web.config;

import net.bytecms.web.Interceptors.HandleShakeInterceptors;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.security.Principal;
import java.util.Map;

@Configuration
@EnableWebSocketMessageBroker
public class SocketStompConfig implements WebSocketMessageBrokerConfigurer {

    public void registerStompEndpoints(StompEndpointRegistry registry) {
                registry.addEndpoint("/stomp")
                .setAllowedOrigins("*")
                .setHandshakeHandler(new DefaultHandshakeHandler() {
                    @Override
                    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
                        //将客户端标识封装为Principal对象，从而让服务端能通过getName()方法找到指定客户端
                        Principal principal = (Principal)attributes.get("socketUser");
                        return principal;
                    }
                })
                //添加socket拦截器，用于从请求中获取客户端标识参数
                .addInterceptors(new HandleShakeInterceptors()).withSockJS();

    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //客户端发送消息的请求前缀
        registry.setApplicationDestinationPrefixes("/app");
        //客户端订阅消息的请求前缀，topic一般用于广播推送，queue用于点对点推送
        registry.enableSimpleBroker("/topic", "/queue");

        /*  服务端通知客户端的前缀，可以不设置，默认为user
         *  1. 配置一对一消息前缀， 客户端接收一对一消息需要配置的前缀 如“'/user/'+userid + '/message'”，
         *     是客户端订阅一对一消息的地址 stompClient.subscribe js方法调用的地址
         *  2. 使用@SendToUser发送私信的规则不是这个参数设定，在框架内部是用UserDestinationMessageHandler处理，
         *     而不是而不是 AnnotationMethodMessageHandler 或  SimpleBrokerMessageHandler
         *     or StompBrokerRelayMessageHandler，是在@SendToUser的URL前加“user+sessionId"组成
         */
        registry.setUserDestinationPrefix("/user");
        /*  如果是用自己的消息中间件，则按照下面的去配置，删除上面的配置
         *   registry.enableStompBrokerRelay("/topic", "/queue")
            .setRelayHost("rabbit.someotherserver")
            .setRelayPort(62623)
            .setClientLogin("marcopolo")
            .setClientPasscode("letmein01");
            registry.setApplicationDestinationPrefixes("/app", "/foo");
         * */


    }

/*    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.setInterceptors(createUserInterceptor());
    }

    @Bean
    public WebsocketUserInterceptor createUserInterceptor() {
        return new WebsocketUserInterceptor();
    }*/
}