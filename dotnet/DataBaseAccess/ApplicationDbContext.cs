using System;
using DataBaseAccess.Dtos;
using Microsoft.AspNetCore.Identity.EntityFrameworkCore;
using Microsoft.EntityFrameworkCore;

namespace DataBaseAccess
{
    public class ApplicationDbContext : IdentityDbContext<ApplicationUser, ApplicationRole, Guid>
    {
        public ApplicationDbContext(DbContextOptions<ApplicationDbContext> options)
            : base(options)
        {
        }


        protected override void OnModelCreating(ModelBuilder builder)
        {
            base.OnModelCreating(builder);

            builder.Entity<HwCmdRuleLogDto>().HasOne(s => s.HwCmdRuleEntity).WithMany(s => s.HwCmdRuleLogList).HasForeignKey(s => s.RuleId);


        }

        /// <summary>
        /// 硬件编码
        /// </summary>
        public virtual DbSet<HwUploadHistory> HwProfile { get; set; }

        /// <summary>
        /// 指令集合
        /// </summary>
        public virtual DbSet<HwCmdRuleDto> HwCmdRule { get; set; }

        /// <summary>
        /// HwRuleReceivLogDto
        /// </summary>
        public virtual DbSet<HwCmdRuleLogDto> HwRuleReceivLog { get; set; }


    }
}
