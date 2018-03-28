using System.ComponentModel.DataAnnotations.Schema;
using DataBaseAccess;
using DataBaseAccess.Dtos;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace SmartSchool.Controllers.Admin
{
    [NotMapped]
    [Route("[controller]/[action]")]
    public class BaseAdminController<T> : Controller
    {
        protected readonly ApplicationDbContext _applicationDbContext;
        protected readonly ILogger _logger;
        protected readonly RoleManager<ApplicationRole> _roleManager;
        protected readonly SignInManager<ApplicationUser> _signInManager;

        protected readonly UserManager<ApplicationUser> _userManager;


        public BaseAdminController(
            UserManager<ApplicationUser> userManager,
            RoleManager<ApplicationRole> roleManager,
            SignInManager<ApplicationUser> signInManager,
            ApplicationDbContext dbContext,
            ILogger<T> logger)
        {
            _userManager = userManager;
            _roleManager = roleManager;
            _signInManager = signInManager;
            _applicationDbContext = dbContext;
            _logger = logger;
        }
    }
}