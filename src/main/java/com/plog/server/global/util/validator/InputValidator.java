package com.plog.server.global.util.validator;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

public class InputValidator {

    /**
     * 내용을 검증하고, XSS 공격을 방지하기 위해 HTML 태그를 정제
     */
    public static String sanitizeContent(String content) {

        // XSS 방지를 위해 HTML 태그를 정제
        String sanitizedContent = Jsoup.clean(content, Safelist.basic());

        // 정제 후 내용이 비어있지 않은지 확인
        if (sanitizedContent.trim().isEmpty()) {
            throw new IllegalArgumentException("Content is invalid after sanitization.");
        }

        return sanitizedContent;
    }
}

