package okhttp;

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

    public PocketEncodingBuilder add(String name, String value) {
        if (content.size() > 0) {
            content.writeByte('&');
        }
        if (contentBuilder.length()>0)
            contentBuilder.append("&");
        formbudyBuilder.add(name,value);
        contentBuilder.append(name);
        content.writeByte('=');
        contentBuilder.append("=");
        contentBuilder.append(value);
        return this;
    }

    public RequestBody build() {
        //Log.d("PocketEncodingBuilder","contentBuilder:"+contentBuilder.toString());
        DebugUtil.d("contentBuilder:"+contentBuilder.toString());
        return FormBody.create(CONTENT_TYPE, contentBuilder.toString());
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
