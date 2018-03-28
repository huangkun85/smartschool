using System;
using System.ComponentModel.DataAnnotations;

namespace DataBaseAccess.Dtos
{
    public class BaseDto
    {
        [Display(Name = "Id")]
        [Key]
        public Guid Id { get; set; }
        

        [Display(Name = "名字")]
        [StringLength(32)]
        public String Name { get; set; }

        
        [Display(Name = "状态")]
        public int Status { get; set; }
        

        [Display(Name = "创建时间")]
        public DateTime CreateTime { get; set; }

        [Display(Name = "更新时间")]
        public DateTime UpdateTime { get; set; }
    }
}
