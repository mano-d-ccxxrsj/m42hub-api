package com.m42hub.m42hub_api.file.client;

import com.m42hub.m42hub_api.file.dto.response.ImgBBResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "imgbb", url = "${imgBB.upload-url}")
public interface ImgBBClient {

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ImgBBResponse uploadImage(@RequestPart("image") MultipartFile file);
}