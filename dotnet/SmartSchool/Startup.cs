using System;
using DataBaseAccess;
using DataBaseAccess.Dtos;
using Microsoft.AspNetCore.Builder;
using Microsoft.AspNetCore.Hosting;
using Microsoft.AspNetCore.Identity;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using Microsoft.Extensions.DependencyInjection;
using SmartSchool.Services;

namespace SmartSchool
{
    public class Startup
    {
        public Startup(IConfiguration configuration)
        {
            Configuration = configuration;
        }

        public IConfiguration Configuration { get; }

        // This method gets called by the runtime. Use this method to add services to the container.
        public void ConfigureServices(IServiceCollection services)
        {
            services.AddDbContext<ApplicationDbContext>(options =>
                options.UseNpgsql(Configuration.GetConnectionString("DefaultConnection")));

            services.AddIdentity<ApplicationUser, ApplicationRole>(options =>
                {
                    // Password settings
                    options.Password.RequireDigit = true;
                    options.Password.RequiredLength = 6;
                    options.Password.RequireNonAlphanumeric = false;
                    options.Password.RequireUppercase = false;
                    options.Password.RequireLowercase = false;
                    options.Password.RequiredUniqueChars = 2;

                    // Lockout settings
                    options.Lockout.DefaultLockoutTimeSpan = TimeSpan.FromMinutes(5);
                    options.Lockout.MaxFailedAccessAttempts = 5;
                    options.Lockout.AllowedForNewUsers = true;

                    // Signin settings
                    options.SignIn.RequireConfirmedEmail = false;
                    options.SignIn.RequireConfirmedPhoneNumber = false;
                    // User settings
                    options.User.RequireUniqueEmail = false;
                })
                .AddEntityFrameworkStores<ApplicationDbContext>()
                .AddDefaultTokenProviders();

            // Add application services.
            services.AddTransient<IEmailSender, EmailSender>();
            services.AddTransient<ISMSSender, SMSSender>();

            //// 增加MVC
            services.AddMvc();

            //// 消息队列-发送-工厂
            //RabbitMQConfigModel RabbitMQConfig = Configuration.GetSection("RabbitMQ").Get<RabbitMQConfigModel>();
            //IRabbitMQFactoryService mqFactoryService = new RabbitMQFactoryService(RabbitMQConfig);


            //// Socket - 端口号
            //int port = int.Parse(Configuration.GetSection("SocketServerPort").Value);
            //ISocketServer socketServer = new SocketServer(port, mqFactoryService);
            //services.AddSingleton(socketServer);

            //// 消费者队列       
            //IRabitMQConsumerService mqConsumerService = new RabitMQConsumerService(RabbitMQConfig, socketServer);

            //// 发送队列
            //services.AddSingleton(mqFactoryService);

            //// 接收队列
            //services.AddSingleton(mqConsumerService);


            //ServiceProvider provider = services.BuildServiceProvider();
            //ApplicationDbContext dbContext = provider.GetService<ApplicationDbContext>();
            //socketServer.SetApplicationDbcontext(dbContext);
        }

        // This method gets called by the runtime. Use this method to configure the HTTP request pipeline.
        public void Configure(IApplicationBuilder app, IHostingEnvironment env)
        {
            if (env.IsDevelopment())
            {
                app.UseBrowserLink();
                app.UseDeveloperExceptionPage();
                app.UseDatabaseErrorPage();
            }
            else
            {
                app.UseExceptionHandler("/Home/Error");
            }

            app.UseStaticFiles();
            app.UseAuthentication();

            app.UseMvc(routes =>
            {
                routes.MapRoute(
                    "default",
                    "{controller=Home}/{action=Index}/{id?}");
            });
        }
    }
}