using System;
using System.Text;
using RabbitMQ.Client;

namespace SmartSchool.Services.RabbitMQ
{
    /// <summary>
    ///     服务器向手机APP发送消息，采用Topic模式，厂商名+硬件编码
    /// </summary>
    public class RabbitMQFactoryService : IRabbitMqFactoryService
    {
        /// <summary>
        ///     队列交换机名
        /// </summary>
        public RabbitMQFactoryService(RabbitMqConfigModel config)
        {
            MqConfig = config;
        }

        private ConnectionFactory MqConnectionFactory { get; set; }

        private IConnection MqConnection { get; set; }

        private IModel MqChannel { get; set; }

        private RabbitMqConfigModel MqConfig { get; }

        public void SendMqControlMessage(string message)
        {
            if (MqConnectionFactory == null)
                MqConnectionFactory = new ConnectionFactory
                {
                    HostName = MqConfig.HostName,
                    UserName = MqConfig.UserName,
                    Password = MqConfig.Password,
                    VirtualHost = MqConfig.VirtualHost
                };

            if (MqConnection == null || MqChannel == null || !MqConnection.IsOpen || !MqChannel.IsOpen)
            {
                MqConnection = MqConnectionFactory.CreateConnection();
                MqChannel = MqConnection.CreateModel();
                MqChannel.ExchangeDeclare(MqConfig.TopicExchangeName, ExchangeType.Topic);
            }


            var body = Encoding.UTF8.GetBytes(message);
            MqChannel.BasicPublish(MqConfig.TopicExchangeName, "Control", null, body);

            Console.WriteLine("*发送Topic队列:Ex:{0},Rou:{1},Msg:{2}", MqConfig.TopicExchangeName, "Control", message);
        }



    }
}