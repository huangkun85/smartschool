using System;
using System.Collections.Generic;
using System.Text;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using SocketServer.RabbitMQ.Interfaces;

namespace SocketServer.RabbitMQ.Services
{
    /// <summary>
    ///     订阅总渠道上的信息
    /// </summary>
    public class RabitMqConsumerService : AbstractRabbitMqService, IRabitMqConsumerService
    {
        public Dictionary<string, IMessageQueueMessageReceivedListener> Listeners;


        public RabitMqConsumerService(string hostName, string userName, string password, string vHost,
            string directQueueName, string topicExchangeName) : base(hostName, userName, password, vHost,
            directQueueName, topicExchangeName)
        {
            Listeners = new Dictionary<string, IMessageQueueMessageReceivedListener>();
        }

        public void AddListener(string handlerName, IMessageQueueMessageReceivedListener msgReceivedhandler)
        {
            Listeners.Add(handlerName, msgReceivedhandler);
        }


        protected override void StartAfterGetConnection()
        {
            MqChannel.ExchangeDeclare("ANY", ExchangeType.Direct);
            MqChannel.QueueDeclare(DirectQueueName, false, false, false, null);
            MqChannel.QueueBind(DirectQueueName, "amq.direct", "ANY", null);
            MqChannel.BasicQos(0, 1000, false);

            var consumer = new EventingBasicConsumer(MqChannel);

            consumer.Received += (ch, ea) =>
            {
                var message = Encoding.UTF8.GetString(ea.Body);
                Console.WriteLine("<<- Receive MSG -<({0})", message);

                //转发监听器
                foreach (var item in Listeners.Values)
                {
                    //调用监听器事件
                    item.NewMessageQueueMsgReciviced(message);
                }
            };


            MqChannel.BasicConsume(DirectQueueName, true, consumer);
            Console.WriteLine("[Direct] Listening to Queue" + DirectQueueName);
        }

        protected override void PostCreateConnection()
        {

        }
    }
}