package com.ipear.web.training.controller;

import com.ipear.web.training.entity.User;
import com.ipear.web.training.mapper.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import net.sf.json.JSONArray;

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

    static Map<String, String> chapter_name = new HashMap<>() {
        {
            put("1-1", "1.1 函数");
            put("1-2", "1.2 数列的极限");
            put("1-3", "1.3 函数的极限");
            put("1-4", "1.4 极限的存在法则");

            put("2-1", "导数的概念");
            put("2-2", "导数的求导法则");
            put("2-3", "高阶导数");
            put("2-4", "微分");
        }
    };

    @RequestMapping("/getChapterInfo/{chapter}")
    public Map<String, Object> getChapterInfo(@PathVariable String chapter) {
        Map<String,Object> map=new HashMap<>();
        map.put("chapter_name","123");
        map.put("problem_count",3);
        return map;
    }

    @RequestMapping("/checkPassword")
    public Map<String, Object> checkPassword(@RequestParam("key") String key){
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
