using System;
using System.Text;
using RabbitMQ.Client;
using SmartSchool.Services.RabbitMQ.Model;

namespace SmartSchool.Services.RabbitMQ
{
    /// <summary>
    ///     服务器向手机APP发送消息，采用Topic模式，厂商名+硬件编码
    /// </summary>
    public class RabbitMQFactoryService : IRabbitMQFactoryService
    {
        /// <summary>
        ///     队列交换机名
        /// </summary>
        public RabbitMQFactoryService(RabbitMQConfigModel config)
        {
            _MQ_Config = config;
        }

        private ConnectionFactory _MQ_ConnectionFactory { get; set; }

        private IConnection _MQ_Connection { get; set; }

        private IModel _MQ_Channel { get; set; }

        private RabbitMQConfigModel _MQ_Config { get; }


        /// <summary>
        ///     发送消息队列
        /// </summary>
        /// <param name="hwCode"></param>
        /// <param name="message"></param>
        /// <param name="hwVendor"></param>
        public void SendMQ(string hwCode, string message, string hwVendor = "3G")
        {
            var rouKey = string.Format("{0}.{1}", hwVendor, hwCode);

            if (_MQ_ConnectionFactory == null)
                _MQ_ConnectionFactory = new ConnectionFactory
                {
                    HostName = _MQ_Config.HostName,
                    UserName = _MQ_Config.UserName,
                    Password = _MQ_Config.Password,
                    VirtualHost = _MQ_Config.VirtualHost
                };

            if (_MQ_Connection == null || _MQ_Channel == null || !_MQ_Connection.IsOpen || !_MQ_Channel.IsOpen)
            {
                _MQ_Connection = _MQ_ConnectionFactory.CreateConnection();
                _MQ_Channel = _MQ_Connection.CreateModel();
                _MQ_Channel.ExchangeDeclare(_MQ_Config.TopicExchangeName, ExchangeType.Topic);
            }


            var body = Encoding.UTF8.GetBytes(message);
            _MQ_Channel.BasicPublish(_MQ_Config.TopicExchangeName, rouKey, null, body);

            Console.WriteLine("*发送Topic队列:Ex:{0},Rou:{1},Msg:{2}", _MQ_Config.TopicExchangeName, rouKey, message);
        }
    }
}