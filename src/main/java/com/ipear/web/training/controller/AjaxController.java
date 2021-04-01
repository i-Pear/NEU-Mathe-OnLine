package com.ipear.web.training.controller;

import com.google.gson.Gson;
import com.ipear.web.training.entity.ExerciseRecord;
import com.ipear.web.training.entity.Problem;
import com.ipear.web.training.entity.User;
import com.ipear.web.training.mapper.ExerciseRecordRepository;
import com.ipear.web.training.mapper.ProblemRepository;
import com.ipear.web.training.mapper.UserRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

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

    @Autowired
    ProblemRepository problemRepository;

    static HashMap chapter_name;

    public AjaxController() throws IOException {
        load_chapter_name();
    }

    @RequestMapping("/getProblemImage/{id}")
    public List<Object> getProblemImage(@PathVariable int id, HttpServletRequest request){
        Problem problem =problemRepository.getProblemById(id);
        return new ArrayList<Object>(){{
            add(problem.img0);
            add(problem.img1);
            add(problem.img2);
            add(problem.img3);
            add(problem.img4);
            add(problem.imgans);
        }};
    }

    @RequestMapping("/getChapterInfo/{chapter}")
    public Map<String, Object> getChapterInfo(@PathVariable String chapter, HttpServletRequest request) {
        Cookie[] cookies= request.getCookies();
        String username = null;
        if(cookies==null){
            return new HashMap<String,Object>(){{
                put("error","cookie invalid.");
            }};
        }
        for(Cookie c:cookies){
            if(c.getName().equals("username")){
                username=c.getValue();
                break;
            }
        }
        if(username==null){
            return new HashMap<String,Object>(){{
                put("error","username invalid.");
            }};
        }

        ExerciseRecord exerciseRecord= exerciseRecordRepository.getExerciseRecordByUid(username);
        List<Problem> problems= problemRepository.getProblemsByChapter(chapter);

        ArrayList<Integer> problem_ids=new ArrayList<Integer>();
        for(Problem problem:problems){
            problem_ids.add(problem.id);
        }

        ArrayList<Integer> problem_ans=new ArrayList<Integer>();
        for(Problem problem:problems){
            problem_ans.add(problem.answer);
        }

        int problem_count=problems.size();

        Map<String,Object> map=new HashMap<>();
        map.put("chapter_name",chapter_name.get(chapter));
        map.put("problem_count",problem_count);
        map.put("problem_ids",problem_ids);
        map.put("answer",problem_ans);
        map.put("markedItems",exerciseRecord.record);
        return map;
    }

    @RequestMapping(value = "/updateExerciseRecord/",method = {RequestMethod.POST})
    public Map<String, Object> updateExerciseRecord(@RequestBody String data, HttpServletRequest request) {
        Cookie[] cookies= request.getCookies();
        String username = null;
        for(Cookie c:cookies){
            if(c.getName().equals("username")){
                username=c.getValue();
                break;
            }
        }
        if(username==null){
            return new HashMap<String,Object>(){{
                put("error","username invalid.");
            }};
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

    static class ChapterInfo{
        String id;
        String name;
    }

    private void load_chapter_name() throws IOException {
        String str = Files.readString(Path.of("chapter_name.json"));
        Gson json=new Gson();
        chapter_name =json.fromJson(str, HashMap.class);
    }

}
