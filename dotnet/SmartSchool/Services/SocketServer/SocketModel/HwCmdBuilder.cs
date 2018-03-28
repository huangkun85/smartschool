using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SmartSchool.Services.SocketServer.SocketModel
{
    /// <summary>
    ///     代发命令生成工具类
    /// </summary>
    public static class HwCmdBuilder
    {
        /// <summary>
        ///     字符串转为命令信息
        /// </summary>
        /// <param name="strCmd">命令行</param>
        /// <param name="status">2为处理接收的命令</param>
        /// <returns></returns>
        public static HwCmd BuildHwCmdFromString(this string strCmd, int status = 2)
        {
            Console.WriteLine("**** {0}Buil CMD From String{1} ****", status, strCmd);

            var i = strCmd.Trim().IndexOf("[");

            if (i == -1)
            {
                Console.WriteLine("CMD Format Invalid:{0}", strCmd);
                var invalidCmd = new HwCmd
                {
                    Ext_CmdStatus = -1,
                    Ext_RawCommand = strCmd,
                    Ext_IsCmdValid = false,
                    Ext_ReceiveTime = DateTime.Now,
                    Format_Conent = "CMD Format Invalid"
                };
                return invalidCmd;
            }

            strCmd = strCmd.Substring(i, strCmd.Length - i);
            var cmdArray = strCmd.Substring(1, strCmd.Length - 2).Split('*');

            // 判断厂家是否符合
            if (cmdArray.Length != 4)
            {
                Console.WriteLine("CMD Format Invalid:{0}", strCmd);

                var invalidCmd = new HwCmd
                {
                    Ext_CmdStatus = -1,
                    Ext_RawCommand = strCmd,
                    Ext_IsCmdValid = false,
                    Ext_ReceiveTime = DateTime.Now,
                    Format_Conent = "CMD Format Invalid"
                };

                return invalidCmd;
            }

            // 判断厂家是否符合
            if (cmdArray[0].ToLowerInvariant() != "3g")
            {
                Console.WriteLine("Manufacturer Invalid:{0}", strCmd);

                var invalidCmd = new HwCmd
                {
                    Ext_CmdStatus = -2,
                    Ext_RawCommand = strCmd,
                    Ext_IsCmdValid = false,
                    Ext_ReceiveTime = DateTime.Now,
                    Format_Conent = "Manufacturer Invalid"
                };

                return invalidCmd;
            }


            var cmd = new HwCmd
            {
                Format_HwVendor = cmdArray[0],
                Format_HwCode = cmdArray[1],
                Format_HexLength = cmdArray[2],
                Format_Conent = cmdArray[3]
            };


            cmd.Ext_CmdStatus = status;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_RawCommand = strCmd;


            //获取参数内容
            var contentArray = cmd.Format_Conent.Split(',');

            // 分析命令并获取参数
            var strCmdName = contentArray[0].ToUpperInvariant();


            if (strCmdName.Equals("TKQ"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.TKQ;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }

            if (strCmdName.Equals("TKQ2"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.TKQ2;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }

            if (strCmdName.Equals("LK"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.LK;

                if (contentArray.Length == 4)
                {
                    cmd.Ext_CmdParameter = new HwStatusParemeter
                    {
                        Steps = contentArray[1].ToIntValue(),
                        Tumblings = contentArray[2].ToIntValue(),
                        BatteryPercentage = contentArray[3].ToIntValue()
                    };
                    cmd.Ext_IsCmdValid = true;
                    return cmd;
                }
            }

            if (strCmdName.Equals("VERNO"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.VERNO;

                if (contentArray.Length == 2)
                {
                    cmd.Ext_CmdParameter = new HwStatusParemeter
                    {
                        HwVersion = contentArray[1]
                    };
                    cmd.Ext_IsCmdValid = true;
                    return cmd;
                }
            }


            if (strCmdName.Equals("POWEROFF"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.POWEROFF;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }

            if (strCmdName.Equals("RESET"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.RESET;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("FIND"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.FIND;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("PHB"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.PHB;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("PEDO"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.PEDO;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }

            if (strCmdName.Equals("WALKTIME"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.WALKTIME;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("MESSAGE"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.MESSAGE;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("UPLOAD"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.UPLOAD;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("CALL"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.CALL;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }

            if (strCmdName.Equals("SOS"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.SOS;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }

            if (strCmdName.Equals("SOS1"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.SOS1;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("SOS2"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.SOS2;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }

            if (strCmdName.Equals("SOS3"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.SOS3;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("HRTSTART"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.HRTSTART;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }


            if (strCmdName.Equals("HEART"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.HEART;
                if (contentArray.Length == 2)
                {
                    cmd.Ext_CmdParameter = new HwStatusParemeter
                    {
                        HwHeartBeat = contentArray[1].ToIntValue()
                    };

                    cmd.Ext_IsCmdValid = true;
                    return cmd;
                }
            }

            if (strCmdName.Equals("CR"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.CR;
                cmd.Ext_IsCmdValid = true;
                return cmd;
            }

            if (strCmdName.Equals("UD"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.UD;
                if (contentArray.Length > 15)
                {
                    cmd.Ext_CmdParameter = new HwStatusParemeter
                    {
                        LocationStatus = contentArray[3].Equals("A") ? 1 : 0,
                        Latitude = contentArray[4],
                        Longitude = contentArray[6],
                        SatelliteNum = contentArray[11],
                        BatteryPercentage = contentArray[13].ToIntValue(),
                        Steps = contentArray[14].ToIntValue(),
                        Tumblings = contentArray[15].ToIntValue(),
                        HwStatus = contentArray[16].ToIntValue()
                    };

                    cmd.Ext_IsCmdValid = true;
                    return cmd;
                }
            }


            if (strCmdName.Equals("UD2"))
            {
                cmd.Ext_CmdType = CmdTypeEnum.UD2;
                if (contentArray.Length > 15)
                {
                    cmd.Ext_CmdParameter = new HwStatusParemeter
                    {
                        LocationStatus = contentArray[3].Equals("A") ? 1 : 0,
                        Latitude = contentArray[4],
                        Longitude = contentArray[6],
                        SatelliteNum = contentArray[11],
                        BatteryPercentage = contentArray[13].ToIntValue(),
                        Steps = contentArray[14].ToIntValue(),
                        Tumblings = contentArray[15].ToIntValue(),
                        HwStatus = contentArray[16].ToIntValue()
                    };

                    cmd.Ext_IsCmdValid = true;
                    return cmd;
                }
            }


            Console.WriteLine("**** UnSupport Cmd ****");
            return cmd;
        }

        #region 生产命令

        /// <summary>
        ///     生成待发送的TKQ[终端检测离线语音]
        /// </summary>
        /// <param name="strHwCode">硬件编码</param>
        /// <param name="strHwVendor">厂商名称</param>
        /// <returns></returns>
        public static HwCmd TKQ(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "TKQ"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.TKQ;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        /// <summary>
        ///     生成待发送的TKQ[终端请求好友录音下发]
        /// </summary>
        /// <param name="strHwCode">硬件编码</param>
        /// <param name="strHwVendor">厂商名称</param>
        /// <returns></returns>
        public static HwCmd TKQ2(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "TKQ2"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.TKQ2;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     生成待发送的LK [链路指令]
        /// </summary>
        /// <param name="strHwCode">硬件编码</param>
        /// <param name="strHwVendor">厂商名称</param>
        /// <returns></returns>
        public static HwCmd LK(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "LK"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.LK;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     生成待发送的 Upload [计步开关,1 打开 0 关闭]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="interval"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd UPLOAD(string strHwCode, int interval = 300, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("UPLOAD,{0}", interval < 0 ? 300 : interval)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.UPLOAD;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        public static HwCmd FACTORY(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "FACTORY"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.RESET;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        public static HwCmd RESET(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "RESET"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.RESET;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     生成待发送的 VERNO [查询版本号指令]
        ///     [3G*3925105617*0005*VERNO]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd VERNO(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "VERNO"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.VERNO;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        /// <summary>
        ///     关机
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd POWEROFF(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "POWEROFF"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.POWEROFF;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     生成待发送的 FIND [寻找手表]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd FIND(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "FIND"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.FIND;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     生成待发送的 PHB [设置电环号码]
        ///     号码,名字,号码,名字
        /// </summary>
        /// <param name="strHwCode">硬件Code</param>
        /// <param name="contactList">联系方式列表</param>
        /// <param name="strHwVendor">应尽厂商，默认3g</param>
        /// <returns></returns>
        public static HwCmd PHB(string strHwCode, List<HwContactModel> contactList, string strHwVendor = "3G")
        {
            var array = contactList.Select(p => p.toCmdParaContent()).ToArray();
            var para = string.Join(',', array);

            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("PHB,{0}", para)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.PHB;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     手表拆除报警的开关，[1打开，0关闭]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="status"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd REMOVE(string strHwCode, int status, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("REMOVE,{0}", status == 1 ? 1 : 0)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.REMOVE;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     手表拆除,短信报警的开关，[1打开，0关闭]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="status"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd REMOVESMS(string strHwCode, int status, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("REMOVESMS,{0}", status == 1 ? 1 : 0)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.REMOVESMS;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     生成待发送的 PEDO [计步开关,1 打开 0 关闭]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="status"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd PEDO(string strHwCode, int status, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("PEDO,{0}", status == 1 ? 1 : 0)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.PEDO;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        /// <summary>
        ///     设置计步时间段
        ///     [CS*YYYYYYYYYY*LEN*WALKTIME,时间段,时间段,时间段]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="arrrayTimeSpan"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd WALKTIME(string strHwCode, string[] arrrayTimeSpan, string strHwVendor = "3G")
        {
            if (arrrayTimeSpan.Length > 3 || arrrayTimeSpan.Length < 1)
                throw new ArgumentException("不能超过3个或小于1个时间段设置", "arrrayTimeSpan");

            var strPhone = string.Join(',', arrrayTimeSpan);

            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("WALKTIME,{0}", strPhone)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.WALKTIME;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        /// <summary>
        ///     设置免打扰的时间段范围，时间段只针对周一至周五，拦截终端的任何来电，周末无效。
        ///     平台发送:[CS*YYYYYYYYYY*LEN*SILENCETIME, 时间段, 时间段, 时间段, 时间段]
        ///     实例:[3g*5678901234*0037*SILENCETIME,21:10-7:30,21:10-7:30,21:10-7:30,21:10-7:30]
        ///     终端回复:[CS*YYYYYYYYYY*LEN*SILENCETIME] 实例:[3g*5678901234*000B*SILENCETIME]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="arrrayTimeSpan"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd SILENCETIME(string strHwCode, string[] arrrayTimeSpan, string strHwVendor = "3G")
        {
            if (arrrayTimeSpan.Length > 4 || arrrayTimeSpan.Length < 1)
                throw new ArgumentException("不能超过3个或小于1个时间段设置", "arrrayTimeSpan");

            var strPhone = string.Join(',', arrrayTimeSpan);

            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("SILENCETIME,{0}", strPhone)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.SILENCETIME;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        /// <summary>
        ///     生成待发送的 Message
        /// </summary>
        /// <param name="strHwCode">硬件编码</param>
        /// <param name="strMessage">消息内容</param>
        /// <param name="strHwVendor">供应商名</param>
        /// <returns></returns>
        public static HwCmd MESSAGE(string strHwCode, string strMessage, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("MESSAGE,{0}", strMessage.ToUnicode())
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.MESSAGE;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     生成待发送的 Call [拨打电话]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="strPhone"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd CALL(string strHwCode, string strPhone, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("CALL,{0}", strPhone)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.CALL;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     SOS 3个号码同时设置
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="arrayPhone"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd SOS(string strHwCode, string[] arrayPhone, string strHwVendor = "3G")
        {
            if (arrayPhone.Length > 3 || arrayPhone.Length < 1)
                throw new ArgumentException("不能超过3个或小于1个号码", "arrayPhone");

            var strPhone = string.Join(',', arrayPhone);

            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("SOS,{0}", strPhone)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.SOS;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        /// <summary>
        ///     SOS1
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="strPhone"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd SOS1(string strHwCode, string strPhone, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("SOS1,{0}", strPhone)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.SOS1;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     SOS2
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="strPhone"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd SOS2(string strHwCode, string strPhone, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("SOS2,{0}", strPhone)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.SOS2;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        /// <summary>
        ///     SOS3
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="strPhone"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd SOS3(string strHwCode, string strPhone, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("SOS3,{0}", strPhone)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.SOS3;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     [3G*8800000015*len*hrtstart,x]
        ///     x为上传间隔时间，单位秒,连续上传时最小时间不小于300秒，最大不超过65535.
        ///     实例：[3G*8800000015*len*hrtstart,300]        终端会每隔300秒检测一次心率
        ///     x为1则代表终端心率单次上传，上传完后自动关闭。
        ///     x为0则代表终端心率上传关闭。
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="interval"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd HRTSTART(string strHwCode, int interval, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = string.Format("hrtstart,{0}", interval)
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.HRTSTART;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }


        /// <summary>
        ///     生成待发送的CR [定位指令]
        /// </summary>
        /// <param name="strHwCode"></param>
        /// <param name="strHwVendor"></param>
        /// <returns></returns>
        public static HwCmd CR(string strHwCode, string strHwVendor = "3G")
        {
            var cmd = new HwCmd
            {
                Format_HwVendor = strHwVendor,
                Format_HwCode = strHwCode,
                Format_Conent = "CR"
            };

            cmd.Ext_CmdStatus = 1;
            cmd.Ext_IsCmdValid = true;
            cmd.Ext_ReceiveTime = DateTime.Now;
            cmd.Ext_CmdType = CmdTypeEnum.CR;
            cmd.Ext_CmdParameter = null;
            cmd.Ext_RawCommand = cmd.ToCmdString();
            return cmd;
        }

        #endregion

        #region 扩展

        /// <summary>
        ///     转为Unicode字符串
        /// </summary>
        /// <param name="s"></param>
        /// <returns></returns>
        public static string ToUnicode(this string s)
        {
            var charbuffers = s.ToCharArray();
            byte[] buffer;
            var sb = new StringBuilder();
            for (var i = 0; i < charbuffers.Length; i++)
            {
                buffer = Encoding.Unicode.GetBytes(charbuffers[i].ToString());
                sb.Append(string.Format("{0:X2}{1:X2}", buffer[1], buffer[0]));
            }

            return sb.ToString();
        }


        /// <summary>
        ///     命令计算16进制长度，共4位最大FFFF，高位在前
        /// </summary>
        /// <param name="strcmd"></param>
        /// <returns></returns>
        public static string ToHexLength(this string strcmd)
        {
            var t = Convert.ToString(strcmd.Length, 16).PadLeft(4, '0');
            return t.ToUpperInvariant();
        }

        #endregion
    }
}