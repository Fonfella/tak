package pololu.usb.exception;

public class UsbRuntimeException extends RuntimeException
{
    public UsbRuntimeException(String message)
    {
        super(message);
    }

    public UsbRuntimeException(Throwable cause)
    {
        super(cause);
    }
}

