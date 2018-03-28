using System.ComponentModel.DataAnnotations;

namespace SmartSchool.Models.ViewModels.UserRoleMgrViewModels
{
    public class AppRoleViewModel
    {
        [Display(Name = "角色名称")]
        [MaxLength(127)]
        public string RoleName { get; set; }


        [Display(Name = "角色描述")]
        [MaxLength(256)]
        public string RoleDiscription { get; set; }
    }
}