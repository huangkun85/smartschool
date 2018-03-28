using System;
using System.Collections.Generic;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using SocketServer.RabbitMQ.Interfaces;

namespace SocketServer.RabbitMQ.Services.Comsumer
{
    /// <summary>
    ///     Topic 消费者：订阅控制信息
    /// </summary>
    public class MqTopicConsumerService : AbstractRabbitMqService, IMqTopicConsumerService
    {
        private readonly Dictionary<string, IMessageQueueMessageReceivedListener> _listeners;

        private readonly String _routingKey;



        public MqTopicConsumerService(string hostName, string userName, string password, string virtualHostName, string exchangeName, string queueName, String routingKey = "socket_server_id")
            : base(hostName, userName, password, virtualHostName, exchangeName, queueName)
        {
            _listeners = new Dictionary<string, IMessageQueueMessageReceivedListener>();
            _routingKey = routingKey;
        }


        public void AddListener(string handlerName, IMessageQueueMessageReceivedListener msgReceivedhandler)
        {
            _listeners.Add(handlerName, msgReceivedhandler);
        }


        protected override void DoAfterMqConnection()
        {
            MqChannel.ExchangeDeclare(ExchangeName, ExchangeType.Topic);

            var topicQueueName = MqChannel.QueueDeclare().QueueName;

            MqChannel.QueueBind(queue: topicQueueName, exchange: ExchangeName, routingKey: _routingKey);

            var consumer = new EventingBasicConsumer(MqChannel);

            consumer.Received += (ch, ea) =>
            {
                var message = Encoding.UTF8.GetString(ea.Body);
                Console.WriteLine("<<- Receive MSG -<({0})", message);

                //转发监听器
                foreach (var item in _listeners.Values)
                    //调用监听器事件
                    item.NewMessageQueueMsgReciviced(message);
            };


            MqChannel.BasicConsume(QueueName, true, consumer);

            Console.WriteLine("[Topic] Listening [{0}]-[{1}]", _routingKey, topicQueueName);
        }



        protected override void DoAfterCreateNewMqConnection()
        {
        }
    }
}