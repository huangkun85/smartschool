using System;

namespace SmartSchool.Services.SocketServer.SocketModel
{
    /// <summary>
    ///     联系方式
    ///     号码不超过20个ascii字符，姓名不超过10个Unicode字符
    /// </summary>
    public class HwContactModel
    {
        /// <summary>
        ///     构造函数
        /// </summary>
        /// <param name="phone"></param>
        /// <param name="name"></param>
        public HwContactModel(string phone, string name)
        {
            Phone = phone;
            Name = name;
        }

        /// <summary>
        ///     电话号码
        /// </summary>
        public string Phone { get; }

        /// <summary>
        ///     联系名
        /// </summary>
        public string Name { get; }

        public override string ToString()
        {
            return string.Format("{0},{1}", Phone, Name);
        }

        /// <summary>
        ///     生成参数内容
        ///     列表号码:  Ascii字符
        ///     名字:      Unicode编码
        /// </summary>
        /// <returns></returns>
        public string toCmdParaContent()
        {
            var strUnicodeName = Name.ToUnicode();

            if (Phone.Length > 20) throw new ArgumentException("电话号码长度超出", "Phone");

            //if (strUnicodeName.Length > 10)
            //{
            //    throw new ArgumentException("联系名长度超出", "Name");
            //}

            return string.Format("{0},{1}", Phone, strUnicodeName);
        }
    }
}