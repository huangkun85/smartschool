using System;
using System.Collections.Generic;
using System.Text;

namespace SocketServer.RabbitMQ.Interfaces
{
    public interface IRabbitMqService
    {
        void Start();

        void Stop();

    }

    /// <summary>
    /// 向APP发送信息
    /// </summary>
    public interface IRabbitMqFactoryService : IRabbitMqService
    {
        void SendTopicMessage(string routeKey, string message);
    }

    /// <summary>
    /// 接受APP发送来的信息
    /// </summary>
    public interface IMqDirectConsumerService : IRabbitMqService
    {
        void AddListener(String handlerName, IMessageQueueMessageReceivedListener msgReceivedhandler);
    }

    /// <summary>
    /// 接受控制消息
    /// </summary>
    public interface IMqTopicConsumerService : IRabbitMqService
    {
        void AddListener(String handlerName, IMessageQueueMessageReceivedListener msgReceivedhandler);

    }
}
