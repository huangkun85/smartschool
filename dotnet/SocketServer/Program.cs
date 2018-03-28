using System;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using DataBaseAccess;
using Microsoft.EntityFrameworkCore;
using Npgsql.EntityFrameworkCore.PostgreSQL;
using Newtonsoft.Json;
using SocketServer.Config;
using SocketServer.Handler;
using SocketServer.RabbitMQ.Interfaces;
using SocketServer.RabbitMQ.Services;
using SocketServer.SocketService;

namespace SocketServer
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            // 读取配置文件
            var strPath = Path.Combine(AppDomain.CurrentDomain.BaseDirectory, "appsetting.json");
            var r = File.OpenText(strPath);
            var appsetting = r.ReadToEndAsync().Result;
            var appSettingModel = JsonConvert.DeserializeObject<AppSetting>(appsetting);

            var version = Assembly.GetExecutingAssembly().GetName().Version.ToString();
            var strTitle = string.Format($"Socket Server v.{{0}}", version);

            Console.Title = strTitle;
            Console.OutputEncoding = Encoding.Default;


            Console.WriteLine("1. Start Producer MQ");

            IRabbitMqFactoryService rabbitMqFactoryService =
                new RabbitMqFactoryService(appSettingModel.RabbitMq.HostName, appSettingModel.RabbitMq.UserName,
                    appSettingModel.RabbitMq.Password,
                    appSettingModel.RabbitMq.VirtualHost, appSettingModel.RabbitMq.DirectQueueName,
                    appSettingModel.RabbitMq.TopicExchangeName);

            rabbitMqFactoryService.Start();

            Console.WriteLine("Producer MQ Started");


            Console.WriteLine("2. Start Comsumer MQ");

            IRabitMqConsumerService rabitMqConsumerService =
                new RabitMqConsumerService(appSettingModel.RabbitMq.HostName, appSettingModel.RabbitMq.UserName,
                    appSettingModel.RabbitMq.Password,
                    appSettingModel.RabbitMq.VirtualHost, appSettingModel.RabbitMq.DirectQueueName,
                    appSettingModel.RabbitMq.TopicExchangeName);

            rabitMqConsumerService.Start();

            Console.WriteLine("Comsumer MQ Started...");


            Console.WriteLine("3. Start Socket Service");
            ISocketServer socketServer = new SocketService.SocketServer(appSettingModel.SocketServerPort);
            socketServer.Start();
            Console.WriteLine("Socket Service Started...");


            Console.WriteLine("4. Start DataBase Access");
            var optionsBuilder = new DbContextOptionsBuilder<ApplicationDbContext>();
            optionsBuilder.UseNpgsql(appSettingModel.DbConnectionString);
            ApplicationDbContext applicationDbContext = new ApplicationDbContext(optionsBuilder.Options);
            Console.Write("\t" + applicationDbContext.HwProfile.Count());
            Console.WriteLine(" DataBase Access ready");



            var mqMsgrHandler = new MessageQueueToSocketHandler(socketServer);
            rabitMqConsumerService.AddListener("mainHandler", mqMsgrHandler);
            Console.WriteLine($"Register Main MQ Message Handler");


            var socketReceivedMsgHandler = new SocketToMessageQueueMsgHandler(rabbitMqFactoryService);
            socketServer.AddSocketMessageReceivedListener("Socket2Mq", socketReceivedMsgHandler);
            Console.WriteLine($"Register Main Socker Message Handler");


            // Soket服务器主控端
            CentralSocketServerControlHandler centralSocketServerControlHandler = new CentralSocketServerControlHandler(optionsBuilder.Options);
            socketServer.AddSocketMessageReceivedListener("CentralSocketServerControlHandler", centralSocketServerControlHandler);
            rabitMqConsumerService.AddListener("CentralSocketServerControlHandler", centralSocketServerControlHandler);

            Console.ReadLine();
        }
    }
}