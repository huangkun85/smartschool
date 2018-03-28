using System;
using System.Linq;
using System.Threading.Tasks;
using DataBaseAccess;
using DataBaseAccess.Dtos;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using SmartSchool.Models.RequestModels;

namespace SmartSchool.Controllers.Admin
{
    [Authorize]
    public class AppRoleMgrController : BaseAdminController<AppRoleMgrController>
    {
        public AppRoleMgrController(
            UserManager<ApplicationUser> userManager,
            RoleManager<ApplicationRole> roleManager,
            SignInManager<ApplicationUser> signInManager,
            ApplicationDbContext dbContext,
            ILogger<AppRoleMgrController> logger) : base(userManager, roleManager, signInManager, dbContext, logger)
        {
        }

        // GET: AppRoleMgr
        public ActionResult Index()
        {
            return View("/Views/Admin/RoleMgr_List.cshtml");
        }


        /// <summary>
        /// </summary>
        /// <param name="tableRequestModel"></param>
        /// <returns></returns>
        public ActionResult DataTableIndex([FromQuery] BootStrapTableRequestModel tableRequestModel)
        {
            var resRoles = _roleManager.Roles;
            var total = resRoles.Count();

            if (tableRequestModel.Sort != null)
                switch (tableRequestModel.Sort.ToLower())
                {
                    case "name":
                    {
                        if (tableRequestModel.Order.Equals("asc"))
                            resRoles.OrderBy(x => x.Name);
                        else
                            resRoles.OrderByDescending(x => x.Name);


                        break;
                    }
                    case "description":
                    {
                        if (tableRequestModel.Order.Equals("asc"))
                            resRoles.OrderBy(x => x.Description);
                        else
                            resRoles.OrderByDescending(x => x.Description);

                        break;
                    }

                    default:
                        break;
                }

            var resList = resRoles.Skip(tableRequestModel.Offset)
                .Take(tableRequestModel.Limit)
                .ToList();

            var res = new {total, rows = resList};
            return Json(res);
        }


        // GET: AppRoleMgr/Details/5
        public ActionResult Details(int id)
        {
            return View();
        }

        // GET: AppRoleMgr/Create
        public ActionResult Create()
        {
            return View("/Views/Admin/RoleMgr_Create.cshtml");
        }

        // POST: AppRoleMgr/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Create(ApplicationRole vm)
        {
            try
            {
                var result = await _roleManager.CreateAsync(vm);
                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("CreateRole", ex.Message);
                return View();
            }
        }

        // GET: AppRoleMgr/Edit/5
        public async Task<ActionResult> Edit(string id)
        {
            var role = await _roleManager.FindByIdAsync(id);
            return View("/Views/Admin/RoleMgr_Edit.cshtml", role);
        }

        // POST: AppRoleMgr/Edit/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Edit(string id, ApplicationRole applicationRole)
        {
            try
            {
                var role = await _roleManager.FindByIdAsync(applicationRole.Id.ToString());
                role.Name = applicationRole.Name;
                role.Description = applicationRole.Description;

                var res = await _roleManager.UpdateAsync(role);


                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("DeleteRole", ex.Message);
                return View("/Views/Admin/RoleMgr_Edit.cshtml");
            }
        }

        // GET: AppRoleMgr/Delete/5
        public async Task<ActionResult> Delete(string id)
        {
            var role = await _roleManager.FindByIdAsync(id);
            return View("/Views/Admin/RoleMgr_Delete.cshtml", role);
        }

        // POST: AppRoleMgr/Delete/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public async Task<ActionResult> Delete(string id, ApplicationRole applicationRole)
        {
            try
            {
                var role = await _roleManager.FindByNameAsync(applicationRole.Name);

                if (role != null)
                {
                    var res = await _roleManager.DeleteAsync(role);
                }

                return RedirectToAction(nameof(Index));
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("DeleteRole", ex.Message);
                return View("/Views/Admin/RoleMgr_Delete.cshtml");
            }
        }
    }
}