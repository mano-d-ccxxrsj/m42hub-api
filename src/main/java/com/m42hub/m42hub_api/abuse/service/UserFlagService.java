package com.m42hub.m42hub_api.abuse.service;

import com.m42hub.m42hub_api.abuse.entity.UserFlag;
import com.m42hub.m42hub_api.abuse.repository.UserFlagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFlagService {

    private final UserFlagRepository userFlagRepository;

    public void saveUserFlag(UserFlag userFlag) {
        UserFlag flag = new UserFlag();
        flag.setUserId(userFlag.getUserId());
        flag.setField(userFlag.getField());
        flag.setAction(userFlag.getAction());
        flag.setTargetEndpoint(userFlag.getTargetEndpoint());
        flag.setAttemptedText(userFlag.getAttemptedText());
        flag.setMatchedWords(String.join(", ", userFlag.getMatchedWords()));
        flag.setDetails("");
        userFlagRepository.save(flag);
    }
}