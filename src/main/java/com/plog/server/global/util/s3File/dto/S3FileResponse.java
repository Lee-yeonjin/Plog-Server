package com.plog.server.global.util.s3File.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class S3FileResponse {
    String presignedUrl;

    String s3FileName;
}