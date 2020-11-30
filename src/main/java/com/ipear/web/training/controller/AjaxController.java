package com.ipear.web.training.controller;

import com.ipear.web.training.entity.ExerciseRecord;
import com.ipear.web.training.entity.User;
import com.ipear.web.training.mapper.ExerciseRecordRepository;
import com.ipear.web.training.mapper.UserRepository;
import lombok.Data;
import net.sf.json.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
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

    @Autowired
    ExerciseRecordRepository exerciseRecordRepository;

    static Map<String, Pair<String,Integer>> chapter_name = new HashMap<>() {{
            put("1_1", Pair.of("1.1 函数",3));
            put("1_2", Pair.of("1.2 数列的极限",2));
            put("1_3", Pair.of("1.3 函数的极限",3));
            put("1_4", Pair.of("1.4 极限的存在法则",3));

            put("2_1", Pair.of("2.1 导数的概念",3));
            put("2_2", Pair.of("2.2 导数的求导法则",3));
            put("2_3", Pair.of("2.3 高阶导数",3));
            put("2_4", Pair.of("2.4 微分",3));
    }};

    @RequestMapping("/getChapterInfo/{chapter}")
    public Map<String, Object> getChapterInfo(@PathVariable String chapter, HttpServletRequest request) {
        Cookie[] cookies= request.getCookies();
        String username="";
        for(Cookie c:cookies){
            if(c.getName().equals("username")){
                username=c.getValue();
                break;
            }
        }
        ExerciseRecord exerciseRecord= exerciseRecordRepository.getExerciseRecordByUid(username);

        Map<String,Object> map=new HashMap<>();
        map.put("chapter_name",chapter_name.get(chapter).getFirst());
        map.put("problem_count",chapter_name.get(chapter).getSecond());
        map.put("markedItems",exerciseRecord.record);
        return map;
    }

    @RequestMapping(value = "/updateExerciseRecord/",method = {RequestMethod.POST})
    public Map<String, Object> updateExerciseRecord(@RequestBody String data, HttpServletRequest request) {
        Cookie[] cookies= request.getCookies();
        String username="";
        for(Cookie c:cookies){
            if(c.getName().equals("username")){
                username=c.getValue();
                break;
            }
        }

        ExerciseRecord exerciseRecord= exerciseRecordRepository.getExerciseRecordByUid(username);
        exerciseRecord.record=data;
        exerciseRecordRepository.save(exerciseRecord);

        return new HashMap<>(){{
            put("status","success");
        }};
    }

    static class ModifyPasswordData{
        public String old;
        public String _new;
    }

    @RequestMapping(value = "/modifyPassword",method = RequestMethod.POST)
    public Map<String, Object> changePassword(@RequestBody ModifyPasswordData key, HttpServletRequest request) {
        Cookie[] cookies= request.getCookies();
        String username="";
        for(Cookie c:cookies){
            if(c.getName().equals("username")){
                username=c.getValue();
                break;
            }
        }
        String old_password= key.old;
        String new_password= key._new;
        User user= userRepository.getUserByUid(username);
        if(user==null||!user.password.equals(old_password)){
            return new HashMap<>(){{
                put("status","fail");
            }};
        }else{
            user.password=new_password;
            userRepository.save(user);
            return new HashMap<>(){{
                put("status","success");
            }};
        }
    }

    @ResponseBody
    @RequestMapping("/doLogin")
    public Map<String, Object> doLogin(@RequestBody RegisterData data, HttpServletResponse response){
        String username= data.username;
        String password= data.password;

        try{
            User user= userRepository.getUserByUid(username);
            if(user==null|| !user.password.equals(password)){
                return new HashMap<>(){{
                    put("status","fail");
                }};
            }else{
                Cookie cookie = new Cookie("username", username);
                cookie.setMaxAge(-1); // cookie in session
                response.addCookie(cookie);
                return new HashMap<>(){{
                    put("status","success");
                }};
            }
        }catch (Exception ex){
            return new HashMap<>(){{
                put("status","fail");
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
            user.uid= data.username;
            user.alias=data.username;
            user.password=data.password;
            userRepository.save(user);

            // initialize exercise record
            ExerciseRecord exerciseRecord=new ExerciseRecord();
            exerciseRecord.uid= data.username;
            exerciseRecord.record="{}";
            exerciseRecordRepository.save(exerciseRecord);

            return "{\"status\":\"success\"}";

        }catch (Exception ex){
            return "{\"status\":\"failed\"}";
        }

    }

}
