package net.bewithu.questioncommunity.aspect;

// 使用了aspectj
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Aspect
@Component
public class Log {

    // 日志输出的时候可以打印出日志信息所在的类，logger.debug("用户信息"); 将会打出 net.bewithu.questioncommunity.aspect.Log:用户信息
    private static  final Logger logger =  LoggerFactory.getLogger(Log.class);


    /*  execution(* com.sample.service.impl..*.*(..))
        第一个 * :表示返回值的类型任意
        com.sample.service.impl:AOP所切的服务的包名，即，我们的业务部分
        包名后面的 .. :表示当前包及子包
        第二个 * :表示类名，*就是表示的所有的类
        .*(..):表示任何方法名，括号表示参数，两个点表示任何参数
     */
    @Before("execution(* net.bewithu.questioncommunity.Controller.Index.test(..))")
    public void before(JoinPoint joinPoint){
        StringBuilder sb = new StringBuilder();
        for (Object arg : joinPoint.getArgs()) {
            sb.append("arg:" + arg.toString() + "|");
        }
        logger.info("before method:" + sb.toString());
    }

    @After("execution(* net.bewithu.questioncommunity.Controller.Index.test(..))")
    public void after(){
        logger.info("after");
    }
}
