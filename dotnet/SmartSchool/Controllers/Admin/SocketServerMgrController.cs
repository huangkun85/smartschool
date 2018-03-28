using DataBaseAccess;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using SmartSchool.Services.SocketServer;

namespace SmartSchool.Controllers.Admin
{
    public class SocketServerMgrController : Controller
    {
        public const string Path = "/Views/Admin/SocketServerMgr/";
        protected readonly ApplicationDbContext _applicationDbContext;
        protected readonly ILogger _logger;
        protected readonly ISocketServer _socketServer;

        public SocketServerMgrController(
            ISocketServer socketServer,
            ApplicationDbContext dbContext,
            ILogger<HwCmdRulesController> logger)
        {
            _socketServer = socketServer;
            _applicationDbContext = dbContext;
            _logger = logger;
        }


        public IActionResult Index()
        {
            return View($"{Path}Index.cshtml");
        }

        public IActionResult Start()
        {
            //var dtolist = _applicationDbContext.HwCmdRule.ToList();
            //_socketServer.SetRuleList(dtolist);

            _socketServer.Start();
            ViewData["Message"] = "开始服务成功";
            return View($"{Path}Index.cshtml");
        }

        public IActionResult Stop()
        {
            _socketServer.Stop();
            ViewData["Message"] = "停止服务成功";
            return View($"{Path}Index.cshtml");
        }

        public IActionResult Update()
        {
            //var dtolist = _applicationDbContext.HwCmdRule.ToList();
            //_socketServer.SetRuleList(dtolist);

            ViewData["Message"] = "更新规则成功";
            return View($"{Path}Index.cshtml");
        }
    }
}