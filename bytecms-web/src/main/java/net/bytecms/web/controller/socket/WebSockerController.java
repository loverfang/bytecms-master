package net.bytecms.web.controller.socket;

import com.alibaba.fastjson.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WebSockerController {

    @Autowired
    SimpMessagingTemplate SMT;

    @MessageMapping("socket/send")
    public void subscription(String str) throws MessagingException, UnsupportedEncodingException {
        System.err.println(str);
        SMT.convertAndSend("/topic/sub","开始推送消息了qqqq："+str);
    }


    @MessageMapping("socket/sendChatMsgByOwn")
    public void sendChatMsgByOwn(String str, Principal principal) throws MessagingException, UnsupportedEncodingException {
        System.err.println(str);
        System.out.println(principal.toString());
        SMT.convertAndSendToUser(principal.getName(),"/queue/msg","开始推送消息了："+str);
    }

    @MessageMapping("/sendChatMsgById/{accountId}")
    //@SendToUser(value = "/userTest/callBack")
    public Map<String, Object> sendChatMsgById(
            @DestinationVariable(value = "accountId") String accountId, String json,
            StompHeaderAccessor headerAccessor)
    {
        Map msg = (Map) JSON.parse(json);
        Map<String, Object> data = new HashMap<String, Object>();

        // 这里拿到的user对象是在WebSocketChannelInterceptor拦截器中绑定上的对象


        // 向用户发送消息,第一个参数是接收人、第二个参数是浏览器订阅的地址，第三个是消息本身
        // 如果服务端要将消息发送给特定的某一个用户，
        // 可以使用SimpleMessageTemplate的convertAndSendToUser方法(第一个参数是用户的登陆名username)
        String address = "/queue/userTest/callBack";
        SMT.convertAndSendToUser(accountId, address, msg.get("msg"));
        data.put("msg", "callBack 消息已推送，消息内容：" + msg.get("msg"));
        return data;
    }


}
