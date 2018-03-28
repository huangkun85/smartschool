using System;
using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace DataBaseAccess.Dtos
{
    /// <summary>
    ///  条目
    /// </summary>
    public class HwCmdRuleDto : BaseDto
    {

        public HwCmdRuleDto()
        {
            HwCmdRuleLogList = new List<HwCmdRuleLogDto>();
        }

        [Display(Name = "类型")]
        public int CmdType { get; set; }
        
        [Display(Name = "内容")]
        [StringLength(128)]
        public String CmdData { get; set; }
       
        [Display(Name = "启用时间")]
        public DateTime EnableTime { get; set; }
        
        [Display(Name = "失效时间")]
        public DateTime ExpireTime { get; set; }
        

        public ICollection<HwCmdRuleLogDto> HwCmdRuleLogList { get; set; }


    }
}
