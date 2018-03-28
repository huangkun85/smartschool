using System;
using System.Collections.Generic;
using System.Text;

namespace SocketServer.Config
{
   public class RabbitMq
    {
        public string HostName { get; set; }
        public string UserName { get; set; }
        public string Password { get; set; }

        public string VirtualHost { get; set; }
        
        public String DirectQueueName { get; set; }
        
        public String TopicExchangeName { get; set; }
    }
}
