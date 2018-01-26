package com.squareup.okhttp;

import com.manaul.highschool.utils.DebugUtil;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Author: vector.huang
 * Email: 642378415@qq.com
 * Date: 2015/10/15
 * Description:<p>{TODO: 用一句话描述}
 */
public class PocketEncodingBuilder {
    private static final MediaType CONTENT_TYPE =
            MediaType.parse("application/x-www-form-urlencoded;charset=utf-8");

    private final Buffer content = new Buffer();
    private final StringBuilder contentBuilder = new StringBuilder();

    FormBody.Builder formbudyBuilder = new FormBody.Builder();
    /** Add new key-value pair. */
   /* public PocketEncodingBuilder add(String name, String value) {
        if (content.size() > 0) {
            content.writeByte('&');
        }

        HttpUrl.canonicalize(content, name, 0, name.length(),
                HttpUrl.FORM_ENCODE_SET, false, true);
        content.writeByte('=');
        HttpUrl.canonicalize(content, value, 0, value.length(),
                HttpUrl.FORM_ENCODE_SET, false, true);
        return this;
    }   */
    public PocketEncodingBuilder add(String name, String value) {
        if (content.size() > 0) {
            content.writeByte('&');
        }
        if (contentBuilder.length()>0)
            contentBuilder.append("&");

        formbudyBuilder.add(name,value);

        contentBuilder.append(name);
       /* HttpUrl.canonicalize(content, name, 0, name.length(),
                HttpUrl.FORM_ENCODE_SET, false, true);*/
        content.writeByte('=');
        contentBuilder.append("=");
        /*
        HttpUrl.canonicalize(content, value, 0, value.length(),
                HttpUrl.FORM_ENCODE_SET, false, true);*/

        contentBuilder.append(value);
        return this;
    }

    /** Add new key-value pair. */
  /*  public PocketEncodingBuilder addEncoded(String name, String value) {
        if (content.size() > 0) {
            content.writeByte('&');
        }
        HttpUrl.canonicalize(content, name, 0, name.length(),
                HttpUrl.FORM_ENCODE_SET, true, true);
        content.writeByte('=');
        HttpUrl.canonicalize(content, value, 0, value.length(),
                HttpUrl.FORM_ENCODE_SET, true, true);
        return this;
    }*/

   /* public RequestBody build() {
        return RequestBody.create(CONTENT_TYPE, content.snapshot());
    } */

    public RequestBody build() {
        //Log.d("PocketEncodingBuilder","contentBuilder:"+contentBuilder.toString());
        DebugUtil.d("contentBuilder:"+contentBuilder.toString());
        return FormBody.create(CONTENT_TYPE, contentBuilder.toString());
        //return formbudyBuilder.build();
    }

    /**
     * e.g key=value&key=value
     * @return key=value&key=value
     */
    @Override
    public String toString() {
        return contentBuilder.toString();
    }
}
