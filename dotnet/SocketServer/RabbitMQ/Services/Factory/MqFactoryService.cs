using System;
using System.Text;
using NLog;
using RabbitMQ.Client;
using SocketServer.RabbitMQ.Interfaces;

namespace SocketServer.RabbitMQ.Services.Factory
{
    /// <summary>
    ///     服务器向手机APP发送消息，采用Topic模式，厂商名+硬件编码
    /// </summary>
    public class MqFactoryService : AbstractRabbitMqService, IRabbitMqFactoryService
    {
  

        public MqFactoryService(string hostName, string userName, string password, string virtualHostName, string exchangeName)
            : base(hostName, userName, password, virtualHostName, exchangeName, null)
        {
            ServiceLogger = LogManager.GetCurrentClassLogger();
        }


        public void SendTopicMessage(string routeKey, string message)
        {
            Start();

            var body = Encoding.UTF8.GetBytes(message);
            MqChannel.BasicPublish(ExchangeName, routeKey, null, body);

            String msg = $"*{DateTime.Now}Send Topic:Ex:{ExchangeName},Rou:{routeKey},Msg:{message}";
            ServiceLogger.Info(msg);
            Console.WriteLine(msg);
        }


        /// <summary>
        ///     发送者使用Topic 并指定一个路由
        /// </summary>
        protected override void DoAfterMqConnection()
        {
            MqChannel.ExchangeDeclare(ExchangeName, ExchangeType.Topic);
        }

        protected override void DoAfterCreateNewMqConnection()
        {
            var exchangeName = ExchangeName;
            if (exchangeName != null)
            {
                Console.WriteLine("Created New Connection on Topic Exchange [{0}]", exchangeName);
            }
        }
    }
}