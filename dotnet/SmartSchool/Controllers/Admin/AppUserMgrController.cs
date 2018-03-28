using DataBaseAccess;
using DataBaseAccess.Dtos;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Identity;
using Microsoft.AspNetCore.Mvc;
using Microsoft.Extensions.Logging;

namespace SmartSchool.Controllers.Admin
{
    public class AppUserMgrController : BaseAdminController<AppUserMgrController>
    {
        public AppUserMgrController(
            UserManager<ApplicationUser> userManager,
            RoleManager<ApplicationRole> roleManager,
            SignInManager<ApplicationUser> signInManager,
            ApplicationDbContext dbContext,
            ILogger<AppUserMgrController> logger) : base(userManager, roleManager, signInManager, dbContext, logger)
        {
        }

        // GET: AppUserMgr
        public ActionResult Index(string id)
        {
            return View();
        }

        // GET: AppUserMgr/Details/5
        public ActionResult Details(int id)
        {
            return View();
        }

        // GET: AppUserMgr/Create
        public ActionResult Create()
        {
            return View();
        }

        // POST: AppUserMgr/Create
        [HttpPost]
        [ValidateAntiForgeryToken]
        public ActionResult Create(IFormCollection collection)
        {
            try
            {
                // TODO: Add insert logic here

                return RedirectToAction(nameof(Index));
            }
            catch
            {
                return View();
            }
        }

        // GET: AppUserMgr/Edit/5
        public ActionResult Edit(int id)
        {
            return View();
        }

        // POST: AppUserMgr/Edit/5
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

        // GET: AppUserMgr/Delete/5
        public ActionResult Delete(int id)
        {
            return View();
        }

        // POST: AppUserMgr/Delete/5
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