package org.elasticsearch.plugin;

import org.elasticsearch.common.xcontent.ToXContent;
import org.elasticsearch.common.xcontent.XContentBuilder;

import java.io.IOException;

public class Photos implements ToXContent {
    private String result;

    public Photos(String result) {
        this.result = result;
    }

    @Override
    public XContentBuilder toXContent(XContentBuilder builder, Params params) throws IOException {
        return builder.field("message", result);
    }
}
