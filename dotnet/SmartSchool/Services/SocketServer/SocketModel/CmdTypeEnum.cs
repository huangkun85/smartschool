namespace SmartSchool.Services.SocketServer.SocketModel
{
    /// <summary>
    ///     命令的类型
    /// </summary>
    public enum CmdTypeEnum
    {
        FACTORY = -1,
        RESET = 0,
        LK = 1,
        VERNO = 2,
        FIND = 3,
        PHB = 4,
        PEDO = 5,
        MESSAGE = 6,
        UPLOAD = 7,
        CALL = 8,
        SOS1 = 9,
        SOS2 = 10,
        SOS3 = 11,
        SOS = 12,
        HRTSTART = 13,
        HEART = -14, // 手表上传的数据
        CR = 15,
        WALKTIME = 16, //计步时间段设置
        TKQ = 17,
        POWEROFF = 18,
        SILENCETIME = 19,
        TKQ2 = 20,
        REMOVE = 21,
        REMOVESMS = 22,
        UD = 23, //定位数据
        UD2 = 24 //补充数据
    }
}