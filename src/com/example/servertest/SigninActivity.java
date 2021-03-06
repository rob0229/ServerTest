package com.example.servertest;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;

public class SigninActivity  extends AsyncTask<String,Void,String>{
	private String userNameQueryResult, queryResult ="";
   private TextView statusField,roleField;
   private Context context;
   private int byGetOrPost = 0; 
   //flag 0 means get and 1 means post.(By default it is get.)
   public SigninActivity(Context context, TextView statusField, TextView roleField,int flag) {
      this.context = context;
      this.statusField = statusField;
      this.roleField = roleField;
      byGetOrPost = flag;
   }

   protected void onPreExecute(){

   }
   @Override
   protected String doInBackground(String... arg0) {
      if(byGetOrPost == 0){ //means by Get Method
         try{
            String username = (String)arg0[0];
            String password = (String)arg0[1];
            String link = "http://fapapp.bugs3.com/login.php?username="+username+"&password="+password;
          //  URL url = new URL(link);
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader
           (new InputStreamReader(response.getEntity().getContent()));

           StringBuffer sb = new StringBuffer("");
           String line="";
           while ((line = in.readLine()) != null) {
              sb.append(line);
              break;
            }
            in.close();
            System.out.println("Get Method returned: " +sb.toString());
            
            userNameQueryResult = sb.toString();
            
            //Serversfree returns an object with an advertisement string appended to the result, 
            //this loop looks for the appending first '<' which starts the added content and stops
            //adding characters to the resultQuery string.
            for (int i = 0; i < userNameQueryResult.length(); i++){
            	if (userNameQueryResult.charAt(i) == '<')
            		break;
            	else 
            		queryResult += userNameQueryResult.charAt(i);
            	
            }
            
            System.out.println("Parsed queryResult is now: " +queryResult);
            
            
            
            
            return queryResult;
      }catch(Exception e){
         return new String("Exception: " + e.getMessage());
      }
      }
      else{
         try{
            String username = (String)arg0[0];
            String password = (String)arg0[1];
            String link="http://myphpmysqlweb.hostei.com/loginpost.php";
            String data  = URLEncoder.encode("username", "UTF-8") 
            + "=" + URLEncoder.encode(username, "UTF-8");
            data += "&" + URLEncoder.encode("password", "UTF-8") 
            + "=" + URLEncoder.encode(password, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection(); 
            conn.setDoOutput(true); 
            OutputStreamWriter wr = new OutputStreamWriter
            (conn.getOutputStream()); 
            wr.write( data ); 
            wr.flush(); 
            BufferedReader reader = new BufferedReader
            (new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line = null;
            // Read Server Response
            while((line = reader.readLine()) != null)
            {
               sb.append(line);
               break;
            }
            System.out.println("Post method returned: " + sb.toString());
            return sb.toString();
         }catch(Exception e){
            return new String("Exception: " + e.getMessage());
         }
      }
   }
   @Override
   protected void onPostExecute(String result){
      this.statusField.setText("Login Successful");
      System.out.println("Inside the onPostExecute************************************");
      this.roleField.setText(result);
   }
}
