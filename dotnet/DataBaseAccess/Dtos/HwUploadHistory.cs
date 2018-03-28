using System;
using System.ComponentModel.DataAnnotations;

namespace DataBaseAccess.Dtos
{
    /// <summary>
    /// 手表硬件上传的指令信息
    /// </summary>
    public class HwUploadHistory : BaseDto
    {



        [Display(Name = "硬件编码")]
        [StringLength(16)]
        public String HwCode { get; set; }

        [Display(Name = "电话号码")]
        [StringLength(16)]
        public String PhoneNumber { get; set; }

        [Display(Name = "短信通知号码")]
        [StringLength(16)]
        public String SmsMobile { get; set; }

        [Display(Name = "学生编号")]
        [StringLength(32)]
        public String StudentId { get; set; }

    }
}
