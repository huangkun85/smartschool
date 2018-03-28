using System.ComponentModel.DataAnnotations;

namespace SmartSchool.Models.ViewModels.AccountViewModels
{
    public class RegisterViewModel
    {
        [Required]
        [Phone]
        [Display(Name = "手机号码：用于登陆")]
        public string PhoneNumber { get; set; }


        [Required]
        [EmailAddress]
        [Display(Name = "邮箱：通过电子邮箱获取验证码")]
        public string Email { get; set; }


        [Required]
        [StringLength(100, ErrorMessage = "密码{0}至少需要{2}位最多{1}位字符长度.", MinimumLength = 6)]
        [DataType(DataType.Password)]
        [Display(Name = "密码")]
        public string Password { get; set; }


        [DataType(DataType.Password)]
        [Display(Name = "确认密码")]
        [Compare("Password", ErrorMessage = "输入的密码不匹配")]
        public string ConfirmPassword { get; set; }
    }
}