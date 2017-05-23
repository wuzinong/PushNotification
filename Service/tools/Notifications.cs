using Microsoft.Azure.NotificationHubs;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Reflection;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApplication4.tools
{
   public class Notifications
    {
        public static Notifications Instance = new Notifications();

        private List<Notification> notifications = new List<Notification>();

        private static string connectionString = "Endpoint=sb://mydnvglnotificationdevtest.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=qSrFkM6M8fMiyfn2IRACxkCSoUoQdOl7KgEakVI0iUs=";
        private static string hubName = "mydnvgl-notification-devtest";


        public NotificationHubClient Hub { get; set; }

        private Notifications()
        {
            // Placeholders: replace with the connection string (with full access) for your notification hub and the hub name from the Azure Classics Portal
            Hub = NotificationHubClient.CreateClientFromConnectionString(connectionString, hubName);

            
        }

        public Notification CreateNotification(string message, string richType, string payload)
        {
            var notification = new Notification()
            {
                Id = notifications.Count,
                Message = message,
                RichType = richType,
                Payload = payload,
                Read = false
            };

            notifications.Add(notification);

            return notification;
        }



        public Stream ReadImage(int id)
        {
            var assembly = Assembly.GetExecutingAssembly();
            // Placeholder: image file name (for example, logo.png).
            return assembly.GetManifestResourceStream("AppBackend.img.{logo.png}");
        }

        public class Notification
        {
            public int Id { get; set; }
            // Initial notification message to display to users
            public string Message { get; set; }
            // Type of rich payload (developer-defined)
            public string RichType { get; set; }
            public string Payload { get; set; }
            public bool Read { get; set; }
        }
    }
}
