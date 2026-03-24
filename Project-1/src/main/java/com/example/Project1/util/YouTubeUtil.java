package com.example.Project1.util;

import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public final class YouTubeUtil {

    private YouTubeUtil() {}

    public static String extractVideoId(String url) {
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("Video URL is required.");
        }

        String trimmed = url.trim();

        try {
            URI uri = URI.create(trimmed);
            String host = Objects.toString(uri.getHost(), "").toLowerCase();
            String path = Objects.toString(uri.getPath(), "");

            // youtu.be/<id>
            if (host.contains("youtu.be")) {
                String candidate = path.startsWith("/") ? path.substring(1) : path;
                return stripAfterDelimiters(candidate);
            }

            // youtube.com/embed/<id>
            if (path.startsWith("/embed/")) {
                return stripAfterDelimiters(path.substring("/embed/".length()));
            }

            // youtube.com/watch?v=<id>
            Map<String, String> query = parseQuery(uri.getRawQuery());
            if (query.containsKey("v")) {
                return stripAfterDelimiters(query.get("v"));
            }
        } catch (IllegalArgumentException ignored) {
            // fall through
        }

        // fallback: find v=
        int vIndex = trimmed.indexOf("v=");
        if (vIndex >= 0) {
            return stripAfterDelimiters(trimmed.substring(vIndex + 2));
        }

        throw new IllegalArgumentException("Could not extract YouTube video id from URL: " + trimmed);
    }

    private static String stripAfterDelimiters(String s) {
        if (s == null) return "";
        String out = s;
        for (char delim : new char[]{'?', '&', '#', '/'} ) {
            int idx = out.indexOf(delim);
            if (idx >= 0) out = out.substring(0, idx);
        }
        return out;
    }

    private static Map<String, String> parseQuery(String rawQuery) {
        Map<String, String> map = new HashMap<>();
        if (rawQuery == null || rawQuery.isBlank()) return map;

        for (String pair : rawQuery.split("&")) {
            int eq = pair.indexOf('=');
            if (eq <= 0) continue;
            String key = URLDecoder.decode(pair.substring(0, eq), StandardCharsets.UTF_8);
            String val = URLDecoder.decode(pair.substring(eq + 1), StandardCharsets.UTF_8);
            map.put(key, val);
        }
        return map;
    }
}