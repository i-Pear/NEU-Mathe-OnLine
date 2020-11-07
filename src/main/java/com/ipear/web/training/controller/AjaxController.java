package com.ipear.web.training.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.Map;

@Controller
public class AjaxController {

    static HashMap<String, String> chapter_name = new HashMap<>() {
        {
            chapter_name.put("1-1", "1.1 函数");
            chapter_name.put("1-2", "1.2 数列的极限");
            chapter_name.put("1-3", "1.3 函数的极限");
            chapter_name.put("1-4", "1.4 极限的存在法则");

            chapter_name.put("2-1", "导数的概念");
            chapter_name.put("2-2", "导数的求导法则");
            chapter_name.put("2-3", "高阶导数");
            chapter_name.put("2-4", "微分");
        }
    };

    @RequestMapping("/getChapterInfo/{chapter}")
    public Map<String, Object> getChapterInfo(@PathVariable String chapter) {
        Map<String,Object> map=new HashMap<>();
        map.put("chapter_name",chapter_name.get(chapter));
        map.put("problem_count",);
        return map;
    }

}
