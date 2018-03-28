using System;
using System.Text;
using Newtonsoft.Json;
using RabbitMQ.Client;
using RabbitMQ.Client.Events;
using SmartSchool.Services.RabbitMQ.Model;
using SmartSchool.Services.SocketServer;
using SmartSchool.Services.SocketServer.SocketModel;

namespace SmartSchool.Services.RabbitMQ
{
    /// <summary>
    ///     订阅总渠道上的信息
    /// </summary>
    public class RabitMQConsumerService : IRabitMQConsumerService
    {
        private readonly string exchangeName = "any";
        private readonly string routingKey = "any";


        /// <summary>
        ///     构造函数
        /// </summary>
        /// <param name="config"></param>
        /// <param name="socketServer"></param>
        public RabitMQConsumerService(RabbitMQConfigModel config, ISocketServer socketServer)
        {
            _SocketServer = socketServer;

            _MQ_ConnectionFactory = new ConnectionFactory
            {
                HostName = config.HostName,
                UserName = config.UserName,
                Password = config.Password,
                VirtualHost = config.VirtualHost
            };

            if (_MQ_Connection == null || _MQ_Channel == null || !_MQ_Connection.IsOpen || !_MQ_Channel.IsOpen)
            {
                _MQ_Connection = _MQ_ConnectionFactory.CreateConnection();
                _MQ_Channel = _MQ_Connection.CreateModel();
                _MQ_Channel.ExchangeDeclare(exchangeName, ExchangeType.Direct);
                _MQ_Channel.QueueDeclare(config.DirectQueueName, false, false, false, null);
                _MQ_Channel.QueueBind(config.DirectQueueName, exchangeName, routingKey, null);
            }

            _MQ_Channel.QueueDeclare(config.DirectQueueName, false, false, false, null);
            _MQ_Channel.BasicQos(0, 1000, false);

            var consumer = new EventingBasicConsumer(_MQ_Channel);

            consumer.Received += (ch, ea) =>
            {
                var message = Encoding.UTF8.GetString(ea.Body);
                Console.WriteLine("<<-收到消息队列-<({0})", message);

                RabbitMQMsgModel model = null;
                HwCmd cmd = null;

                try
                {
                    model = JsonConvert.DeserializeObject<RabbitMQMsgModel>(message);
                    cmd = HandleMsg(model); //处理收到的消息

                    Console.WriteLine(cmd.ToCmdString());
                    _SocketServer.SendCommand(cmd);
                }
                catch (Exception ex)
                {
                    Console.WriteLine("json 格式错误或不被支持的命令:" + ex.Message);
                }
            };

            _MQ_Channel.BasicConsume(config.DirectQueueName, true, consumer);
        }


        private ConnectionFactory _MQ_ConnectionFactory { get; }

        private IConnection _MQ_Connection { get; }

        private IModel _MQ_Channel { get; }

        private RabbitMQConfigModel _MQ_Config { get; set; }

        private ISocketServer _SocketServer { get; }


        /// <summary>
        ///     处理MQ信息
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        private HwCmd HandleMsg(RabbitMQMsgModel model)
        {
            var interval = -1;
            var status = -1;
            HwCmd cmd = null;

            switch (model.CmdType)
            {
                case -1:
                    cmd = HwCmdBuilder.FACTORY(model.HwCode);
                    break;

                case 0:
                    cmd = HwCmdBuilder.RESET(model.HwCode);
                    break;


                case 2:
                    cmd = HwCmdBuilder.VERNO(model.HwCode);
                    break;

                case 3:
                    cmd = HwCmdBuilder.FIND(model.HwCode);
                    break;

                case 4:
                    cmd = HwCmdBuilder.PHB(model.HwCode, model.ContactList);
                    break;

                case 5:
                    status = model.Status == null ? 0 : (int) model.Status;
                    cmd = HwCmdBuilder.PEDO(model.HwCode, status);
                    break;

                case 6:
                    cmd = HwCmdBuilder.MESSAGE(model.HwCode, model.Message);
                    break;

                case 7:
                    interval = model.Interval == null ? 10 : (int) model.Interval;
                    cmd = HwCmdBuilder.UPLOAD(model.HwCode, interval);
                    break;

                case 8:
                    cmd = HwCmdBuilder.CALL(model.HwCode, model.Phone);
                    break;

                case 9:
                    cmd = HwCmdBuilder.SOS1(model.HwCode, model.Phone);
                    break;

                case 10:
                    cmd = HwCmdBuilder.SOS2(model.HwCode, model.Phone);
                    break;

                case 11:
                    cmd = HwCmdBuilder.SOS3(model.HwCode, model.Phone);
                    break;

                case 12:
                    cmd = HwCmdBuilder.SOS(model.HwCode, model.Phones);
                    break;

                case 13:
                    interval = model.Interval == null ? 100 : (int) model.Interval;
                    cmd = HwCmdBuilder.HRTSTART(model.HwCode, interval);
                    break;

                case 15:
                    cmd = HwCmdBuilder.CR(model.HwCode);
                    break;

                case 16:
                    cmd = HwCmdBuilder.WALKTIME(model.HwCode, model.ArrrayTimeSpan);
                    break;

                case 18:
                    cmd = HwCmdBuilder.POWEROFF(model.HwCode);
                    break;


                default:
                    throw new ArgumentException("不支持的命令", "CmdType");
            }


            return cmd;
        }
    }
}