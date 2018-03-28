using System;
using System.Collections.Generic;
using System.Text;

namespace SocketServer.RabbitMQ.Interfaces
{
    public interface IMessageQueueMessageReceivedListener
    {
        void NewMessageQueueMsgReciviced(String msg);


    }
}
