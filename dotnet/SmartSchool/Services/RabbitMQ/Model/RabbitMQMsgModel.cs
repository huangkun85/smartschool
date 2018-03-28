using System.Collections.Generic;
using SmartSchool.Services.SocketServer.SocketModel;

namespace SmartSchool.Services.RabbitMQ.Model
{
    /// <summary>
    ///     创建命令下发到硬件所需要用的builder
    /// </summary>
    public class RabbitMQMsgModel
    {
        /// <summary>
        ///     设置计步时间段 或者 平台免打扰时间/
        /// </summary>
        public string[] ArrrayTimeSpan;


        /// <summary>
        ///     联系方式
        /// </summary>
        public List<HwContactModel> ContactList;

        /// <summary>
        ///     电话
        /// </summary>
        public string Phone;

        /// <summary>
        ///     类型(必要字段)
        /// </summary>
        public int CmdType { get; set; }

        /// <summary>
        ///     硬件编码（必要自导）
        /// </summary>
        public string HwCode { get; set; }

        /// <summary>
        ///     消息
        /// </summary>
        public string Message { get; set; }

        /// <summary>
        ///     时间间隔
        /// </summary>
        public int? Interval { get; set; }


        /// <summary>
        ///     状态
        /// </summary>
        public int? Status { get; set; }


        public string[] Phones { get; set; }
    }
}