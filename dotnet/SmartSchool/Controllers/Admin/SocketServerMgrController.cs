using DataBaseAccess;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using SmartSchool.Services.RabbitMQ;

namespace SmartSchool.Controllers.Admin
{

    /// <summary>
    /// 向消息队列发送控制消息
    /// </summary>
    public class SocketServerMgrController : Controller
    {
        public const string Path = "/Views/Admin/SocketServerMgr/";

        protected readonly ApplicationDbContext _applicationDbContext;
        private readonly IRabbitMqFactoryService _rabbitMqFactoryService;
        protected readonly ILogger _logger;


        public SocketServerMgrController(
       
            ApplicationDbContext dbContext,
            IRabbitMqFactoryService rabbitMqFactoryService,
            ILogger<HwCmdRulesController> logger)
        {
            _rabbitMqFactoryService = rabbitMqFactoryService;
            _applicationDbContext = dbContext;
            _logger = logger;
        }


        public IActionResult Index()
        {
            return View($"{Path}Index.cshtml");
        }

        public IActionResult Start()
        {
            ViewData["Message"] = "开始服务成功";
            return View($"{Path}Index.cshtml");
        }

        public IActionResult Stop()
        {

            ViewData["Message"] = "停止服务成功";
            return View($"{Path}Index.cshtml");
        }

        public IActionResult Update()
        {

            ViewData["Message"] = "更新规则成功";
            return View($"{Path}Index.cshtml");
        }
    }
}