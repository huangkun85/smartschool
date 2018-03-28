using System.Threading.Tasks;

namespace SmartSchool.Services
{
    /// <summary>
    ///     发用邮件
    /// </summary>
    public interface IEmailSender
    {
        Task SendEmailAsync(string email, string subject, string message);
    }
}