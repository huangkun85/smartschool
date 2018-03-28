using System;
using System.Collections.Generic;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using SocketServer.RabbitMQ.Interfaces;

namespace SocketServer.RabbitMQ.Services.Comsumer
{

    /// <summary>
    /// Direct 消费者，指定渠道， 订阅总渠道上的信息，处理 手机APP向某个指定驱动发送消息的订阅
    /// </summary>
    public class MqDirectConsumerService : AbstractRabbitMqService, IMqDirectConsumerService
    {
        public Dictionary<string, IMessageQueueMessageReceivedListener> Listeners;

        private const string Routekey = "AppToServer";


        public MqDirectConsumerService(string hostName, string userName, string password, string virtualHostName, string exchangeName, string queueName)
            : base(hostName, userName, password, virtualHostName, exchangeName, queueName)
        {
            Listeners = new Dictionary<string, IMessageQueueMessageReceivedListener>();
        }


        public void AddListener(string handlerName, IMessageQueueMessageReceivedListener msgReceivedhandler)
        {
            Listeners.Add(handlerName, msgReceivedhandler);
        }


        protected override void DoAfterMqConnection()
        {
            MqChannel.ExchangeDeclare(ExchangeName, ExchangeType.Direct);
            MqChannel.QueueDeclare(QueueName, false, false, false, null);
            MqChannel.QueueBind(QueueName, ExchangeName, Routekey, null);
            MqChannel.BasicQos(0, 1000, false);

            var consumer = new EventingBasicConsumer(MqChannel);

            consumer.Received += (ch, ea) =>
            {
                var message = Encoding.UTF8.GetString(ea.Body);
                Console.WriteLine("<<- Receive MSG -<({0})", message);

                //转发监听器处理

                foreach (var item in Listeners.Values)
                {
                    //调用监听器事件
                    item.NewMessageQueueMsgReciviced(message);
                }
            };

            MqChannel.BasicConsume(QueueName, true, consumer);
            Console.WriteLine("[Direct] Listening to Queue [{0}]", QueueName);
        }

        protected override void DoAfterCreateNewMqConnection()
        {

        }
    }
}