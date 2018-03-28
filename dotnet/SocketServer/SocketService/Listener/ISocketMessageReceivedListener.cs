using System;
using System.Collections.Generic;
using System.Text;
using SocketServer.SocketService.SocketModel;

namespace SocketServer.SocketService.Listener
{
    /// <summary>
    /// socket 收到消息
    /// </summary>
    public interface ISocketMessageReceivedListener
    {

        void NewSocketMessage(HwCmd hwCmd, HwClient hwClient, ISocketServer socketServer);

    }
}
