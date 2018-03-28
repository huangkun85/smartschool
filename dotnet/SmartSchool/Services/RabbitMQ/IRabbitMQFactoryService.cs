namespace SmartSchool.Services.RabbitMQ
{
    public interface IRabbitMqFactoryService
    {
        /// <summary>
        /// 向消息队列发送控制命令
        /// </summary>
        /// <param name="message"></param>
        void SendMqControlMessage(string message);
    }
}