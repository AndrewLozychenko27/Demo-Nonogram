package ua.lozychenko.nonogram.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Aspect
@Component
public class LoggerAspect {
    private final Map<String, Logger> loggers = new HashMap<>();

    @Pointcut("execution(* ua.lozychenko.nonogram.service.data.*.*(..))")
    public void dataServices() {
    }

    @Pointcut("execution(* ua.lozychenko.nonogram.controller.*.*(..))")
    public void viewControllers() {
    }

    @Pointcut("execution(* ua.lozychenko.nonogram.controller.rest.*.*(..))")
    public void restControllers() {
    }

    @Pointcut("@annotation(org.springframework.web.bind.annotation.ModelAttribute))")
    public void controllerExclusion() {
    }

    @Pointcut("(viewControllers() || restControllers()) && !controllerExclusion()")
    public void controllers() {
    }

    @Pointcut("execution(* ua.lozychenko.nonogram.service.generator.*.*(..))")
    public void generators() {
    }

    @Before("dataServices()")
    public void logDataServices(JoinPoint point) {
        String className = getClassName(point);
        Logger log = getLogger(className);
        Method method = getMethod(point);

        log.debug("{} {}({})",
                method.getReturnType(),
                method.getName(),
                parseArgs(point.getArgs()));
    }

    @Before("controllers()")
    public void logControllers(JoinPoint point) {
        Method method = getMethod(point);

        if (method.getAnnotation(PostMapping.class) != null) {
            logPost(point, method);
        } else if (method.getAnnotation(GetMapping.class) != null) {
            logGet(point, method);
        } else if (method.getAnnotation(KafkaListener.class) != null) {
            logKafka(point, method);
        }
    }

    @Before("generators()")
    public void logGenerators(JoinPoint point) {
        String className = getClassName(point);
        Logger log = getLogger(className);
        Method method = getMethod(point);

        log.debug("{} {}({}) for puzzle {}:{}",
                method.getReturnType(),
                method.getName(),
                parseArgs(point.getArgs()),
                point.getArgs()[0],
                point.getArgs()[1]);
    }

    private void logGet(JoinPoint point, Method method) {
        String className = getClassName(point);
        Logger log = getLogger(className);

        log.debug("GET {} {}({})",
                method.getAnnotation(GetMapping.class).value()[0],
                method.getName(),
                parseArgs(point.getArgs())
        );
    }

    private void logPost(JoinPoint point, Method method) {
        String className = getClassName(point);
        Logger log = getLogger(className);

        log.debug("POST {} {}({})",
                Arrays.toString(method.getAnnotation(PostMapping.class).value()),
                method.getName(),
                parseArgs(point.getArgs())
        );
    }

    private void logKafka(JoinPoint point, Method method) {
        String className = getClassName(point);
        Logger log = getLogger(className);

        log.debug("Kafka {} {}({})",
                Arrays.toString(method.getAnnotation(KafkaListener.class).topics()),
                method.getName(),
                parseArgs(point.getArgs())
        );
    }

    private String getClassName(JoinPoint point) {
        return point.getTarget().getClass().getCanonicalName();
    }

    private Logger getLogger(String className) {
        if (!loggers.containsKey(className)) {
            loggers.put(className, LoggerFactory.getLogger(className));
        }
        return loggers.get(className);
    }

    private Method getMethod(JoinPoint point) {
        return ((MethodSignature) point.getSignature()).getMethod();
    }

    private String parseArgs(Object[] args) {
        return Arrays.stream(args).map(a -> a == null ? "null" : a.getClass().getSimpleName()).collect(Collectors.joining(", "));
    }
}