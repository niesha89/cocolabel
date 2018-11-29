package ns.coco.cocolabel.exception;
/**
 * 运行相关异常
 */
public class CocoRuntimeException extends RuntimeException {

    public CocoRuntimeException() {
        super();
    }

    public CocoRuntimeException(String message) {
        super(message);
    }

    public CocoRuntimeException(Throwable t) {
        super(t);
    }

    public CocoRuntimeException(String message, Throwable t) {
        super(message, t);
    }

}
