# PushNotification

  Push Notifications using Microsoft Azure push notification hub. For android side we use Google GCM service.

1. Follow the tutorial of Microsoft

https://docs.microsoft.com/en-us/azure/notification-hubs/notification-hubs-android-push-notification-google-gcm-get-started

Problems may meet
https://stackoverflow.com/questions/39379532/azure-notification-hub-no-longer-accepting-gcm-api-key

Login Azure to get the connection string and hub name
https://portal.azure.com/

Apply for SenderId :
https://console.developers.google.com/cloud-resource-manager


2. Sending method on server side
   1) set connection string and hubname
      private static string connectionString = "connection string in portal";
      private static string hubName = "your hub name in portal";
   2) send notification
           public static async void SendTemplateNotificationAsync(String[] channels,string message)
        {
            NotificationHubClient hub = NotificationHubClient.CreateClientFromConnectionString(connectionString, hubName);
            Dictionary<string, string> templateParams = new Dictionary<string, string>();
            
            foreach (var channel in channels)
            {
                templateParams["url"] = "http://www.g.cn";
                templateParams["open_type"] = new Random().Next(1,10).ToString();
                templateParams["message"] = message;
                var result = await hub.SendTemplateNotificationAsync(templateParams, channel);
            }
        }
       
       In this method ,we call SendTemplateNotificationAsync method ,and pass two parameters. One is the message we need to send (a dictionary) the other is the channel or tags you want to send. Also we can send a tag expresssion like (TagOne && !TagTwo)  you may see https://docs.microsoft.com/en-us/azure/notification-hubs/notification-hubs-tags-segment-push-message for details
       

3. Subscribe Channel and tags on android side
    In the Notifications.java file , we can see method storeCategoriesAndSubscribe, and we may pass the tags or channels that we want to subscribe to this function. And it'll call subscribeToCategories , in this function we should pay attention to the templateBodyGCM field,it should match the parameter passed from api
    
     public void storeCategoriesAndSubscribe(Set<String> categories)
    {
        SharedPreferences settings = context.getSharedPreferences(PREFS_NAME, 0);
        settings.edit().putStringSet("categories", categories).commit();
        subscribeToCategories(categories);
    }
    
    public void subscribeToCategories(final Set<String> categories) {
        new AsyncTask<Object, Object, Object>() {
            @Override
            protected Object doInBackground(Object... params) {
                try {

                    if(!categories.contains(NotificationSettings.BasicChannelID)){
                        categories.add(NotificationSettings.BasicChannelID);
                    }


                    String regid = gcm.register(senderId);

                    String templateBodyGCM = "{\"data\":         {\"message\":\"$(message)\",\"open_type\":\"$(open_type)\",\"url\":\"$(url)\"}}";

                    hub.registerTemplate(regid,"simpleGCMTemplate", templateBodyGCM,
                            categories.toArray(new String[categories.size()]));

                } catch (Exception e) {
                    Log.e("MainActivity", "Failed to register - " + e.getMessage());
                    return e;
                }
                return null;
            }

            protected void onPostExecute(Object result) {
                String message = "Subscribed for categories: "
                        + categories.toString();
                Toast.makeText(context, message,
                        Toast.LENGTH_LONG).show();

                //Useless just for demo to set the right checkbox status for easier observation
                cts = categories;
            }
        }.execute(null, null, null);
    }


/*
Baidu API:
http://push.baidu.com/doc/android/api
Baidu create key
http://push.baidu.com/console/app
*/
