using System;
using System.Collections.Generic;
using SocketServer.SocketService.SocketModel;

namespace SocketServer.Handler.Model
{
    /// <summary>
    /// 消息队列传输模型
    /// </summary>
    public class MessageQueueDataModel
    {
        /// <summary>
        /// 类型(必要字段)
        /// </summary>
        public int CmdType { get; set; }

        /// <summary>
        /// 硬件编码（必要自导）
        /// </summary>
        public String HwCode { get; set; }

        /// <summary>
        /// 消息
        /// </summary>
        public String Message { get; set; }

        /// <summary>
        /// 时间间隔
        /// </summary>
        public int? Interval { get; set; }

        /// <summary>
        /// 状态
        /// </summary>
        public int? Status { get; set; }

        /// <summary>
        /// 联系方式
        /// </summary>
        public List<HwCmdContact> ContactList { get; set; }

        /// <summary>
        /// 设置计步时间段 或者 平台免打扰时间
        /// </summary>
        public String[] ArrrayTimeSpan;

        /// <summary>
        /// 电话
        /// </summary>
        public String Phone;

        public string[] Phones { get; set; }

    }
}
