using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using DataBaseAccess;
using DataBaseAccess.Dtos;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using SocketServer.Handler.Model;
using SocketServer.RabbitMQ.Interfaces;
using SocketServer.SocketService;
using SocketServer.SocketService.Listener;
using SocketServer.SocketService.SocketModel;

namespace SocketServer.Handler
{
    /// <summary>
    /// 服务器集中控制
    /// </summary>
    public class CentralSocketServerControlHandler : ISocketMessageReceivedListener, IMessageQueueMessageReceivedListener
    {
        //数据库连接字符串
        private readonly DbContextOptions<ApplicationDbContext> _dbContextOptionsoptions;
        private List<HwCmdRuleDto> _publishCmdRule;


        public CentralSocketServerControlHandler(DbContextOptions<ApplicationDbContext> dbContextOptionsoptions)
        {
            this._dbContextOptionsoptions = dbContextOptionsoptions;
            _publishCmdRule = new List<HwCmdRuleDto>();

        }


        /// <summary>
        /// 消息队列收到新的消息处理
        /// </summary>
        /// <param name="msg"></param>
        public void NewMessageQueueMsgReciviced(string msg)
        {
            MessageQueueDataModel model = JsonConvert.DeserializeObject<MessageQueueDataModel>(msg);

            //重新加载数据
            if (model.CmdType == 1000)
            {
                _publishCmdRule.Clear();

                using (ApplicationDbContext db = new ApplicationDbContext(_dbContextOptionsoptions))
                {
                    _publishCmdRule = db.HwCmdRule.ToList();
                }
                Console.WriteLine("Reload Rules from Db");
            }


        }

        public void NewSocketMessage(HwCmd hwCmd, HwClient hwClient, ISocketServer socketServer)
        {
            // 根据命令行内容回复信息

            throw new NotImplementedException();
        }
    }
}
