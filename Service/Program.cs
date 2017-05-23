using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Microsoft.Azure.NotificationHubs;
 
using Newtonsoft.Json;

namespace ConsoleApplication4
{
    class Program
    {


        private static string connectionString = "Endpoint=sb://namespace3.servicebus.windows.net/;SharedAccessKeyName=DefaultFullSharedAccessSignature;SharedAccessKey=mKgyevHkN6WrIvb9XJWVUc1nj4nxUKWiVbegIsueP+Q=";
        private static string hubName = "testpushnotification";


        private List<NotificationEntity> notificationsList = new List<NotificationEntity>();
        static void Main(string[] args)
        {
            string message = string.Empty;

            Console.WriteLine("Please enter your message for broad cast: input 1 ; for tags: input 2");

            String type = Console.ReadLine();

            if (type.Equals("1"))
            {
                Console.WriteLine("Please enter your message like: title|message|deviceID(if have)");
                while (!string.IsNullOrEmpty((message = Console.ReadLine())))
                {
                    SendNotificationAsync(message);
                    message = string.Empty;
                    Console.WriteLine("message sent");
                }
            }
            else if (type.Equals("2"))
            {
                //send to tag users
                // mock data:  channelOne|this message is for channel one
                // or you may use something like : (channelOne && !channelTwo)|message for channelOne and not for channelTwo


                Console.WriteLine("enter channel messages lie: TagOne,TagTwo|messages for list tag notification, and (TagOne && !TagTwo)|messages for tag expression notification");
                String[] cList = new String[] { "channelOne" };

                while (!string.IsNullOrEmpty((message = Console.ReadLine())))
                {
                    String[] whole = message.Split('|');
                    SendTemplateNotificationAsync(whole[0].Split(','), whole[1]);
                    message = string.Empty;
                    Console.WriteLine("message sent");
                }
            }
            else {
                Console.WriteLine("wrong input!");
            }



            


            Console.ReadLine();
        }
 

        private static async void SendNotificationAsync(string m)
        {
            
            string url = "http://www.baidu.com";
 
            NotificationHubClient hub = NotificationHubClient.CreateClientFromConnectionString(connectionString,hubName);

            BaiduMessage bMessage = new BaiduMessage();
            bMessage.title = m.Split('|')[0].ToString();
            bMessage.description = m.Split('|')[1].ToString();
            bMessage.url = "http://www.baidu.com";
            bMessage.open_type = 1;

            string baidu = JsonConvert.SerializeObject(bMessage);

            GoogleMessage gMessage = new GoogleMessage();
            gMessage.data = new googleData() {
                message= m.Split('|')[1].ToString(),
                title = m.Split('|')[0].ToString(),
                url = url,
                open_type= new Random().Next(1, 10)
        };
            gMessage.notification = new googleNotification() {
                click_action= "OPEN_URL",
                color = "#03A9F4"
            } ;
 
            string google = JsonConvert.SerializeObject(gMessage);

 
            NotificationOutcome resultGoogle = await hub.SendGcmNativeNotificationAsync(google);

            //if (m.Split('|').Length>2)
            //{
            //    //通过 hub.register(token, NotificationSettings.channelID).getRegistrationId() 这个方法注册的tag要用下面方法调用tag
            //    //如果通过subscribe注册的就调用 SendTemplateNotificationAsync
            //    NotificationOutcome resultGoogle = await hub.SendGcmNativeNotificationAsync(google, m.Split('|')[2].ToString());
            //}
            //else
            //{
            //    NotificationOutcome resultGoogle = await hub.SendGcmNativeNotificationAsync(google);
            //}
        }



        public static async void SendTemplateNotificationAsync(String[] channels,string message)
        {
            //define notification hub
            NotificationHubClient hub = NotificationHubClient.CreateClientFromConnectionString(connectionString, hubName);
            //Create an array of breaking news categories
            var categories = channels;
            // Sending the notification as a template notification. All template registrations that contain
            // "messageParam" and the proper tags will receive the notifications.
            // This includes APNS, GCM, WNS, and MPNS template registrations.


            Dictionary<string, string> templateParams = new Dictionary<string, string>();
            
            foreach (var category in categories)
            {
                templateParams["url"] = "http://www.g.cn";
                templateParams["open_type"] = new Random().Next(1,10).ToString();
                templateParams["message"] = message;

                var result = await hub.SendTemplateNotificationAsync(templateParams, category);
            }
        }




        private class NotificationTemplate {
           public string Message { get; set; }
           public string ObjId { get; set; }
           public string ActionCode { get; set; }
         
        }


        public class NotificationEntity
        {
            public int Id { get; set; }
            // Initial notification message to display to users
            public string Message { get; set; }
            // Type of rich payload (developer-defined)
            public string RichType { get; set; }
            public string Payload { get; set; }
            public bool Read { get; set; }
        }
        public NotificationEntity CreateNotification(string message, string richType, string payload)
        {
            var notification = new NotificationEntity()
            {
                Id = notificationsList.Count,
                Message = message,
                RichType = richType,
                Payload = payload,
                Read = false
            };

            notificationsList.Add(notification);

            return notification;
        }


        public class BaiduMessage {
            public string title { get; set; }
            public string description { get; set; }
            public int notification_builder_id { get; set; }
            public int notification_basic_style { get; set; }
            public int open_type { get; set; }
            public string url { get; set; }
            public string pkg_content { get; set; }
            public string custom_content { get; set; }

            public string tag { get; set; }

            public int push_type { get; set; }
        }


        //Baidu message 
        //{  
        //    "title" : "hello" ,  
        //    "description": "hello world" //必选  
        //    "notification_builder_id": 0, //可选  
        //    "notification_basic_style": 7, //可选  
        //    "open_type":0, //可选  
        //    "url": "http://developer.baidu.com", //可选  
        //    "pkg_content":"", //可选  
        //    "custom_content":{"key":"value"},  
        //}
        // title：通知标题，可以为空；如果为空则设为appid对应的应用名;
        // description：通知文本内容，不能为空;
        // notification_builder_id：android客户端自定义通知样式，如果没有设置默认为0;
        // notification_basic_style：只有notification_builder_id为0时有效，可以设置通知的基本样式包括
        //(响铃：0x04; 振动：0x02;可清除：0x01;),这是一个flag整形，每一位代表一种样式,如果想选择任意两种或三种通知样式，
        // notification_basic_style的值即为对应样式数值相加后的值。
        // open_type：点击通知后的行为(1：打开Url; 2：自定义行为；); 
        //     open_type = 1，url != null：打开网页； 
        //     open_type = 2，pkg_content = null：直接打开应用； 
        //     open_type = 2，pkg_content != null：自定义动作打开应用。
        // url：需要打开的Url地址，open_type为1时才有效;
        // pkg_content：open_type为2时才有效，Android端SDK会把pkg_content字符串转换成Android Intent, 
        // 通过该Intent打开对应app组件，所以pkg_content字符串格式必须遵循Intent uri格式，最简单的方法可以通过Intent方法toURI()获取
        // custom_content：自定义内容，键值对，Json对象形式(可选)；在android客户端，这些键值对将以Intent中的extra进行传递。
        //http://push.baidu.com/doc/restapi/msg_struct
        //http://www.tuicool.com/articles/ZnmANn
         //method string 是   方法名，必须存在：push_msg。
                //apikey string 是   访问令牌，明文AK，可从此值获得App的信息，配合sign中的sk做合法性身份认证。
                //user_id string 否   用户标识，在Android里，channel_id + userid指定某一个特定client。不超过256字节，如果存在此字段，则只推送给此用户。
                //push_type uint 是   推送类型，取值范围为：1～3
                //1：单个人，必须指定user_id 和 channel_id （指定用户的指定设备）或者user_id（指定用户的所有设备）
                //2：一群人，必须指定 tag
                //3：所有人，无需指定tag、user_id、channel_id
                //channel_id  uint 否   通道标识
                //tag string 否   标签名称，不超过128字节
                //device_type uint 否   设备类型，取值范围为：1～5
                //云推送支持多种设备，各种设备的类型编号如下：
                //1：浏览器设备；
                //2：PC设备；
                //3：Andriod设备；
                //4：iOS设备；
                //5：Windows Phone设备；
                //如果存在此字段，则向指定的设备类型推送消息。 默认为android设备类型。
                //message_type uint 否   消息类型
                //0：消息（透传给应用的消息体）
                //1：通知（对应设备上的消息通知）
                //默认值为0。
                //messages string 是   指定消息内容，单个消息为单独字符串。如果有二进制的消息内容，请先做 BASE64 的编码。
                //当message_type为1 （通知类型），请按以下格式指定消息内容。 
                //通知消息格式及默认值：
                //{
                ////android必选，ios可选
                //"title" : "hello" ,   
                //“description: "hello world" 

                ////android特有字段，可选
                //"notification_builder_id": 0,
                //"notification_basic_style": 7,
                //"open_type":0,
                //"net_support" : 1,
                //"user_confirm": 0,
                //"url": "http://developer.baidu.com",
                //"pkg_content":"",
                //"pkg_name" : "com.baidu.bccsclient",
                //"pkg_version":"0.1",

                ////android自定义字段
                //"custom_content": {
	               // "key1":"value1", 
	               // "key2":"value2"
	               // },  

                ////ios特有字段，可选
                //"aps": {
	               // "alert":"Message From Baidu Push",
	               // "sound":"",
	               // "badge":0
	               // },

                ////ios的自定义字段
                //"key1":"value1", 
                //"key2":"value2"
                //}
                //    注意：
                //当description与alert同时存在时，ios推送以alert内容作为通知内容
                //当custom_content与 ios的自定义字段"key":"value"同时存在时，ios推送的自定义字段内容会将以上两个内容合并，但推送内容整体长度不能大于256B，否则有被截断的风险。
                //此格式兼容Android和ios原生通知格式的推送。
                //如果通过Server SDK推送成功，Android端却收不到通知，解决方案请参考该： 问题
                //msg_keys    string 是   消息标识。
                //指定消息标识，必须和messages一一对应。相同消息标识的消息会自动覆盖。特别提醒：该功能只支持android、browser、pc三种设备类型。
                //message_expires uint 否   指定消息的过期时间，默认为86400秒。必须和messages一一对应。
                //deploy_status uint 否   部署状态。指定应用当前的部署状态，可取值：
                //1：开发状态
                //2：生产状态
                //若不指定，则默认设置为生产状态。特别提醒：该功能只支持ios设备类型。
                //timestamp uint 是   用户发起请求时的unix时间戳。本次请求签名的有效时间为该时间戳+10分钟。
                //sign string 是   调用参数签名值，与apikey成对出现。
                //详细用法，请参考： 签名计算算法
                //expires uint 否   用户指定本次请求签名的失效时间。格式为unix时间戳形式。
                //v uint 否   API版本号，默认使用最高版本。

        public class GoogleMessage
        {
           public googleData data { get; set; }
           public googleNotification notification { get; set; }
        }

        public class googleData {
            public string message { get; set; }
            public string title { get; set; }
            public int open_type { get; set; }
            public string url { get; set; }
        }
        public class googleNotification
        {
            public string click_action { get; set; }
            public string color { get; set; }
        }

        //https://developers.google.com/cloud-messaging/http-server-ref
        //https://github.com/google/gcm/issues/63

    }
}
