/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package models;
import org.json.JSONObject;
/**
 *
 * @author HBadr
 */
public class EmailValidation {
    private final String key = "5a3ce41824msh1c5a64a78ca076dp14b7e4jsn08ee066a23b5";
    private final String web = "email-checker.p.rapidapi.com";
    private String test;
     public EmailValidation(String email) {
        String url = "https://email-checker.p.rapidapi.com/verify/v1?email=" + email;
        ConnectToApi api = new ConnectToApi(url, this.web, this.key);
        JSONObject results = api.getJsonObject();
        //System.out.println(results.toString());
        test = results.getString("status");
    }

}
