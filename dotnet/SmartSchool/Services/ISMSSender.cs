using System.Threading.Tasks;

namespace SmartSchool.Services
{
    /// <summary>
    ///     短信接口
    /// </summary>
    public interface ISMSSender
    {
        Task SendSMSAsync(string phoneNumber, string message);
    }
}