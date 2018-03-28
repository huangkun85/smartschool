using System;

namespace SmartSchool.Services.SocketServer.SocketModel
{
    /// <summary>
    ///     基础的数据命令模型
    /// </summary>
    public class HwCmd
    {
        /// <summary>
        ///     获取命令的字符串形式
        /// </summary>
        /// <returns></returns>
        public string ToCmdString()
        {
            // 命令格式
            var strFormat = "[{0}*{1}*{2}*{3}]";
            string strRes = null;

            if (Ext_IsCmdValid)
                strRes = string.Format(strFormat, Format_HwVendor, Format_HwCode, Format_Conent.ToHexLength(),
                    Format_Conent);

            return strRes;
        }

        #region 扩展属性

        /// <summary>
        ///     原始的字符型命令内容
        /// </summary>
        public string Ext_RawCommand { get; set; }

        /// <summary>
        ///     指令的接收时间
        /// </summary>
        public DateTime Ext_ReceiveTime { get; set; }

        /// <summary>
        ///     命令行是否正确
        /// </summary>
        public bool Ext_IsCmdValid { get; set; }

        /// <summary>
        ///     状态设置
        /// </summary>
        public int Ext_CmdStatus { get; set; }

        /// <summary>
        ///     指令的类型枚举
        /// </summary>
        public CmdTypeEnum Ext_CmdType { get; set; }


        /// <summary>
        ///     指令的携带参数类型
        /// </summary>
        public HwStatusParemeter Ext_CmdParameter { get; set; }

        #endregion

        #region 指令的有效内容格式

        /// <summary>
        ///     第一部分：厂商标识 3G =三基同创
        /// </summary>
        public string Format_HwVendor { get; set; }

        /// <summary>
        ///     第二部分：硬件的唯一标识
        /// </summary>
        public string Format_HwCode { get; set; }

        /// <summary>
        ///     第三部分：内容长度
        /// </summary>
        public string Format_HexLength { get; set; }

        /// <summary>
        ///     第四部分: 指令内容
        /// </summary>
        public string Format_Conent { get; set; }

        #endregion
    }
}