using System;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;

namespace DataBaseAccess.Dtos
{
    /// <summary>
    /// 用户角色
    /// </summary>
    public class ApplicationRole : IdentityRole<Guid>
    {
        /// <summary>
        /// 描述
        /// </summary>
        [Display(Name = "描述")]
        public virtual string Description { get; set; }

    }
}
