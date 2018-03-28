namespace SocketServer.RabbitMQ.Interfaces
{
    /// <inheritdoc />
    public interface IRabbitMqFactoryService : IRabbitMqService
    {

        /// <summary>
        ///  发送Topic 消息
        /// </summary>
        /// <param name="routeKey"></param>
        /// <param name="message"></param>
        void SendTopicMessage(string routeKey, string message);


    }
}
