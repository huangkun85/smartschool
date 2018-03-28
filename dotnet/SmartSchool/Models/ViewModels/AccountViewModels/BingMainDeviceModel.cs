using System.ComponentModel.DataAnnotations;

namespace SmartSchool.Models.ViewModels.AccountViewModels
{
    public class BingMainDeviceModel
    {
        [Required] [Phone] public string PhoneNumber { get; set; }

        [Required] public string HwCode { get; set; }

        [StringLength(16)] public string SosPhone { get; set; }
    }
}