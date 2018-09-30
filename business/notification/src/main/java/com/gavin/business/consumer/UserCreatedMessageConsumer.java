package com.gavin.business.consumer;

import com.gavin.business.dto.UserCreatedMailDto;
import com.gavin.business.exception.EmailSendException;
import com.gavin.business.service.MailService;
import com.gavin.common.consumer.MessageConsumer;
import com.gavin.common.messaging.UserCreatedProcessor;
import com.gavin.common.payload.UserCreatedPayload;
import com.gavin.common.util.JsonUtils;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;


@Component
@Slf4j
public class UserCreatedMessageConsumer implements MessageConsumer<UserCreatedPayload> {

    @Autowired
    @Qualifier("poolTaskExecutor")
    private Executor executor;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private MailService mailService;

    @StreamListener(UserCreatedProcessor.INPUT)
    @Transactional
    public void consumeMessage(@Payload UserCreatedPayload _payload) {
        log.info("received user_created message ({}).", JsonUtils.toJson(_payload));

        UserCreatedMailDto mailDto = modelMapper.map(_payload, UserCreatedMailDto.class);

        CompletableFuture
                .runAsync(() -> {
                    try {
                        mailService.sendUserCreatedNotificationMail(mailDto);
                    } catch (MessagingException | TemplateException | IOException e) {
                        throw new EmailSendException(e);
                    }
                }, executor)
                .thenRunAsync(() -> log.info("send user_created notification email successfully. user_id={}", mailDto.getUserId()), executor)
                .exceptionally(e -> {
                    log.error(e.getMessage(), e);
                    log.warn("send user_created notification email failed. user_id={}", mailDto.getUserId());
                    return null;
                });
    }

}
