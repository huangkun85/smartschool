using System;
using System.Threading.Tasks;
using DataBaseAccess;
using DataBaseAccess.Dtos;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using SmartSchool.Extensions;
using SmartSchool.Models.ViewModels.AccountViewModels;
using SmartSchool.Services;

namespace SmartSchool.Controllers.API
{
    [Produces("application/json")]
    [Route("api/Account")]
    public class AccountApiController : Controller
    {
        private readonly ApplicationDbContext _dbContext;
        private readonly IEmailSender _emailSender;
        private readonly ILogger _logger;
        private readonly SignInManager<ApplicationUser> _signInManager;
        private readonly ISMSSender _smsSender;
        private readonly UserManager<ApplicationUser> _userManager;


        public AccountApiController(
            ApplicationDbContext dbContext,
            UserManager<ApplicationUser> userManager,
            SignInManager<ApplicationUser> signInManager,
            IEmailSender emailSender,
            ILogger<AccountApiController> logger
        )
        {
            _dbContext = dbContext;
            _userManager = userManager;
            _signInManager = signInManager;
            _emailSender = emailSender;
            _logger = logger;
        }


        [HttpPost]
        [Route("Register")]
        public async Task<IActionResult> RegisterAsync([FromBody] RegisterViewModel model)
        {
            var user = new ApplicationUser
            {
                UserName = model.PhoneNumber,
                PhoneNumber = model.PhoneNumber,
                Email = model.Email
            };
            var result = await _userManager.CreateAsync(user, model.Password);

            if (result.Succeeded)
            {
                _logger.LogInformation("开始创建用户");

                var code = await _userManager.GenerateEmailConfirmationTokenAsync(user);
                var callbackUrl = Url.EmailConfirmationLink(user.Id.ToString(), code, Request.Scheme);
                await _emailSender.SendEmailConfirmationAsync(model.PhoneNumber, callbackUrl);

                return Ok("创建用户成功");
            }

            return BadRequest("创建用户失败，请重新检查用户名或者联系管理员");
        }


        [HttpPost]
        [Route("Login")]
        public async Task<IActionResult> Login([FromBody] LoginViewModel model)
        {
            //检索用户
            var user = await _userManager.FindByNameAsync(model.PhoneNumber);
            if (null == user) return BadRequest("用户不存在");
            var result = await _signInManager.CheckPasswordSignInAsync(user, model.Password, false);

            if (result.Succeeded)
            {
                _logger.LogInformation(model.PhoneNumber + "用户登陆成功" + user.MainHwCode);

                // var phonelist = _dbContext.HwProfile.Where(p => p.PhoneNumber.Equals(model.PhoneNumber)).ToList();
                var res = new
                {
                    phone = user.PhoneNumber,
                    email = user.Email,
                    user.SosPhone,
                    user.MainHwCode,
                    loginTime = DateTime.Now
                };
                return Ok(res);
            }

            return BadRequest("用户登陆失败");
        }


        /// <summary>
        ///     绑定主要设备
        /// </summary>
        /// <param name="model"></param>
        /// <returns></returns>
        [HttpPost]
        [Route("BindMainDevice")]
        public async Task<IActionResult> BindMainDevice([FromBody] BingMainDeviceModel model)
        {
            //检索用户
            var user = await _userManager.FindByNameAsync(model.PhoneNumber);
            if (null == user) return BadRequest("用户不存在");

            user.MainHwCode = model.HwCode;
            user.SosPhone = model.SosPhone;
            var res = await _userManager.UpdateAsync(user);

            if (res.Succeeded) return Ok("用户设备绑定成功");

            return BadRequest("用户设备绑定失败");
        }
    }
}