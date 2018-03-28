using System;
using System.Collections.Generic;
using System.Linq;

namespace SmartSchool.Services.SocketServer.SocketModel
{
    public static class HwExtension
    {
        /// <summary>
        ///     转为int，不能转换则返回-1
        /// </summary>
        /// <param name="pValue"></param>
        /// <returns></returns>
        public static int ToIntValue(this string pValue)
        {
            var temp = -1;
            var res = int.TryParse(pValue, out temp);
            return temp;
        }


        /// <summary>
        ///     将新收到的命令添加到列表中，当超过指定的容量或者时间超过8个小时，清除数据
        /// </summary>
        /// <param name="HwCmdList"></param>
        /// <param name="hwCmd"></param>
        /// <returns></returns>
        public static void ExtAddTo(this List<HwCmd> HwCmdList, HwCmd hwCmd)
        {
            Console.WriteLine("*>Ext Add Cmd{0} to List", hwCmd.ToCmdString());
            HwCmdList.Add(hwCmd);
        }


        /// <summary>
        ///     寻找客户端
        /// </summary>
        /// <param name="HwClientList"></param>
        /// <param name="hwCode"></param>
        /// <param name="hwVendor"></param>
        /// <returns></returns>
        public static HwClient FindHwClient(this List<HwClient> HwClientList, string hwCode, string hwVendor = "3G")
        {
            var existClient = HwClientList
                .FirstOrDefault(
                    x => x.HwCode.Equals(hwCode) &&
                         x.HwVendor.Equals(hwVendor)
                );

            return existClient;
        }


        /// <summary>
        ///     创建或者更新客户端列表
        /// </summary>
        /// <param name="HwClientList"></param>
        /// <param name="hwClient"></param>
        /// <returns></returns>
        public static HwClient CreateOrUpdateHwClient(this List<HwClient> HwClientList, HwClient hwClient)
        {
            var existClient = HwClientList.FindHwClient(hwClient.HwCode, hwClient.HwVendor);

            //当前客户端列表不存在该链接
            if (existClient == null)
            {
                HwClientList.Add(hwClient);
                Console.WriteLine("-新建客户端[{0}],[{1}]", hwClient.HwCode, DateTime.Now);
                return hwClient;
            }

            Console.WriteLine("-更新客户端[{0}],[{1}]", hwClient.HwCode, DateTime.Now);
            existClient.UpdateTime = hwClient.UpdateTime;
            existClient.WorkSocket = hwClient.WorkSocket;
            return existClient;
        }

        /// <summary>
        ///     客户端的链接的清理工作
        /// </summary>
        /// <param name="HwClient"></param>
        public static void DoClean(this HwClient HwClient)
        {
            //删除超过8个小时的数据
            HwClient.HwCmdList.RemoveAll(x => (DateTime.Now - x.Ext_ReceiveTime).Hours > 8);
        }


        #region 操作命令相关

        #endregion
    }
}