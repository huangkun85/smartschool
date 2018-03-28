using RabbitMQ.Client;

using SocketServer.RabbitMQ.Interfaces;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace SocketServer.RabbitMQ.Services
{
    /// <summary>
    /// 服务器向手机APP发送消息，采用Topic模式，厂商名+硬件编码
    /// </summary>
    public class RabbitMqFactoryService : AbstractRabbitMqService, IRabbitMqFactoryService
    {
        /// <summary>
        /// 
        /// </summary>
        /// <param name="hostName"></param>
        /// <param name="userName"></param>
        /// <param name="password"></param>
        /// <param name="vHost"></param>
        /// <param name="directQueueName"></param>
        /// <param name="topicExchangeName"></param>
        public RabbitMqFactoryService(string hostName, string userName, string password, string vHost, string directQueueName, string topicExchangeName) : base(hostName, userName, password, vHost, directQueueName, topicExchangeName)
        {

        }



        public void SendTopicMessage(string routeKey, string message)
        {
            Start();

            var body = Encoding.UTF8.GetBytes(message);
            MqChannel.BasicPublish(this.TopicExchangeName, routeKey, null, body);
            Console.WriteLine("*Send Topic:Ex:{0},Rou:{1},Msg:{2}", this.TopicExchangeName, routeKey, message);
        }



        /// <summary>
        /// 发送者使用Topic 并指定一个路由
        /// </summary>
        protected override void StartAfterGetConnection()
        {
            MqChannel.ExchangeDeclare(this.TopicExchangeName, ExchangeType.Topic);
        }

        protected override void PostCreateConnection()
        {

        }
    }
}
