using System.Threading.Tasks;

namespace SmartSchool.Services
{
    /// <summary>
    ///     发送SMS消息
    /// </summary>
    public class SMSSender : ISMSSender
    {
        public Task SendSMSAsync(string phoneNumber, string message)
        {
            return Task.CompletedTask;
        }
    }
}