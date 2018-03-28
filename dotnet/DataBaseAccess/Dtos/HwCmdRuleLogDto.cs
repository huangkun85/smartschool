using System;
using System.ComponentModel.DataAnnotations;

namespace DataBaseAccess.Dtos
{
    public class HwCmdRuleLogDto : BaseDto
    {

        [Display(Name = "规则Id")]
        public Guid RuleId { get; set; }

        [Display(Name = "硬件编码")]
        [StringLength(16)]

        public String HwCode { get; set; }


        public HwCmdRuleDto HwCmdRuleEntity { get; set; }
    }
}
