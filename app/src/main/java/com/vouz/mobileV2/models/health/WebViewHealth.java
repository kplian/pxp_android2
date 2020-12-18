package com.vouz.mobileV2.models.health;

public class WebViewHealth {
    private Boolean success;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    @Override
    public String toString() {
        return "WebViewHealth{" +
                "success=" + success +
                '}';
    }
}
