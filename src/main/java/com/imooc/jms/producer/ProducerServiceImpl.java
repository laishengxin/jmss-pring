package com.imooc.jms.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;

import javax.annotation.Resource;
import javax.jms.*;

public class ProducerServiceImpl implements ProducerService{

    @Autowired
    JmsTemplate jmsTemplate;

    //队列
    //@Resource(name="queueDestination")
    //主题
    @Resource(name="topicDestination")
    Destination destination;
    //使用JmsTemplate发送消息
    public void sendMessage(final String message) {
        jmsTemplate.send(destination, new MessageCreator() {
            public Message createMessage(Session session) throws JMSException {
             TextMessage textMessage=session.createTextMessage(message);
             return textMessage;
            }
        });
        System.out.println("发送消息："+message);
    }
}
