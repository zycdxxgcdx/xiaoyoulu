package com.zy.final_project.controller;

import com.zy.final_project.entity.publicpost.PublicPost;
import com.zy.final_project.service.PublicPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/publicpost")
public class PublicPostController {

    @Autowired
    private PublicPostService publicPostService;

    //主页查询全部帖子接口
    @GetMapping("/findAllPages")
    public int findAllPages(){
        return publicPostService.findAllPages();
    }

    //主页分页查询接口
    @GetMapping("/findAll/{page}/{size}")
    public List<PublicPost> findAll(@PathVariable("page") Integer page, @PathVariable("size") Integer size){
        return publicPostService.findAll((page-1)*size,size);
    }
    //主页帖子模糊查询接口
    @GetMapping("/findSearchPost/{input}/{page}/{size}")
    public Map<String,Object> findSearchPost(@PathVariable("input") String input,
                                     @PathVariable("page") Integer page,
                                     @PathVariable("size") Integer size){
        System.out.println(input);
        return publicPostService.findSearchPost(input,(page-1)*size,size);
    }
    //帖子查询接口
    @GetMapping("/findPost/{publicpostid}")
    public Map<String,Object> findPost(@PathVariable("publicpostid") long publicpostid){
        return publicPostService.findPost(publicpostid);
    }
    //帖子评论接口
    @PostMapping("/publicpostComment")
    public Map<String,Object> publicpostComment(@RequestBody Map<String, Object> map){
        HashMap<String, Object> res_map = new HashMap<>();
        if(publicPostService.publicpostComment(map)){
            res_map.put("state",true);
            res_map.put("msg","评论成功");
            return res_map;
        }else{
            res_map.put("state",false);
            res_map.put("msg","评论失败");
            return res_map;
        }
    }
    //获取帖子评论接口
    @GetMapping("/findpublicostComment/{publicpostid}/{page}/{size}")
    public Map<String, Object> findpublicostComment(@PathVariable("publicpostid") long publicpostid,
                                                    @PathVariable("page") Integer page,
                                                    @PathVariable("size") Integer size){
      HashMap<String, Object> map = new HashMap<>();
        System.out.println(page);
        System.out.println(size);
      map.put("commentContent",publicPostService.findpublicostComment(publicpostid,(page-1)*size,size));
      map.put("commentCount",publicPostService.countPublicostComment(publicpostid));
      map.put("replyContent",publicPostService.findreply(publicpostid,(page-1)*size,size));
      return map;
    }

    @PostMapping("/reply")
    public Map<String, Object> reply(@RequestBody Map<String, Object> map){
        return publicPostService.reply(map);
    }

    @GetMapping("/findreply/{publicpostid}/{commentId}/{commentPage}/{replyPage}/{commentpagesize}/{replysize}")
    public Map<String, Object> findReplyByCommentId(@PathVariable("publicpostid") Long publicpostid,
                                                    @PathVariable("commentId") Integer commentId,
                                                    @PathVariable("commentPage") Integer commentPage,
                                                    @PathVariable("replyPage") Integer replyPage,
                                                    @PathVariable("commentpagesize") Integer commentpagesize,
                                                    @PathVariable("replysize") Integer replysize){
        //只拿回复的数据 然后返回给前端覆盖
        //帖子id   评论id    当前帖子中评论第几页   当前评论中回复第几页   评论每一页的条数    回复每一页的条数
        HashMap<String, Object> map = new HashMap<>();
        map.put("state",true);
        map.put("msg","查询回复成功");
        System.out.println(commentId);
        System.out.println(replyPage);
        map.put("replyContent",publicPostService.findReplyByCommentId(publicpostid,commentId,(commentPage-1)*commentpagesize,(replyPage-1)*replysize,commentpagesize,replysize));
        return map;
    }

    @GetMapping("/getStatisticalInfo")
    public Map<String, Object> getStatisticalInfo(){
        return publicPostService.getStatisticalInfo();
    }
}
