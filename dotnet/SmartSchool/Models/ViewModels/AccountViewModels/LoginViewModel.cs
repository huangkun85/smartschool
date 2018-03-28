using System.ComponentModel.DataAnnotations;

namespace SmartSchool.Models.ViewModels.AccountViewModels
{
    public class LoginViewModel
    {
        [Required]
        [Phone]
        [Display(Name = "手机号码")]
        public string PhoneNumber { get; set; }

        [Required]
        [DataType(DataType.Password)]
        [Display(Name = "登录密码")]
        public string Password { get; set; }

        [Display(Name = "记住我?")] public bool RememberMe { get; set; }
    }
}