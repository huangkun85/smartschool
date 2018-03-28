using System;
using System.Collections.Generic;
using System.Net.Sockets;

namespace SocketServer.SocketService.SocketModel
{
    /// <summary>
    ///     硬件连接的Socket客户端
    /// </summary>
    public class HwClient
    {
        #region 属性

        /// <summary>
        ///     硬件编码
        /// </summary>
        public string HwVendor { get; set; }

        /// <summary>
        ///     硬件编码
        /// </summary>
        public string HwCode { get; set; }

        /// <summary>
        ///     最后更新时间
        /// </summary>
        public DateTime UpdateTime { get; set; }

        /// <summary>
        ///     活动的Socket
        /// </summary>
        public Socket WorkSocket { get; set; }

        /// <summary>
        ///     当前客户端的所保存的命令信息
        /// </summary>
        public List<HwCmd> HwCmdList { get; set; }


        public HwClient()
        {
            HwCmdList = new List<HwCmd>();
        }

        #endregion
    }
}