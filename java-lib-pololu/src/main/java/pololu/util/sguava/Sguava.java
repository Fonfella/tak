package pololu.util.sguava;

public class Sguava
{

    public static <O> O checkNotNull(O object)
    {
        return checkNotNull(object, "Object is null");
    }

    public static <O> O checkNotNull(O object, String message)
    {
        if (object == null) {

            throw new NullPointerException(message);
        }

        return object;
    }
}
