package com.zyz.ai.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@RequestMapping("/sse")
@RestController
public class SseController {

    @Autowired
    private ChatClient chatClient;

    private static final int COUNT = 10;

    @GetMapping(value = "/data")
    public void data(HttpServletResponse response) throws IOException, InterruptedException {
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        for (int i = 0; i < 10; i++) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String date = LocalDateTime.now().format(formatter);
            System.out.println(date);
            writer.write(String.format("data: 现在的时间是%s\n\n", date));
            writer.flush();
            Thread.sleep(1000);
        }
    }

    @GetMapping("/retry")
    public void retry(HttpServletResponse response) throws IOException {
        log.info("retry");
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = LocalDateTime.now().format(timeFormatter);
        writer.write("retry: 2000\n"); // 2s后重连
        writer.write(String.format("data: 现在的时间是%s\n\n", date));
        writer.flush();
    }

    @GetMapping("/event")
    public void event(HttpServletResponse response) throws IOException {
        log.info("event");
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = LocalDateTime.now().format(timeFormatter);
        writer.write("retry: 2000\n"); // 2s后重连
        writer.write(String.format("event: time\ndata: 现在的时间是%s\n\n", date)); // 主要区别是前端需要用自定义的even的time来接收
        writer.flush();
    }

    private int count = 0;
    @GetMapping("/end")
    public void end(HttpServletResponse response) throws IOException {
        response.setContentType("text/event-stream;charset=utf-8");
        PrintWriter writer = response.getWriter();
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String date = LocalDateTime.now().format(timeFormatter);
        writer.write("retry: 2000\n"); // 2s后重连
        writer.write(String.format("event: time\ndata: 现在的时间是%s\n\n", date)); // 主要区别是前端需要用自定义的even的time来接收
        writer.flush();
        count++;
        log.info("第{}次访问",count);
        if (count == COUNT) {
            writer.write("event: end\n");
            writer.write("data: END\n\n");
            writer.flush();
            log.info("sse end");
        }
    }

    @GetMapping("/flux")
    public Flux<String> flux(HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=utf-8");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return Flux.interval(Duration.ofSeconds(1)).map(s -> LocalDateTime.now().format(timeFormatter));
    }

    @GetMapping(value = "/stream")
    public Flux<String> stream(HttpServletResponse response) {
        response.setContentType("text/event-stream;charset=utf-8");
        Flux<String> content = chatClient.prompt()
                .user("你是谁")
                .stream()
                .content();
        return content.concatWith(Flux.just("END"));
    }
}
