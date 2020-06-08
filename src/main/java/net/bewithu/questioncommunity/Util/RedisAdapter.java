package net.bewithu.questioncommunity.Util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.*;
import java.util.List;
import java.util.Set;

/**
 * 这个类是对 jedis 里面的方法做一个封装，主要是使用缓存连接池和异常处理
 */
@Service
public class RedisAdapter implements InitializingBean {
    // 缓存连接池
    private JedisPool jedisPool;

    private static final Logger logger = LoggerFactory.getLogger(RedisAdapter.class);

    // InitializingBean 中有 afterPropertiesSet() 接口，在bean属性初始化之后会调用该方法
    @Override
    public void afterPropertiesSet() throws Exception {
        // 可以使用 JedisPoolConfig 进行配置
        // JedisPoolConfig config = new JedisPoolConfig();
        jedisPool = new JedisPool("redis://localhost:6379/9");
    }

    /**
     * 向 Set 中加入元素
     * @param key
     * @param value
     * @return 被添加到集合中的新元素的数量，不包括因重复被忽略的元素。
     */
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("add" + e.getMessage());
        } finally {
            // 这个一定不能忘记关，因为不关的话默认只能最多八个连接，这时候不关的话，只能执行八次，以为之前获得的连接没有释放
            jedis.close();
        }
        return 0;
    }


    // 移除Set key 中的一个或多个 member 元素，不存在的 member 元素会被忽略。
    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("rem" + e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    // 返回集合中元素的数量
    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("card" + e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    // 判断成员元素是否是集合的成员
    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return false;
    }



    // 插入到 list 表头
    public long lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    /**
     * 移出并获取列表的最后一个元素，如果列表没有元素会阻塞列表直到等待超时或发现可弹出元素为止
     * @param key
     * @return 假如在指定时间内没有任何元素被弹出，则返回一个 nil 和等待时长。
     * 反之，返回一个含有两个元素的列表，第一个元素是被弹出元素所属的 key ，第二个元素是被弹出元素的值。
     */
    public List<String> brpop(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            // 参数0表示一直阻塞下去
            return jedis.brpop(0, key);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

    // 向有序集合添加一个或多个成员，或者更新已存在成员的分数
    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    // 移除有序集合中的一个或多个成员
    public long zrem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrem(key, value);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    // 获取有序集合的成员数
    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return 0;
    }

    // 返回有序集中指定区间内的成员（可选是否带有分数值），通过索引，分数从高到低
    public Set<String> zrevrange(String key, long start, long end) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

    // 返回有序集中，成员的分数值
    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {
            jedis.close();
        }
        return null;
    }

    // 获得一个Jedis连接
    public Jedis getJedis() {
        // 一定不能忘记关
        return jedisPool.getResource();
    }

    // 开启事务
    public Transaction multi(Jedis jedis) {
        try {
            return jedis.multi();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return null;
    }

    //执行事务
    public List<Object> exec(Transaction transaction, Jedis jedis) {
        try {
            return transaction.exec();
        } catch (Exception e) {
            transaction.discard();
            logger.error(e.getMessage());
        } finally {
            if (transaction != null) {
                try {
                    transaction.close();
                } catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
            // 关闭开启这个事务的jedis连接
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
