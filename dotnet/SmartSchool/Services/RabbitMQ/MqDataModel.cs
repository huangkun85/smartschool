using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SmartSchool.Services.RabbitMQ
{
    /// <summary>
    /// 控制命令类型
    /// 1000 : 关闭Socket 服务器
    /// 1001 : 开启Socket 服务器
    /// 2000 : 重启Socket 服务器
    /// </summary>
    public class MqDataModel
    {
        /// <summary>
        /// 命令类型
        /// </summary>
        public int CmdType { get; set; }

    }
}
