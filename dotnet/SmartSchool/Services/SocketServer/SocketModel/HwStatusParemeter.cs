

/// <summary>
/// 
/// </summary>
namespace SmartSchool.Services.SocketServer.SocketModel
{
    /// <summary>
    ///     从接收到的命令中获取相关的参数
    /// </summary>
    public class HwStatusParemeter
    {
        /// <summary>
        ///     步数
        /// </summary>
        public int Steps { get; set; }

        /// <summary>
        ///     翻转次数
        /// </summary>
        public int Tumblings { get; set; }

        /// <summary>
        ///     电量百分数
        /// </summary>
        public int BatteryPercentage { get; set; }

        /// <summary>
        ///     硬件信息
        /// </summary>
        public string HwVersion { get; set; }

        /// <summary>
        ///     心率
        /// </summary>
        public int HwHeartBeat { get; set; }

        /// <summary>
        ///     硬件状态
        /// </summary>
        public int HwStatus { get; set; }

        /// <summary>
        ///     卫星个数
        /// </summary>
        public string SatelliteNum { get; set; }

        /// <summary>
        ///     位置状态
        /// </summary>
        public int LocationStatus { get; set; }

        /// <summary>
        ///     Latitude
        /// </summary>
        public string Latitude { get; set; }

        /// <summary>
        ///     Longitude
        /// </summary>
        public string Longitude { get; set; }
    }
}