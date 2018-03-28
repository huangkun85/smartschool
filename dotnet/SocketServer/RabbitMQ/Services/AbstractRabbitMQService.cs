using System;
using Microsoft.Extensions.Logging;
using NLog;
using RabbitMQ.Client;
using SocketServer.RabbitMQ.Interfaces;

namespace SocketServer.RabbitMQ.Services
{
    public abstract class AbstractRabbitMqService : IRabbitMqService
    {

        public string HostName;

        public string UserName;

        public string Password;

        public string VirtualHostName;

        public string ExchangeName;

        public string QueueName;


        public Logger ServiceLogger;


        protected AbstractRabbitMqService(string hostName, string userName, string password, string virtualHostName, string exchangeName, string queueName)
        {
            HostName = hostName;
            UserName = userName;
            Password = password;
            VirtualHostName = virtualHostName;
            ExchangeName = exchangeName;
            QueueName = queueName;
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
                    VirtualHost = VirtualHostName
                };

            if (MqConnection == null || MqChannel == null || !MqConnection.IsOpen || !MqChannel.IsOpen)
            {
                MqConnection = MqConnectionFactory.CreateConnection();
                MqChannel = MqConnection.CreateModel();
                Console.WriteLine("Connected to MQ Server");
                DoAfterCreateNewMqConnection();
            }

            DoAfterMqConnection();
        }


        public void Stop()
        {

            MqChannel.Close();
            MqConnection.Close();

            Console.WriteLine("Stop RabbitMQ Service");
        }


        /// <summary>
        ///     获得连接对象后执行
        /// </summary>
        protected abstract void DoAfterMqConnection();


        /// <summary>
        /// 新建MQ连接
        /// </summary>
        protected abstract void DoAfterCreateNewMqConnection();
    }
}