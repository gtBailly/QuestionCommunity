package net.bewithu.questioncommunity.async.handler;

import com.alibaba.fastjson.JSONObject;
import net.bewithu.questioncommunity.Service.EntityType;
import net.bewithu.questioncommunity.Service.FeedService;
import net.bewithu.questioncommunity.Service.QuestionService;
import net.bewithu.questioncommunity.Service.UserService;
import net.bewithu.questioncommunity.async.EventHandle;
import net.bewithu.questioncommunity.async.EventModel;
import net.bewithu.questioncommunity.async.EventType;
import net.bewithu.questioncommunity.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Component
public class FeedHandler implements EventHandle {
    @Autowired
    UserService userService;
    @Autowired
    QuestionService questionService;
    @Autowired
    FeedService feedService;

    @Override
    public void doHandle(EventModel eventModel) {
        // autowired装载的是bean，并且默认是单例的，也就是在IoC容器中就一个对象，可以自己new一个feed对象
        Feed feed = new Feed();
        feed.setUserId(eventModel.getActorId());
        feed.setCreatedDate(new Date());
        switch (eventModel.getType()) {
            case COMMENT:
                feed.setType(EntityType.ENTITY_COMMENT);
                break;
            case QUESTION:
                feed.setType(EntityType.ENTITY_QUESTION);
                break;
        }
        feed.setData(intizeCommentData(eventModel));
        feedService.insertFeed(feed);
    }


    // 每次发生了评论或者发布问题事件，就相当生成了一个新鲜事，这时要创建一个feed对象
    @Override
    public List<EventType> getSupportEventType() {
        return Arrays.asList(EventType.COMMENT, EventType.QUESTION);
    }

    public String intizeCommentData(EventModel eventModel) {
        HashMap<String, String> map = new HashMap<>();
        map.put("userName", userService.getUserById(eventModel.getActorId()).getName());
        map.put("userHeadUrl", userService.getUserById(eventModel.getActorId()).getHeadUrl());
        // 这里的发布评论也是对某一问题进行的评论，所以这时候记录的还是评论的问题的标题和ID
        map.put("questionTitle", questionService.getQuestion(eventModel.getEntityId()).getTitle());
        map.put("questionId", String.valueOf(questionService.getQuestion(eventModel.getEntityId()).getId()));
        map.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date()));
        return JSONObject.toJSONString(map);
    }
}
