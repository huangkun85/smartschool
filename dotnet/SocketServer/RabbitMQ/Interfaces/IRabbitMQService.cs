using System;
using System.Collections.Generic;
using System.Text;

namespace SocketServer.RabbitMQ.Interfaces
{
    public interface IRabbitMqService
    {    
        void Start();

        void Stop();

    }
}
