package netty.粘包;

/**
 * @Author: yangcs
 * @Date: 2022/4/25 16:16
 * @Description:
 */
public class MsgProtocol {
    private Integer  len;
    private byte[] context;

    public MsgProtocol(Integer len, byte[] context) {
        this.len = len;
        this.context = context;
    }

    public Integer getLen() {
        return len;
    }

    public void setLen(Integer len) {
        this.len = len;
    }

    public byte[] getContext() {
        return context;
    }

    public void setContext(byte[] context) {
        this.context = context;
    }
}
