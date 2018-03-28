using System;
using System.IO;
using System.Linq;
using System.Reflection;
using DataBaseAccess;
using Microsoft.EntityFrameworkCore;
using Newtonsoft.Json;
using SocketServer.Config;
using SocketServer.Handler;
using SocketServer.RabbitMQ.Interfaces;
using SocketServer.RabbitMQ.Services.Comsumer;
using SocketServer.RabbitMQ.Services.Factory;
using SocketServer.SocketService;

namespace SocketServer
{
    public class Program
    {
        private static void Main(string[] args)
        {
            Console.WriteLine("Start to Load Config File");
            // 读取配置文件
            var strPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "appsetting.json");
            var r = File.OpenText(strPath);
            var appsetting = r.ReadToEndAsync().Result;
            var appSettingModel = JsonConvert.DeserializeObject<AppSetting>(appsetting);
            var version = Assembly.GetExecutingAssembly().GetName().Version.ToString();
            var strTitle = string.Format("SocketServer[{0}]v{1}", appSettingModel.SocketServerId, version);
            Console.Title = strTitle;
            Console.WriteLine("Load Config Finished");
            Console.WriteLine();


            Console.WriteLine("Start to Init MQ Factory [Topic->Apps]");
            IRabbitMqFactoryService rabbitMqFactoryService =
                new MqFactoryService(
                    appSettingModel.RabbitMqSetting.HostName,
                    appSettingModel.RabbitMqSetting.UserName,
                    appSettingModel.RabbitMqSetting.Password,
                    appSettingModel.RabbitMqSetting.VirtualHost,
                    appSettingModel.RabbitMqSetting.SocketServerToAppExchangeName
                );
            rabbitMqFactoryService.Start();

            Console.WriteLine("Load MQ Factory Finished");
            Console.WriteLine();


            Console.WriteLine("Start to Init MQ Comsumer [Direct<-Apps]");
            IMqDirectConsumerService mqDirectConsumerService =
                new MqDirectConsumerService(
                    appSettingModel.RabbitMqSetting.HostName,
                    appSettingModel.RabbitMqSetting.UserName,
                    appSettingModel.RabbitMqSetting.Password,
                    appSettingModel.RabbitMqSetting.VirtualHost,
                    appSettingModel.RabbitMqSetting.AppToSocketServerExchangeName,
                    appSettingModel.RabbitMqSetting.AppToSocketServerQueueName
                );
            mqDirectConsumerService.Start();

            Console.WriteLine("Load MQ Comsumer[Direct] Finished");
            Console.WriteLine();



            Console.WriteLine("Start to Init MQ Comsumer [Direct<-Apps]");

            IMqTopicConsumerService mqTopicConsumerService = null;


            mqTopicConsumerService.Start();

            Console.WriteLine("Load MQ Comsumer[Direct] Finished");
            Console.WriteLine();


            Console.WriteLine("3. Start Socket Service");
            ISocketServer socketServer = new SocketService.SocketServer(appSettingModel.SocketServerPort);
            socketServer.Start();
            Console.WriteLine("Socket Service Started...");


            Console.WriteLine("4. Start DataBase Access");
            var optionsBuilder = new DbContextOptionsBuilder<ApplicationDbContext>();
            optionsBuilder.UseNpgsql(appSettingModel.DbConnectionString);
            var applicationDbContext = new ApplicationDbContext(optionsBuilder.Options);
            Console.Write("\t" + applicationDbContext.HwProfile.Count());
            Console.WriteLine(" DataBase Access ready");


            var mqMsgrHandler = new MessageQueueToSocketHandler(socketServer);
            mqDirectConsumerService.AddListener("mainHandler", mqMsgrHandler);
            Console.WriteLine($"Register Main MQ Message Handler");


            var socketReceivedMsgHandler = new SocketToMessageQueueMsgHandler(rabbitMqFactoryService);
            socketServer.AddSocketMessageReceivedListener("Socket2Mq", socketReceivedMsgHandler);
            Console.WriteLine($"Register Main Socker Message Handler");


            // Soket服务器主控端
            var centralSocketServerControlHandler = new CentralSocketServerControlHandler(optionsBuilder.Options);
            socketServer.AddSocketMessageReceivedListener("CentralSocketServerControlHandler",
                centralSocketServerControlHandler);
            mqDirectConsumerService.AddListener("CentralSocketServerControlHandler",
                centralSocketServerControlHandler);

            Console.ReadLine();
        }
    }
}