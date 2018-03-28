using System;
using Newtonsoft.Json;
using SocketServer.Handler.Model;
using SocketServer.RabbitMQ.Interfaces;
using SocketServer.SocketService;
using SocketServer.SocketService.SocketModel;

namespace SocketServer.Handler
{

    /// <summary>
    /// 消息队列中收到的手机指令的的内容下到Socket
    /// </summary>
    public class MessageQueueToSocketHandler : IMessageQueueMessageReceivedListener
    {
        private readonly ISocketServer _socketServer;


        public MessageQueueToSocketHandler(ISocketServer socketServer)
        {
            _socketServer = socketServer;
        }

        public void NewMessageQueueMsgReciviced(string msg)
        {
            Console.WriteLine("MessageQueueReceiveMsgHandler:" + msg);
         
            try
            {
                MessageQueueDataModel model = JsonConvert.DeserializeObject<MessageQueueDataModel>(msg);
                HwCmd cmd;
                int interval;

                switch (model.CmdType)
                {
                    case -1:
                        cmd = HwCmdBuilder.Factory(model.HwCode);
                        break;

                    case 0:
                        cmd = HwCmdBuilder.Reset(model.HwCode);
                        break;

                    case 2:
                        cmd = HwCmdBuilder.Verno(model.HwCode);
                        break;

                    case 3:
                        cmd = HwCmdBuilder.Find(model.HwCode);
                        break;

                    case 4:
                        cmd = HwCmdBuilder.Phb(model.HwCode, model.ContactList);
                        break;

                    case 5:
                        var status = model.Status ?? 0;
                        cmd = HwCmdBuilder.Pedo(model.HwCode, status);
                        break;

                    case 6:
                        cmd = HwCmdBuilder.Message(model.HwCode, model.Message);
                        break;

                    case 7:
                        interval = model.Interval ?? 10;
                        cmd = HwCmdBuilder.Upload(model.HwCode, interval);
                        break;

                    case 8:
                        cmd = HwCmdBuilder.Call(model.HwCode, model.Phone);
                        break;

                    case 9:
                        cmd = HwCmdBuilder.Sos1(model.HwCode, model.Phone);
                        break;

                    case 10:
                        cmd = HwCmdBuilder.Sos2(model.HwCode, model.Phone);
                        break;

                    case 11:
                        cmd = HwCmdBuilder.Sos3(model.HwCode, model.Phone);
                        break;

                    case 12:
                        cmd = HwCmdBuilder.Sos(model.HwCode, model.Phones);
                        break;

                    case 13:
                        interval = model.Interval ?? 100;
                        cmd = HwCmdBuilder.Hrtstart(model.HwCode, interval);
                        break;

                    case 15:
                        cmd = HwCmdBuilder.Cr(model.HwCode);
                        break;

                    case 16:
                        cmd = HwCmdBuilder.Walktime(model.HwCode, model.ArrrayTimeSpan);
                        break;

                    case 18:
                        cmd = HwCmdBuilder.Poweroff(model.HwCode);
                        break;


                    default:
                        Console.WriteLine("不支持的命令");
                        cmd = null;
                        break;
                }

                _socketServer.SendCommand(cmd);



            }

            catch (Exception e)
            {
                Console.WriteLine("Json Conver Fail:" + e.Message);
            }


        }

    }
}