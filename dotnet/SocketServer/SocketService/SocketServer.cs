using System;
using System.Collections.Generic;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using Newtonsoft.Json;
using SocketServer.SocketService.Listener;
using SocketServer.SocketService.SocketModel;

namespace SocketServer.SocketService
{
    /// <summary>
    ///     Socket 服务器参考引用
    ///     https://msdn.microsoft.com/zh-cn/library/fx6588te(v=vs.110).aspx
    /// </summary>
    public class SocketServer : ISocketServer
    {
        /// <summary>
        ///     链接信号量
        /// </summary>
        private readonly ManualResetEvent _connectedSignal;

        /// <summary>
        ///     默认的编码形式
        /// </summary>
        private readonly Encoding _defaultEncoding;

        /// <summary>
        ///     客户端列表
        /// </summary>
        private readonly List<HwClient> _hwClientList;

        /// <summary>
        ///     Socket IP
        /// </summary>
        private readonly IPEndPoint _socketIpEndPoint;

        /// <summary>
        ///     标志位
        /// </summary>
        private bool _isRunning;

        /// <summary>
        ///     主任务
        /// </summary>
        private Task _mainTask;

        /// <summary>
        ///     TCP 监听
        /// </summary>
        private Socket _socketListener;

        /// <summary>
        ///     与外部交互的接口 Socket新消息到达通知
        /// </summary>
        private readonly Dictionary<string, ISocketMessageReceivedListener> _socketMessageReceivedListeners;


        #region 构造函数与基础启动停止

        /// <summary>
        ///     构造函数
        /// </summary>
        /// <param name="port"></param>
        public SocketServer(int port)
        {
            _socketMessageReceivedListeners = new Dictionary<string, ISocketMessageReceivedListener>();

            // IP终端
            _socketIpEndPoint = new IPEndPoint(IPAddress.Any, port);

            // 信号量
            _connectedSignal = new ManualResetEvent(false);

            // 默认编码
            _defaultEncoding = Encoding.ASCII;

            // 客户端列表
            _hwClientList = new List<HwClient>();

            // 是否正在运行
            _isRunning = false;

            // 主要工作线程
            _mainTask = null;
        }


        /// <summary>
        ///     开始Socket服务
        /// </summary>
        public void Start()
        {
            //如果任务正在运行
            if (_isRunning)
            {
                Console.WriteLine($">>> Socket service is already running...");
                return;
            }

            _mainTask = new Task(() =>
            {
                Console.WriteLine($">>> Starting socket service...");
                _isRunning = true;

                //监听器
                _socketListener = new Socket(_socketIpEndPoint.AddressFamily, SocketType.Stream, ProtocolType.Tcp);
                _socketListener.Bind(_socketIpEndPoint);
                _socketListener.Listen(1000);
                Console.WriteLine("Socket Server Listenning on port[{0}]", _socketIpEndPoint.Port);

                while (_isRunning)
                {
                    // Set the event to nonsignaled state.
                    _connectedSignal.Reset();

                    if (_isRunning)
                    {
                        // Start an asynchronous socket to listen for connections.
                        Console.WriteLine($" >>> Waiting for new Connection...");
                        _socketListener.BeginAccept(AcceptCallback, _socketListener);
                    }
                    else
                    {
                        Console.WriteLine($" >>> Stop Socket Listening...");
                        return;
                    }

                    // Wait until a connection is made before continuing.
                    _connectedSignal.WaitOne();
                }
            });

            _mainTask.Start();
        }


        /// <summary>
        ///     停止Socket服务
        /// </summary>
        public void Stop()
        {
            if (_mainTask != null && !_mainTask.IsCompleted)
            {
                Console.WriteLine($">>> Stoppping Soecket Service...");
                _isRunning = false;
                _connectedSignal.Set();

                Task.WaitAll(_mainTask);

                _socketListener.Close();
                Console.WriteLine($">>> Soecket Service Stopped");
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
            if (_isRunning)
            {
                _connectedSignal.Set();

                // Get the socket that handles the client request.
                var listener = (Socket)ar.AsyncState;
                var handler = listener.EndAccept(ar);

                Console.WriteLine("<-- Accept New Socket Connection\t{0}", DateTime.Now);

                //Create the state object.
                var state = new StateObject
                {
                    WorkSocket = handler
                };

                //开始读取数据
                BeginRead(state);
            }
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
        ///     读取数据回掉接口
        /// </summary>
        /// <param name="ar"></param>
        private void ReadCallback(IAsyncResult ar)
        {
            // Retrieve the state object and the handler socket
            // from the asynchronous state object.
            var state = (StateObject)ar.AsyncState;
            var handler = state.WorkSocket;

            try
            {
                // Read data from the client socket. 
                var bytesRead = handler.EndReceive(ar);

                if (bytesRead > 0)
                {
                    // There  might be more data, so store the data received so far.
                    state.Sb.Append(_defaultEncoding.GetString(state.Buffer, 0, bytesRead));

                    // Check for end-of-file tag. If it is not there, read 
                    // more data.
                    var content = state.Sb.ToString();

                    if (content.IndexOf("]", StringComparison.Ordinal) > -1)
                    {
                        // All the data has been read from the client. Display it on the console.
                        Console.WriteLine("<<- Read {0} Bytes from Socket\t Data: {1}", content.Length, content);

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
            var byteData = _defaultEncoding.GetBytes(data);

            // Begin sending the data to the remote device.
            handler.BeginSend(byteData, 0, byteData.Length, 0, SendCallback, handler);
        }


        private void SendCallback(IAsyncResult ar)
        {
            try
            {
                // Retrieve the socket from the state object.
                var handler = (Socket)ar.AsyncState;

                // Complete sending the data to the remote device.
                var bytesSent = handler.EndSend(ar);
                Console.WriteLine("->>Sent {0} bytes to client.", bytesSent);

                // Re Read from the remote device
                Console.WriteLine("* Start to read from the remote device");
                var state = new StateObject
                {
                    WorkSocket = handler
                };

                BeginRead(state);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.ToString());
            }
        }

        /// <summary>
        /// </summary>
        /// <param name="handler"></param>
        /// <param name="content"></param>
        private void HandleReceivedContent(Socket handler, string content)
        {
            Console.WriteLine("处理接受数据:\t({0})", content);

            //通过内容创建命令
            var receivedCmd = content.BuildHwCmdFromString();

            //指令不合规重新开始读取
            if (false == receivedCmd.ExtIsCmdValid)
            {
                Console.WriteLine("CMD is Not Valid ({0})", JsonConvert.SerializeObject(receivedCmd));
                var state = new StateObject
                {
                    WorkSocket = handler
                };
                BeginRead(state);
            }

            //指令合规则根据要求应答
            else
            {
                //从列表中寻找客户端
                var client = new HwClient
                {
                    HwCode = receivedCmd.FormatHwCode,
                    HwVendor = receivedCmd.FormatHwVendor,
                    UpdateTime = DateTime.Now,
                    WorkSocket = handler
                };

                // 更新客户端
                client = _hwClientList.CreateOrUpdateHwClient(client);

                //收到的命令加入到List 
                client.HwCmdList.Add(receivedCmd);


                //如果LK则返回LK
                if (receivedCmd.ExtCmdType == HwCmdTypeEnum.Lk)
                {
                    var toSendCmd = HwCmdBuilder.Lk(client.HwCode, client.HwVendor);
                    BeginSend(client.WorkSocket, toSendCmd.ToCmdString());

                    return;
                }

                //如果TKQ则返回TKQ
                if (receivedCmd.ExtCmdType == HwCmdTypeEnum.Tkq)
                {
                    var toSendCmd = HwCmdBuilder.Tkq(client.HwCode, client.HwVendor);
                    BeginSend(client.WorkSocket, toSendCmd.ToCmdString());

                    return;
                }

                //如果TKQ则返回TKQ2
                if (receivedCmd.ExtCmdType == HwCmdTypeEnum.Tkq2)
                {
                    var toSendCmd = HwCmdBuilder.Tkq2(client.HwCode, client.HwVendor);
                    BeginSend(client.WorkSocket, toSendCmd.ToCmdString());
                    return;
                }

                //收到命令后的处理

                foreach (var kv in _socketMessageReceivedListeners)
                {
                    kv.Value.NewSocketMessage(receivedCmd, client,this);
                }


                //接收到其他命令则再次读取
                Console.WriteLine("*命令处理完成，重新读取设备命令...");
                var state = new StateObject
                {
                    WorkSocket = handler
                };

                BeginRead(state);
            }
        }


        /// <summary>
        ///     发送命令
        /// </summary>
        /// <param name="cmdToSend"></param>
        public void SendCommand(HwCmd cmdToSend)
        {
            if (cmdToSend == null)
            {
                Console.WriteLine("CMD Content to send is null or empty!");
                return;
            }

            //根据Code找出Client
            var client = _hwClientList.FindHwClient(cmdToSend.FormatHwCode, cmdToSend.FormatHwVendor);

            if (client != null && client.WorkSocket.Connected)
            {
                Console.WriteLine($"Socket Send CMD:{cmdToSend.ToCmdString()}");
                BeginSend(client.WorkSocket, cmdToSend.ToCmdString());
            }
        }


        /// <summary>
        /// 发送命令
        /// </summary>
        /// <param name="socket">活动的Socket</param>
        /// <param name="cmdPlainText">平面text</param>
        public void SendCommand(Socket socket, string cmdPlainText)
        {
            BeginSend(socket, cmdPlainText);
        }


        public void AddSocketMessageReceivedListener(string listenerName, ISocketMessageReceivedListener listener)
        {
            _socketMessageReceivedListeners.Add(listenerName, listener);
        }

        public void RemoveSocketMessageReceivedListener(string listenerName)
        {
            _socketMessageReceivedListeners.Remove(listenerName);
        }


        public ISocketMessageReceivedListener GetSocketMessageReceivedListener(string listenerName)
        {
            var res = _socketMessageReceivedListeners.TryGetValue(listenerName, out var socketMessageReceivedListener);

            if (res) return socketMessageReceivedListener;

            return null;
        }


        public bool IsServerRunning()
        {
            return _isRunning;
        }


        #endregion
    }
}