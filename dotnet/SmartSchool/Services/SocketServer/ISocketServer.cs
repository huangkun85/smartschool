using System.Collections.Generic;
using DataBaseAccess;
using DataBaseAccess.Dtos;
using SmartSchool.Services.SocketServer.SocketModel;

namespace SmartSchool.Services.SocketServer
{
    /// <summary>
    ///     Socket 服务器接口
    /// </summary>
    public interface ISocketServer
    {
        void SetApplicationDbcontext(ApplicationDbContext dbContext);


        /// <summary>
        /// </summary>
        /// <returns></returns>
        List<HwCmdRuleDto> GetRuleList();

        /// <summary>
        /// </summary>
        /// <param name="dtolist"></param>
        void SetRuleList(List<HwCmdRuleDto> dtolist);

        /// <summary>
        /// </summary>
        /// <returns></returns>
        List<HwClient> GetHwClientList();

        /// <summary>
        ///     开始 Socket 服务器
        /// </summary>
        void Start();


        /// <summary>
        ///     停止 Socket 服务器
        /// </summary>
        void Stop();


        void SendCommand(HwCmd cmdToSend);
    }
}