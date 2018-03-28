using System;
using System.Collections.Generic;
using System.Linq;

namespace SocketServer.SocketService.SocketModel
{
    public static class HwClientExtension
    {
        /// <summary>
        ///     转为int，不能转换则返回-1
        /// </summary>
        /// <param name="pValue"></param>
        /// <returns></returns>
        public static int ToIntValue(this string pValue)
        {
            int.TryParse(pValue, out var temp);
            return temp;
        }


        /// <summary>
        ///     将新收到的命令添加到列表中，当超过指定的容量或者时间超过8个小时，清除数据
        /// </summary>
        /// <param name="hwCmdList"></param>
        /// <param name="hwCmd"></param>
        /// <returns></returns>
        public static void ExtAddTo(this List<HwCmd> hwCmdList, HwCmd hwCmd)
        {
            Console.WriteLine("*>Ext Add Cmd{0} to List", hwCmd.ToCmdString());
            hwCmdList.Add(hwCmd);
        }


        /// <summary>
        ///     寻找客户端
        /// </summary>
        /// <param name="hwClientList"></param>
        /// <param name="hwCode"></param>
        /// <param name="hwVendor"></param>
        /// <returns></returns>
        public static HwClient FindHwClient(this List<HwClient> hwClientList, string hwCode, string hwVendor = "3G")
        {
            var existClient = hwClientList
                .FirstOrDefault(
                    x => x.HwCode.Equals(hwCode) &&
                         x.HwVendor.Equals(hwVendor)
                );

            return existClient;
        }


        /// <summary>
        ///     创建或者更新客户端列表
        /// </summary>
        /// <param name="hwClientList"></param>
        /// <param name="hwClient"></param>
        /// <returns></returns>
        public static HwClient CreateOrUpdateHwClient(this List<HwClient> hwClientList, HwClient hwClient)
        {
            var existClient = hwClientList.FindHwClient(hwClient.HwCode, hwClient.HwVendor);

            //当前客户端列表不存在该链接
            if (existClient == null)
            {
                hwClientList.Add(hwClient);
                Console.WriteLine("-新建客户端[{0}],[{1}]", hwClient.HwCode, DateTime.Now);
                return hwClient;
            }

            Console.WriteLine("-更新客户端[{0}],[{1}]", hwClient.HwCode, DateTime.Now);
            existClient.UpdateTime = hwClient.UpdateTime;
            existClient.WorkSocket = hwClient.WorkSocket;
            return existClient;
        }

      

    }
}