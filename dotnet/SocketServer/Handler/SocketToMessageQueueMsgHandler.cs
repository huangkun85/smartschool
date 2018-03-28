using System;
using Newtonsoft.Json;
using SocketServer.RabbitMQ.Interfaces;
using SocketServer.SocketService;
using SocketServer.SocketService.Listener;
using SocketServer.SocketService.SocketModel;

namespace SocketServer.Handler
{

    /// <summary>
    /// socket 收到的数据转发到消息队列，通知APP
    /// </summary>
    public class SocketToMessageQueueMsgHandler : ISocketMessageReceivedListener
    {
        private readonly IRabbitMqFactoryService _rabbitMqFactoryService;


        public SocketToMessageQueueMsgHandler(IRabbitMqFactoryService rabbitMqFactoryService)
        {
            _rabbitMqFactoryService = rabbitMqFactoryService;
        }

        
        public void NewSocketMessage(HwCmd hwCmd, HwClient hwClient, ISocketServer socketServer)
        {
            String routeKey = String.Format("{0}.{1}", hwCmd.FormatHwVendor, hwCmd.FormatHwCode);
            String content = JsonConvert.SerializeObject(hwCmd);
            _rabbitMqFactoryService.SendTopicMessage(routeKey, content);

        }


    }
}