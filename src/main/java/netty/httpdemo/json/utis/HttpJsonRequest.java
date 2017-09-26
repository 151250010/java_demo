package netty.httpdemo.json.utis;

import io.netty.handler.codec.http.FullHttpRequest;
import netty.httpdemo.json.entity.Order;

public class HttpJsonRequest {

    private FullHttpRequest request;
    private Object object;

    public HttpJsonRequest(FullHttpRequest request, Object object) {
        this.request = request;
        this.object = object;
    }

    public FullHttpRequest getRequest() {
        return request;
    }

    public void setRequest(FullHttpRequest request) {
        this.request = request;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    @Override
    public String toString() {
        return "HttpJsonRequest{" +
                "request=" + request +
                ", object=" + object +
                '}';
    }
}
