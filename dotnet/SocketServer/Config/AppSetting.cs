using System;
using System.Collections.Generic;
using System.Text;

namespace SocketServer.Config
{
    public class AppSetting
    {
        /// <summary>
        ///   数据库连接字符串
        /// </summary>
        public String DbConnectionString { get; set; }

        /// <summary>
        /// Socket端口
        /// </summary>
        public int SocketServerPort { get; set; }

        /// <summary>
        /// 消息队列配置
        /// </summary>
        public RabbitMq RabbitMq { get; set; }
    }
}
