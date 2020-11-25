package com.ipear.web.training.controller;

import com.ipear.web.training.entity.User;
import com.ipear.web.training.mapper.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.sf.json.JSONArray;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Data
class RegisterData{
    String username;
    String password;
}

@RestController
public class AjaxController {

    @Autowired
    UserRepository userRepository;

    static Map<String, String> chapter_name = new HashMap<>() {{
            put("1-1", "1.1 函数");
            put("1-2", "1.2 数列的极限");
            put("1-3", "1.3 函数的极限");
            put("1-4", "1.4 极限的存在法则");

            put("2-1", "导数的概念");
            put("2-2", "导数的求导法则");
            put("2-3", "高阶导数");
            put("2-4", "微分");
    }};

    @RequestMapping("/getChapterInfo/{chapter}")
    public Map<String, Object> getChapterInfo(@PathVariable String chapter) {
        Map<String,Object> map=new HashMap<>();
        map.put("chapter_name","123");
        map.put("problem_count",3);
        return map;
    }

    @RequestMapping("/changePassword")
    public Map<String, Object> changePassword(@RequestParam("key") String key, HttpServletRequest request) {
        JSONArray jsonArray = JSONArray.fromObject(key);
        Cookie[] cookies= request.getCookies();
        String username="";
        for(Cookie c:cookies){
            if(c.getName().equals("username")){
                username=c.getValue();
                break;
            }
        }
        String old_password= jsonArray.getString(0);
        String new_password= jsonArray.getString(1);
        User user= userRepository.getUserByUid(username);
        if(user==null||!user.password.equals(old_password)){
            user.password=new_password;
            userRepository.save(user);
            return new HashMap<>(){{
                put("result","success");
            }};
        }else{
            return new HashMap<>(){{
                put("result","fail");
            }};
        }
    }

    @RequestMapping("/doLogin")
    public Map<String, Object> doLogin(@RequestParam("key") String key, HttpServletResponse response){
        JSONArray jsonArray = JSONArray.fromObject(key);
        String username= jsonArray.getString(0);
        String password= jsonArray.getString(1);

        try{
            User user= userRepository.getUserByUid(username);
            if(user==null|| !user.password.equals(password)){
                return new HashMap<>(){{
                    put("result","fail");
                }};
            }else{
                Cookie cookie = new Cookie("username", username);
                cookie.setMaxAge(-1); // cookie in session
                response.addCookie(cookie);
                return new HashMap<>(){{
                    put("result","correct");
                }};
            }
        }catch (Exception ex){
            return new HashMap<>(){{
                put("result","fail");
            }};
        }
    }

    @ResponseBody
    @RequestMapping("/registerUser")
    public String registerUser(@RequestBody RegisterData data){

        try{

            // check if username exists
            if(userRepository.existsUserByAlias(data.username)){
                return "{\"status\":\"userExist\"}";
            }

            // try to write database
            User user=new User();
            user.alias=data.username;
            user.password=data.password;
            userRepository.save(user);

            return "{\"status\":\"success\"}";

        }catch (Exception ex){
            return "{\"status\":\"failed\"}";
        }

    }

}