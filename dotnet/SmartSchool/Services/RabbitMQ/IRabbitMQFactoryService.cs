namespace SmartSchool.Services.RabbitMQ
{
    public interface IRabbitMQFactoryService
    {
        void SendMQ(string hwCode, string message, string hwVendor = "3G");
    }
}