package net.bewithu.questioncommunity;

import net.bewithu.questioncommunity.Service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

// 使用JUnit4
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = QuestioncommunityApplication.class)
public class LikeServiceTests {

    @Autowired
    LikeService likeService;

    // 每次有@Test注解的函数执行前都会调用
    @Before
    public void before() {
        System.out.println("before");
    }

    // 每次有@Test注解的函数执行后都会调用
    @After
    public void after() {
        System.out.println("after");
    }

    // 类开始执行前执行一次 ，每次函数调用不会执行
    @BeforeClass
    public static void beforeClass() {
        System.out.println("beforeClass");
    }

    // 类完成执行后执行一次 ，每次函数调用不会执行
    @AfterClass
    public static void afterClass() {
        System.out.println("afterClass");
    }

    @Test
    public void testLike() {
        System.out.println("testLike");
        likeService.like(123, 1, 1);
        Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));

        likeService.disLike(123, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        System.out.println("testException");
        throw new IllegalArgumentException("异常发生了");
    }
}
