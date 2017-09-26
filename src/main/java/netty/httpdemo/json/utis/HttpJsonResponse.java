package netty.httpdemo.json.utis;

import io.netty.handler.codec.http.FullHttpResponse;

public class HttpJsonResponse {

    private FullHttpResponse response;
    private Object result;

    public HttpJsonResponse(FullHttpResponse response, Object result) {
        this.response = response;
        this.result = result;
    }

    public FullHttpResponse getResponse() {
        return response;
    }

    public void setResponse(FullHttpResponse response) {
        this.response = response;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
