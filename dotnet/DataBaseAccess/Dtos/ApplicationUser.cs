using System;
using System.ComponentModel.DataAnnotations;
using Microsoft.AspNetCore.Identity;

namespace DataBaseAccess.Dtos
{
    
    /// <summary>
    /// 用户名称
    /// </summary>
    public class ApplicationUser : IdentityUser<Guid>
    {

        /// <summary>
        /// 
        /// </summary>
        [Display(Name = "用户")]
        public virtual int UserType { get; set; }


        /// <summary>
        /// 描述
        /// </summary>
        [Display(Name = "描述")]
        public virtual string Description { get; set; }


        /// <summary>
        /// 主要设备编码
        /// </summary>
        [Display(Name = "主要设备编码")]
        public virtual string MainHwCode { get; set; }


        /// <summary>
        /// Sos联系号码
        /// </summary>
        [Display(Name = "Sos联系号码")]
        public virtual string SosPhone { get; set; }


    }
}
