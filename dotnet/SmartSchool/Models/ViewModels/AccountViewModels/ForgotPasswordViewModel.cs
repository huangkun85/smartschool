using System.ComponentModel.DataAnnotations;

namespace SmartSchool.Models.ViewModels.AccountViewModels
{
    public class ForgotPasswordViewModel
    {
        [Required] [EmailAddress] public string Email { get; set; }
    }
}