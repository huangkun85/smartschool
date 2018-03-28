namespace SmartSchool.Models.RequestModels
{
    /// <summary>
    /// </summary>
    public class BootStrapTableRequestModel
    {
        public string Sort { get; set; }


        public string Order { get; set; }


        public int Offset { get; set; }


        public int Limit { get; set; }
    }
}