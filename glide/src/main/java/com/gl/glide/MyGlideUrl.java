package com.gl.glide;

import com.bumptech.glide.load.model.GlideUrl;


/*自定义GlideUrl加载图片
        如果图片出于安全的考虑需要在图片地址后面加上token，这样导致同一张图片导致不同的地址，列如下面的地址：
        http://url.image.com/test.jpg?token=adfnjkews8832734
        这里的token可能随时都会变，这样就导致图片地址一直在变，这样就导致缓存失效，一发生改变就去网络加载图片。
        我们可以自己定义自己的GlideUrl来解决这个问题。
        */

public class MyGlideUrl extends GlideUrl {

    private final String mUrl;

    public MyGlideUrl(String url) {
        super(url);
        mUrl = url;
    }

    @Override
    public String getCacheKey() {
        return mUrl.replace(findTokenParam(), "");
    }

    private String findTokenParam() {
        String tokenParam = "";
        int tokenKeyIndex = mUrl.contains("?token=") ? mUrl.indexOf("?token=") : mUrl.indexOf("&token=");
        if (tokenKeyIndex != -1) {
            int nextAndIndex = mUrl.indexOf("&", tokenKeyIndex + 1);
            if (nextAndIndex != -1) {
                tokenParam = mUrl.substring(tokenKeyIndex + 1, nextAndIndex + 1);
            } else {
                tokenParam = mUrl.substring(tokenKeyIndex);
            }
        }
        return tokenParam;
    }

}