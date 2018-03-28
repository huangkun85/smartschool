namespace SocketServer.Config
{
    public class AppSetting
    {
        /// <summary>
        ///     数据库连接字符串
        /// </summary>
        public string DbConnectionString { get; set; }

        /// <summary>
        ///     Socket端口
        /// </summary>
        public int SocketServerPort { get; set; }


        public string SocketServerId { get; set; }

        /// <summary>
        ///     消息队列配置
        /// </summary>
        public MqSetting RabbitMqSetting { get; set; }
    }
}