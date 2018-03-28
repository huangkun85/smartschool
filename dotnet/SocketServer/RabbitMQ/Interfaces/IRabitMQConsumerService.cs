using System;
using System.Collections.Generic;
using System.Linq;
using System.Threading.Tasks;

namespace SocketServer.RabbitMQ.Interfaces
{
    public interface IRabitMqConsumerService : IRabbitMqService
    {

        void AddListener(String handlerName, IMessageQueueMessageReceivedListener msgReceivedhandler);

    }
}
