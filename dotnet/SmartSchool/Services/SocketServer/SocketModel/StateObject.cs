using System.Net.Sockets;
using System.Text;

namespace SmartSchool.Services.SocketServer.SocketModel
{
    /// <summary>
    ///     状态对象
    /// </summary>
    public class StateObject
    {
        /// <summary>
        ///     构造函数
        /// </summary>
        /// <param name="bufferSize"></param>
        public StateObject(int bufferSize = 1024)
        {
            BufferSize = bufferSize;
            Buffer = new byte[BufferSize];
            SB = new StringBuilder();
        }

        /// <summary>
        ///     当前工作的Socket
        /// </summary>
        public Socket WorkSocket { get; set; }


        /// <summary>
        ///     Buffer的长度
        /// </summary>
        public int BufferSize { get; set; }


        /// <summary>
        ///     缓存数组
        /// </summary>
        public byte[] Buffer { get; set; }


        /// <summary>
        ///     接收到的命令
        /// </summary>
        public StringBuilder SB { get; set; }
    }
}