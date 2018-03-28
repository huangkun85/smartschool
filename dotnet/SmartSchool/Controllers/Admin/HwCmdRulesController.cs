using System;
using System.Linq;
using DataBaseAccess;
using DataBaseAccess.Dtos;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;
using SmartSchool.Models.RequestModels;

namespace SmartSchool.Controllers.Admin
{
    public class HwCmdRulesController : BaseAdminController<HwCmdRulesController>
    {
        private const string path = "/Views/Admin/HwCmdRules/";


        public HwCmdRulesController(
            UserManager<ApplicationUser> userManager,
            RoleManager<ApplicationRole> roleManager,
            SignInManager<ApplicationUser> signInManager,
            ApplicationDbContext dbContext,
            ILogger<HwCmdRulesController> logger) : base(userManager, roleManager, signInManager, dbContext, logger)
        {
        }

        // GET: HwRules
        public ActionResult Index([FromQuery] BootStrapTableRequestModel tableRequestModel)
        {
            if (tableRequestModel.Limit == 0 && tableRequestModel.Offset == 0) return View($"{path}Index.cshtml");

            // 带分页则返回Json


            var query = _applicationDbContext.HwCmdRule as IQueryable<HwCmdRuleDto>;
            var total = query.Count();

            if (tableRequestModel.Sort != null)
                switch (tableRequestModel.Sort)
                {
                    case "name":
                    {
                        if (tableRequestModel.Order.Equals("asc"))
                            query = query.OrderBy(x => x.Name);
                        else
                            query = query.OrderByDescending(x => x.Name);
                        break;
                    }

                    default:
                        break;
                }

            var resList = query.Skip(tableRequestModel.Offset)
                .Take(tableRequestModel.Limit)
                .ToList();

            var resJsonObj = new {total, rows = resList};
            return Json(resJsonObj);
        }

        // GET: HwRules/Details/5
        public ActionResult Details(int id)
        {
            return View();
        }

        // GET: HwRules/Create
        public ActionResult Create()
        {
            return View(path + "Create.cshtml");
        }

        // POST: HwRules/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create(HwCmdRuleDto vm)
        {
            try
            {
                var rule = _applicationDbContext.HwCmdRule.Where(t => t.Name.Equals(vm.Name)).FirstOrDefault();

                if (rule == null)
                {
                    vm.CreateTime = DateTime.Now;
                    vm.UpdateTime = vm.CreateTime;
                    _applicationDbContext.HwCmdRule.Add(vm);
                    _applicationDbContext.SaveChanges();
                    return RedirectToAction(nameof(Index));
                }

                ModelState.AddModelError("CreateHwRule", "规则名重复");
                return View(path + "Create.cshtml");
            }
            catch (Exception ex)
            {
                ModelState.AddModelError("CreateHwRule", ex.Message);
                return View(path + "Create.cshtml");
            }
        }

        // GET: HwRules/Edit/5
        public ActionResult Edit(int id)
        {
            return View();
        }

        // POST: HwRules/Edit/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Edit(int id, IFormCollection collection)
        {
            try
            {
                // TODO: Add update logic here

                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View();
            }
        }

        // GET: HwRules/Delete/5
        public ActionResult Delete(int id)
        {
            return View();
        }

        // POST: HwRules/Delete/5
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Delete(int id, IFormCollection collection)
        {
            try
            {
                // TODO: Add delete logic here

                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View();
            }
        }
    }
}