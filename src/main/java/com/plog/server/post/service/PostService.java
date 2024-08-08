package com.plog.server.post.service;

import com.plog.server.post.dto.PostRequest;
import com.plog.server.post.dto.PostResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PostService {

    public PostResponse createPost(PostRequest postRequest, UUID userUUID){

        return null;
    }
}
