package net.bewithu.questioncommunity.Service;

import net.bewithu.questioncommunity.dao.FeedDAO;
import net.bewithu.questioncommunity.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedService {
    @Autowired
    FeedDAO feedDAO;

    // 当发生Comment和Question事件的时候，会创建新鲜事，存入数据库，当用户拉取关注的用户的新鲜事的时候，需要关注用户的IDs
    public List<Feed> getFeeds(List<Integer> userIds, int maxId, int limit) {
        return feedDAO.selectFeeds(userIds, maxId, limit);
    }

    public int insertFeed(Feed feed) {
        return feedDAO.insertFeed(feed);
    }
}
