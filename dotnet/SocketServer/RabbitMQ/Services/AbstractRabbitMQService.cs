using System;
using RabbitMQ.Client;
using SocketServer.RabbitMQ.Interfaces;

namespace SocketServer.RabbitMQ.Services
{
    public abstract class AbstractRabbitMqService : IRabbitMqService
    {
        protected string DirectQueueName;

        protected string HostName;

        protected string Password;

        protected string TopicExchangeName;

        protected string UserName;

        protected string VHost;


        protected AbstractRabbitMqService(string hostName, string userName, string password, string vHost,
            string directQueueName, string topicExchangeName)
        {
            HostName = hostName;
            UserName = userName;
            Password = password;
            VHost = vHost;
            DirectQueueName = directQueueName;
            TopicExchangeName = topicExchangeName;
        }


        protected ConnectionFactory MqConnectionFactory { get; set; }

        protected IConnection MqConnection { get; set; }

        protected IModel MqChannel { get; set; }


        public void Start()
        {
            if (MqConnectionFactory == null)
                MqConnectionFactory = new ConnectionFactory
                {
                    HostName = HostName,
                    UserName = UserName,
                    Password = Password,
                    VirtualHost = VHost
                };

            if (MqConnection == null || MqChannel == null || !MqConnection.IsOpen || !MqChannel.IsOpen)
            {
                MqConnection = MqConnectionFactory.CreateConnection();
                MqChannel = MqConnection.CreateModel();
                Console.WriteLine("Connected to MQ Server");
                PostCreateConnection();
            }

            StartAfterGetConnection();
        }


        public void Stop()
        {
            throw new NotImplementedException();
        }

        /// <summary>
        ///     获得连接对象后执行
        /// </summary>
        protected abstract void StartAfterGetConnection();


        //创建新链接的时候处理该事件
        protected abstract void PostCreateConnection();
    }
}