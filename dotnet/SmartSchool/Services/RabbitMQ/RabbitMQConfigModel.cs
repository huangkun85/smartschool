namespace SmartSchool.Services.RabbitMQ
{
    public class RabbitMqConfigModel
    {
        public string HostName { get; set; }

        public string UserName { get; set; }

        public string Password { get; set; }

        public string VirtualHost { get; set; }

        public string DirectQueueName { get; set; }


        public string TopicExchangeName { get; set; }
    }
}