# jmss-pring
使用Spring集成的JMS连接成ActiveMQ
**初识消息中间件

加入消息中间件

**中间件运行流程

**消息中间件带来的好处
 1.解耦
 2.异步
 3.横向扩展
 4.安全可靠
 5.顺序保证（kaflaka）

**什么是中间件？
     非底层操作系统软件，非业务应用软件，不是直接给最终用户使用的，不能直接给客户带来价值的软件统称为中间件。消息中间件关注与数据的发送和接受，利用高效可靠的异步消息传递机制集成分布式系统。

**什么是JMS？
      Java消息服务（Java Message Service）即JMS，是一个Java平台中关于面向消息中间件的API，用于两个应用程序之间或分布式系统中发送消息，进行异步通信。不是协议，只是API的规范。

**什么是AMQP?
     AMQP(advanced message queuing protocol)是一个提供统一消息服务的应用层标准协议，基于此协议的客户端与消息中间件可传递消息，并不受客户端、中间件不同产品，不同开发语言等条件限制。

**JMS和AMQP的对比

**常见的消息中间件对比

**.ActiveMQ适合中小型企业


**RabbitMQ
          RabbitMQ是一个开源的AMQP实现，服务器端用Erlang语言编写。用于分布式系统中的存储转发消息，在易用性、扩展性、高可用性等方面表现不俗。
          RabbitMQ特性


	*           支持多种客户端，如：Python,Ruby,.NET,Java,JMS,C,PHP,ActionScript等。
	*           AMQP的完整实现(vhost,Exchange,Binding,Routing Key等)
	*           事务支持、发布确认
	*           消息持久化



**什么是KafKa？
      Kafka是一种高吞吐量的分布式发布订阅消息系统，是一个分布式、分区的、可靠的分布式日志存储服务。它通过一种独一无二的设计提供了一个消息系统的功能。不是标准的中间件。

     特性：

	*      通过O(1)的磁盘数据结构提供消息的持久化，这种结构对于即使数以TB的消息存储也能够保持长时间的稳定性能。  
	*      高吞吐量：即使是非常普通的硬件Kafka也可以支持每秒数百万的消息。
	*      Partition、Consumer Group(消费者分组)   

     


****** * * * * * * JMS规范
**JMS相关概念

	*     提供者：实现JMS规范的消息中间服务器
	*     客户端：发送或接收消息的应用程序
	*     生产者/发布者：创建并发送消息的客户端
	*     消费者/订阅者：接收并处理消息的客户端
	*     消息：应用程序之间传递的数据内容
	*     消息模式：在客户端之间传递消息的方式，JMS中定义了主题和队列两种模式


**队列模式

	*      客户端包括生产者和消费者
	*      队列中的消息只能被一个消费者消费
	*      消费者可以随时消费队列中的消息



生产者代码：
package com.imooc.jms.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AppProducer {

//    private static final String url = "tcp://192.168.31.10:61616";
    private static final String url = "tcp://lailai:61616";//要查看启动时候的TCP的地址
    private static final String quequeName = "queue-test";
    public static void main(String[] args) throws JMSException {
        //1.创建ConnectionFactory
        ConnectionFactory connetionFactory = new ActiveMQConnectionFactory(url);

        //2.创建Connection
        Connection connection = connetionFactory.createConnection();

        //3.启动连接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //5.创建一个目标
        Destination destination=session.createQueue(quequeName);

        //6.创建一个生产者
        MessageProducer producer=session.createProducer(destination);

        for (int i=0;i<100;i++){
            //7.创建消息
            TextMessage textMessage=session.createTextMessage("test"+i);
            //8.发布消息
            producer.send(textMessage);

            System.out.println("发送消息"+textMessage.getText());
        }

        //9.关闭连接
        connection.close();

    }

}
          
消费者代码
package com.imooc.jms.queue;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AppConsumer {

    private static final String url = "tcp://lailai:61616";//要查看启动时候的TCP的地址
    private static final String quequeName = "queue-test";
    public static void main(String[] args) throws JMSException {
        //1.创建ConnectionFactory
        ConnectionFactory connetionFactory = new ActiveMQConnectionFactory(url);

        //2.创建Connection
        Connection connection = connetionFactory.createConnection();

        //3.启动连接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //5.创建一个目标
        Destination destination=session.createQueue(quequeName);

        //6.创建一个消费者
        MessageConsumer consumer = session.createConsumer(destination);

        //7.创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage textMessage=(TextMessage)message;
                try {
                    System.out.println("接收消息"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //8.关闭连接
      //  connection.close();

    }

}

**主题模式(模型)

	*      客户端包括发布者和订阅者
	*      队列中的消息能被所有dingyue消费者消费
	*      消费者可以随时消费队列中的消息
	*     订阅者要提前订阅，不然接收不到******




发布者代码
package com.imooc.jms.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AppProducer {

//    private static final String url = "tcp://192.168.31.10:61616";
    private static final String url = "tcp://lailai:61616";//要查看启动时候的TCP的地址
    private static final String topicName = "topic-test";
    public static void main(String[] args) throws JMSException {
        //1.创建ConnectionFactory
        ConnectionFactory connetionFactory = new ActiveMQConnectionFactory(url);

        //2.创建Connection
        Connection connection = connetionFactory.createConnection();

        //3.启动连接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //5.创建一个目标
        Destination destination=session.createTopic(topicName);

        //6.创建一个生产者
        MessageProducer producer=session.createProducer(destination);

        for (int i=0;i<100;i++){
            //7.创建消息
            TextMessage textMessage=session.createTextMessage("test"+i);
            //8.发布消息
            producer.send(textMessage);

            System.out.println("发送消息"+textMessage.getText());
        }

        //9.关闭连接
        connection.close();

    }

}


订阅者代码

package com.imooc.jms.topic;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class AppConsumer {

    private static final String url = "tcp://lailai:61616";//要查看启动时候的TCP的地址
    private static final String topicName = "topic-test";
    public static void main(String[] args) throws JMSException {
        //1.创建ConnectionFactory
        ConnectionFactory connetionFactory = new ActiveMQConnectionFactory(url);

        //2.创建Connection
        Connection connection = connetionFactory.createConnection();

        //3.启动连接
        connection.start();

        //4.创建会话
        Session session = connection.createSession(false,Session.AUTO_ACKNOWLEDGE);

        //5.创建一个目标
        Destination destination=session.createTopic(topicName);

        //6.创建一个消费者
        MessageConsumer consumer = session.createConsumer(destination);

        //7.创建一个监听器
        consumer.setMessageListener(new MessageListener() {
            public void onMessage(Message message) {
                TextMessage textMessage=(TextMessage)message;
                try {
                    System.out.println("接收消息"+textMessage.getText());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });

        //8.关闭连接
      //  connection.close();

    }

}

代码github地址：https://github.com/laishengxin/jms-project.git
***JMS规范
1.JMS编码接口

	*       ConnetionFactory 用于创建连接到消息中间件的连接工厂
	*       Connection 代表了应用程序和消息服务器之间的通信链路
	*       Destination:指消息发布和接受地点，包括主题和队列
	*       Session表示一个单线程的上下文，用于发送和接收消息
	*       MessageConsumer 由会话创建，用于接收发送到目标消息
	*       MessageProducer 由会话创建，用于发送消息到目标
	*       Message是在消费者和生产者之间传送对象，消息投，一组消息属性，一个消息体


2.JMS编码接口之间的关系

**代码演练
  
Spring jms理论

使用Spring集成JMS连接ActiveMQ
*ConnectionFactory 用于管理连接的连接工厂
*JmsTemplate用于发送和接收消息的模板类
*MessageListerner消息监听器


**ConnectionFactory
  1.一个Spring为我们提供的连接池
  2.JmsTemplate每次发消息都会重新创建连接，会话和productor
  3.Spring中提供了SingleConnectionFactory和CachingConnectionFactory

**JmsTemplate
  *是spring提供的，只需向spring容器内注册这个类就可以使用JmsTemplate方便的操作jms

  *JmsTemplate是线程安全的，可以在整个应用范围使用。

**MessageListerner
      *实现一个onMessage方法，该方法只接收一个Message参数

源码地址：https://github.com/laishengxin/jmss-pring



























