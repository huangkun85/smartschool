namespace SocketServer.SocketService.SocketModel
{
    /// <summary>
    /// 命令的类型
    /// </summary>
    public enum HwCmdTypeEnum
    {
     
        Factory = -1,
        Reset = 0,
        Lk = 1,
        Verno = 2,
        Find = 3,
        Phb = 4,
        Pedo = 5,
        Message = 6,
        Upload = 7,
        Call = 8,
        Sos1 = 9,
        Sos2 = 10,
        Sos3 = 11,
        Sos = 12,
        Hrtstart = 13,
        Heart = -14,// 手表上传的数据
        Cr = 15,
        Walktime = 16,//计步时间段设置
        Tkq = 17,
        Poweroff = 18,
        Silencetime = 19,
        Tkq2 = 20,
        Remove = 21,
        Removesms = 22,
        Ud = 23,//定位数据
        Ud2 = 24 //补充数据

    }
}
