using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using DataBaseAccess;
using DataBaseAccess.Dtos;
using Newtonsoft.Json;
using SmartSchool.Services.RabbitMQ;
using SmartSchool.Services.SocketServer.SocketModel;

namespace SmartSchool.Services.SocketServer
{
    /// <summary>
    ///     Socket 服务器
    ///     参考引用
    ///     https://msdn.microsoft.com/zh-cn/library/fx6588te(v=vs.110).aspx
    /// </summary>
    public class SocketServer : ISocketServer
    {
        #region 公共属性

        private ApplicationDbContext _applicationDbContext { get; set; }

        /// <summary>
        ///     Message Queue
        /// </summary>
        public IRabbitMQFactoryService MQService { get; set; }

        /// <summary>
        ///     Socket IP
        /// </summary>
        public IPEndPoint SocketIPEndPoint { get; set; }

        /// <summary>
        ///     TCP 监听
        /// </summary>
        public Socket SocketListener { get; set; }

        /// <summary>
        ///     客户端列表
        /// </summary>
        public List<HwClient> HwClientList { get; set; }

        /// <summary>
        ///     命令规则列表
        /// </summary>
        public List<HwCmdRuleDto> HwCmdRuleList { get; set; }

        #endregion

        #region 私有属性

        /// <summary>
        ///     链接信号量
        /// </summary>
        private readonly ManualResetEvent _ConnectedSignal;

        /// <summary>
        ///     主任务
        /// </summary>
        private Task MainTask;

        /// <summary>
        ///     标志位
        /// </summary>
        private bool IsRunning;

        /// <summary>
        ///     默认的编码形式
        /// </summary>
        private readonly Encoding DefaultEncoding;

        #endregion

        #region 构造函数与基础启动停止

        /// <summary>
        /// </summary>
        /// <param name="port"></param>
        /// <param name="service"></param>
        public SocketServer(int port, IRabbitMQFactoryService service)
        {
            MQService = service;

            // IP终端
            SocketIPEndPoint = new IPEndPoint(IPAddress.Any, port);


            // 信号量
            _ConnectedSignal = new ManualResetEvent(false);


            // 默认编码
            DefaultEncoding = Encoding.ASCII;


            // 客户端列表
            HwClientList = new List<HwClient>();


            // 是否正在运行
            IsRunning = false;


            // 主要工作线程
            MainTask = null;
        }


        /// <summary>
        ///     开始Socket服务
        /// </summary>
        public void Start()
        {
            //如果任务正在运行
            if (IsRunning)
            {
                Console.WriteLine(">>> Socket 服务已经运行...");
                return;
            }

            MainTask = new Task(() =>
            {
                Console.WriteLine(">>> 开始Socket 服务 ..." + HwCmdRuleList.Count);
                IsRunning = true;

                //监听器
                SocketListener = new Socket(SocketIPEndPoint.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
                SocketListener.Bind(SocketIPEndPoint);
                SocketListener.Listen(1000);

                while (IsRunning)
                {
                    // Set the event to nonsignaled state.
                    _ConnectedSignal.Reset();

                    if (IsRunning)
                    {
                        // Start an asynchronous socket to listen for connections.
                        Console.WriteLine(">>> 等待新连接...");
                        SocketListener.BeginAccept(AcceptCallback, SocketListener);
                    }
                    else
                    {
                        Console.WriteLine(">>> 结束Socket监听...");
                        return;
                    }

                    // Wait until a connection is made before continuing.
                    _ConnectedSignal.WaitOne();
                }
            });

            MainTask.Start();
        }


        /// <summary>
        ///     停止Socket服务
        /// </summary>
        public void Stop()
        {
            if (MainTask != null && !MainTask.IsCompleted)
            {
                Console.WriteLine(">>> 正在关闭 Soecket服务...");
                IsRunning = false;
                _ConnectedSignal.Set();

                Task.WaitAll(MainTask);

                SocketListener.Close();
                Console.WriteLine(">>> 关闭 Soecket服务...");
            }
        }

        #endregion


        #region Socket 处理方法

        /// <summary>
        ///     接收到请求的回调
        /// </summary>
        /// <param name="ar"></param>
        public void AcceptCallback(IAsyncResult ar)
        {
            if (!IsRunning) return;

            _ConnectedSignal.Set();

            // Get the socket that handles the client request.
            var listener = (Socket) ar.AsyncState;
            var handler = listener.EndAccept(ar);
            Console.WriteLine("<--新客户端连接\t{0}", DateTime.Now);

            //Create the state object.
            var state = new StateObject();
            state.WorkSocket = handler;

            //开始读取数据
            BeginRead(state);
        }

        /// <summary>
        ///     开始读取数据
        /// </summary>
        /// <param name="state"></param>
        private void BeginRead(StateObject state)
        {
            state.WorkSocket.BeginReceive(state.Buffer, 0, state.BufferSize, 0, ReadCallback, state);
        }

        /// <summary>
        ///     du
        /// </summary>
        /// <param name="ar"></param>
        private void ReadCallback(IAsyncResult ar)
        {
            var content = string.Empty;

            // Retrieve the state object and the handler socket
            // from the asynchronous state object.
            var state = (StateObject) ar.AsyncState;
            var handler = state.WorkSocket;

            try
            {
                // Read data from the client socket. 
                var bytesRead = handler.EndReceive(ar);

                if (bytesRead > 0)
                {
                    // There  might be more data, so store the data received so far.
                    state.SB.Append(DefaultEncoding.GetString(state.Buffer, 0, bytesRead));

                    // Check for end-of-file tag. If it is not there, read 
                    // more data.
                    content = state.SB.ToString();

                    if (content.IndexOf("]") > -1)
                    {
                        // All the data has been read from the client. Display it on the console.
                        Console.WriteLine("<<- 从Socket中读取 {0} 字节\t 数据: {1}", content.Length, content);

                        // 处理收到的信息
                        HandleReceivedContent(handler, content);
                    }
                    else //没有读完继续读取
                    {
                        BeginRead(state);
                    }
                }
            }
            catch (SocketException ex)
            {
                Console.WriteLine(ex.ToString());
            }
        }


        private void BeginSend(Socket handler, string data)
        {
            Console.WriteLine("Begin Sent {0} to client.", data);

            // Convert the string data to byte data using ASCII encoding.
            var byteData = DefaultEncoding.GetBytes(data);

            // Begin sending the data to the remote device.
            handler.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, handler);
        }


        private void SendCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object.
                var handler = (Socket) ar.AsyncState;

                // Complete sending the data to the remote device.
                var bytesSent = handler.EndSend(ar);
                Console.WriteLine("->>Sent {0} bytes to client.", bytesSent);

                // Re Read from the remote device
                Console.WriteLine("* Start to read from the remote device", bytesSent);
                var state = new StateObject();
                state.WorkSocket = handler;
                BeginRead(state);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }


        /// <summary>
        ///     处理接收到的内容
        /// </summary>
        /// <param name="content"></param>
        private void HandleReceivedContent(Socket handler, string content)
        {
            Console.WriteLine("处理接受数据:\t({0})", content);

            //通过内容创建命令
            var receivedCmd = content.BuildHwCmdFromString();

            //指令不合规重新开始读取
            if (false == receivedCmd.Ext_IsCmdValid)
            {
                Console.WriteLine("CMD is Not Valid ({0})", JsonConvert.SerializeObject(receivedCmd));
                var state = new StateObject();
                state.WorkSocket = handler;
                BeginRead(state);
            }

            //指令合规则根据要求应答
            else
            {
                //从列表中寻找客户端
                var client = new HwClient
                {
                    HwCode = receivedCmd.Format_HwCode,
                    HwVendor = receivedCmd.Format_HwVendor,
                    UpdateTime = DateTime.Now,
                    WorkSocket = handler
                };

                // 更新客户端
                client = HwClientList.CreateOrUpdateHwClient(client);

                //收到的命令加入到List //Todo
                client.HwCmdList.Add(receivedCmd);
                var json = JsonConvert.SerializeObject(receivedCmd);

                //收到的命令推送到消息队列
                MQService.SendMQ(receivedCmd.Format_HwCode, json);


                //如果LK则返回LK
                if (receivedCmd.Ext_CmdType == CmdTypeEnum.LK)
                {
                    var toSendCmd = HwCmdBuilder.LK(client.HwCode, client.HwVendor);
                    BeginSend(client.WorkSocket, toSendCmd.ToCmdString());

                    SendRuleCmd(client);

                    return;
                }

                //如果TKQ则返回TKQ
                if (receivedCmd.Ext_CmdType == CmdTypeEnum.TKQ)
                {
                    var toSendCmd = HwCmdBuilder.TKQ(client.HwCode, client.HwVendor);
                    BeginSend(client.WorkSocket, toSendCmd.ToCmdString());

                    return;
                }

                //如果TKQ则返回TKQ2
                if (receivedCmd.Ext_CmdType == CmdTypeEnum.TKQ2)
                {
                    var toSendCmd = HwCmdBuilder.TKQ2(client.HwCode, client.HwVendor);
                    BeginSend(client.WorkSocket, toSendCmd.ToCmdString());
                    return;
                }


                //接收到其他命令则再次读取
                Console.WriteLine("*命令处理完成，重新读取设备命令...");
                var state = new StateObject();
                state.WorkSocket = handler;
                BeginRead(state);
            }
        }


        /// <summary>
        ///     发送命令
        /// </summary>
        /// <param name="CmdToSend"></param>
        public void SendCommand(HwCmd CmdToSend)
        {
            if (CmdToSend == null) throw new ArgumentException("Content to send is null or empty!", "strHwCmd");

            //根据Code找出Client
            var client = HwClientList.FindHwClient(CmdToSend.Format_HwCode, CmdToSend.Format_HwVendor);

            if (client != null && client.WorkSocket.Connected)
            {
                Console.WriteLine("外发命令：" + CmdToSend.ToCmdString());
                BeginSend(client.WorkSocket, CmdToSend.ToCmdString());
            }
        }


        /// <summary>
        ///     发送规则命令
        /// </summary>
        private void SendRuleCmd(HwClient client)
        {
            // To-do 获取最新规则

            //处理规则数据
            foreach (var item in HwCmdRuleList)
                if (item.CmdType == 19)
                    if (null == item.HwCmdRuleLogList.FirstOrDefault(p => p.HwCode.Equals(client.HwCode)))
                    {
                        Console.WriteLine("发送规则命令：" + item.CmdType);
                        var timespan = item.CmdData.Split(",");
                        var toSendCmd = HwCmdBuilder.SILENCETIME(client.HwCode, timespan, client.HwVendor);
                        BeginSend(client.WorkSocket, toSendCmd.ToCmdString());

                        var log = new HwCmdRuleLogDto
                        {
                            CreateTime = DateTime.Now,
                            UpdateTime = DateTime.Now,
                            HwCode = client.HwCode,
                            Name = "SILENCETIME-" + client.HwCode,
                            Id = Guid.NewGuid(),
                            RuleId = item.Id,
                            Status = 1
                        };

                        item.HwCmdRuleLogList.Add(log);
                        _applicationDbContext.HwRuleReceivLog.Add(log);
                        _applicationDbContext.SaveChanges();
                    }
        }

        /// <summary>
        ///     获取客户端
        /// </summary>
        /// <returns></returns>
        public List<HwClient> GetHwClientList()
        {
            return HwClientList;
        }

        public List<HwCmdRuleDto> GetRuleList()
        {
            return HwCmdRuleList;
        }


        public void SetRuleList(List<HwCmdRuleDto> dtolist)
        {
            HwCmdRuleList = dtolist;
        }

        public void SetApplicationDbcontext(ApplicationDbContext dbContext)
        {
            _applicationDbContext = dbContext;
        }

        #endregion
    }
}