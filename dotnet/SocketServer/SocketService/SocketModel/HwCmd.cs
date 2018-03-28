using System;

namespace SocketServer.SocketService.SocketModel
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

            if (ExtIsCmdValid)
                strRes = string.Format(strFormat, FormatHwVendor, FormatHwCode, FormatConent.ToHexLength(),
                    FormatConent);

            return strRes;
        }

        #region 扩展属性

        /// <summary>
        ///     原始的字符型命令内容
        /// </summary>
        public string ExtRawCommand { get; set; }

        /// <summary>
        ///     指令的接收时间
        /// </summary>
        public DateTime ExtReceiveTime { get; set; }

        /// <summary>
        ///     命令行是否正确
        /// </summary>
        public bool ExtIsCmdValid { get; set; }

        /// <summary>
        ///     状态设置
        /// </summary>
        public int ExtCmdStatus { get; set; }

        /// <summary>
        ///     指令的类型枚举
        /// </summary>
        public HwCmdTypeEnum ExtCmdType { get; set; }


        /// <summary>
        ///     指令的携带参数类型
        /// </summary>
        public HwStatusParemeter ExtCmdParameter { get; set; }

        #endregion

        #region 指令的有效内容格式

        /// <summary>
        ///     第一部分：厂商标识 3G =三基同创
        /// </summary>
        public string FormatHwVendor { get; set; }

        /// <summary>
        ///     第二部分：硬件的唯一标识
        /// </summary>
        public string FormatHwCode { get; set; }

        /// <summary>
        ///     第三部分：内容长度
        /// </summary>
        public string FormatHexLength { get; set; }

        /// <summary>
        ///     第四部分: 指令内容
        /// </summary>
        public string FormatConent { get; set; }

        #endregion
    }


    /// <summary>
    ///     联系方式
    ///     号码不超过20个ascii字符，姓名不超过10个Unicode字符
    /// </summary>
    public class HwCmdContact
    {
        /// <summary>
        ///     构造函数
        /// </summary>
        /// <param name="phone"></param>
        /// <param name="name"></param>
        public HwCmdContact(string phone, string name)
        {
            Phone = phone;
            Name = name;
        }

        /// <summary>
        ///     电话号码
        /// </summary>
        public string Phone { get; }

        /// <summary>
        ///     联系名
        /// </summary>
        public string Name { get; }


        public override string ToString() => string.Format("{0},{1}", Phone, Name);

        /// <summary>
        ///     生成参数内容
        ///     列表号码:  Ascii字符
        ///     名字:      Unicode编码
        /// </summary>
        /// <returns></returns>
        public string ToCmdParaContent()
        {
            var strUnicodeName = Name.ToUnicode();

            if (Phone.Length > 20) throw new ArgumentException("电话号码长度超出", $"Phone");

            //if (strUnicodeName.Length > 10)
            //{
            //    throw new ArgumentException("联系名长度超出", "Name");
            //}

            return string.Format("{0},{1}", Phone, strUnicodeName);
        }
    }



    /// <summary>
    /// 从接收到的命令中获取相关的参数
    /// </summary>
    public class HwStatusParemeter
    {
        /// <summary>
        /// 步数
        /// </summary>
        public int Steps { get; set; }

        /// <summary>
        /// 翻转次数
        /// </summary>
        public int Tumblings { get; set; }
        /// <summary>
        /// 电量百分数
        /// </summary>
        public int BatteryPercentage { get; set; }

        /// <summary>
        /// 硬件信息
        /// </summary>
        public String HwVersion { get; set; }

        /// <summary>
        /// 心率
        /// </summary>
        public int HwHeartBeat { get; set; }

        /// <summary>
        /// 硬件状态
        /// </summary>
        public int HwStatus { get; set; }

        /// <summary>
        /// 卫星个数
        /// </summary>
        public string SatelliteNum { get; set; }

        /// <summary>
        /// 位置状态
        /// </summary>
        public string LocationStatus { get; set; }

        /// <summary>
        /// Latitude
        /// </summary>
        public string Latitude { get; set; }

        /// <summary>
        /// Longitude
        /// </summary>
        public string Longitude { get; set; }
    }
}