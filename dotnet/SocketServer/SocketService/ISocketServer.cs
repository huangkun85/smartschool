using System.Net.Sockets;
using SocketServer.SocketService.Listener;
using SocketServer.SocketService.SocketModel;

namespace SocketServer.SocketService
{
    /// <summary>
    ///     Socket 服务器接口
    /// </summary>
    public interface ISocketServer
    {
        /// <summary>
        ///     开始 Socket 服务器
        /// </summary>
        void Start();

        /// <summary>
        ///     停止 Socket 服务器
        /// </summary>
        void Stop();

        /// <summary>
        ///     获取Socket 服务器是否在运行
        /// </summary>
        /// <returns></returns>
        bool IsServerRunning();

        /// <summary>
        ///     发送Socket命令
        /// </summary>
        /// <param name="cmdToSend"></param>
        void SendCommand(HwCmd cmdToSend);

        void SendCommand(Socket socket, string cmdPlainText);

        /// <summary>
        ///     增加一个Socket监听器
        /// </summary>
        /// <param name="listenerName"></param>
        /// <param name="listener"></param>
        void AddSocketMessageReceivedListener(string listenerName, ISocketMessageReceivedListener listener);

        /// <summary>
        ///     删除一个Socket监听器
        /// </summary>
        /// <param name="listenerName"></param>
        void RemoveSocketMessageReceivedListener(string listenerName);

        /// <summary>
        ///     获取一个Socket监听器
        /// </summary>
        /// <param name="listenerName"></param>
        /// <returns></returns>
        ISocketMessageReceivedListener GetSocketMessageReceivedListener(string listenerName);
    }
}