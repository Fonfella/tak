package pololu.util.io;

import pololu.util.sguava.AppendableWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.CharBuffer;

import static pololu.util.io.Readers.inputStreamReader;
import static pololu.util.sguava.Sguava.checkNotNull;

/**
 * Utilities for reading resources.
 *
 * @author Greg Elderfield
 */
public class ResourceUtils
{
    private static final int DEFAULT_BUF_SIZE = 0x800;

    public static void appendResource(String fileName, Appendable onto)
    {
        try (final InputStream in = getStreamOrThrow(fileName)) {
            copyInputStream(in, onto);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        ;
    }

    public static InputStream getStreamOrThrow(String fileName)
    {
        final InputStream result = ResourceUtils.class.getClassLoader().getResourceAsStream(fileName);
        if (result == null) {
            throw new RuntimeException("Could not find resource: " + fileName);
        }
        return result;
    }

    private static void copyInputStream(InputStream in, Appendable onto) throws IOException
    {
        copy(inputStreamReader(in), onto);
    }

    static CharBuffer createBuffer()
    {
        return CharBuffer.allocate(DEFAULT_BUF_SIZE);
    }

    static long copyReaderToWriter(Reader from, Writer to) throws IOException
    {
        checkNotNull(from);
        checkNotNull(to);
        char[] buf = new char[DEFAULT_BUF_SIZE];
        int nRead;
        long total = 0;
        while ((nRead = from.read(buf)) != -1) {
            to.write(buf, 0, nRead);
            total += nRead;
        }
        return total;
    }

    public static long copy(Readable from, Appendable to) throws IOException
    {
        // The most common case is that from is a Reader (like InputStreamReader or StringReader) so
        // take advantage of that.
        if (from instanceof Reader) {
            // optimize for common output types which are optimized to deal with char[]
            if (to instanceof StringBuilder) {
                return copyReaderToBuilder((Reader) from, (StringBuilder) to);
            } else {
                return copyReaderToWriter((Reader) from, asWriter(to));
            }
        } else {
            checkNotNull(from);
            checkNotNull(to);
            long total = 0;
            CharBuffer buf = createBuffer();
            while (from.read(buf) != -1) {
                buf.flip();
                to.append(buf);
                total += buf.remaining();
                buf.clear();
            }
            return total;
        }
    }

    static long copyReaderToBuilder(Reader from, StringBuilder to) throws IOException
    {
        checkNotNull(from);
        checkNotNull(to);
        char[] buf = new char[DEFAULT_BUF_SIZE];
        int nRead;
        long total = 0;
        while ((nRead = from.read(buf)) != -1) {
            to.append(buf, 0, nRead);
            total += nRead;
        }
        return total;
    }

    public static Writer asWriter(Appendable target)
    {
        if (target instanceof Writer) {
            return (Writer) target;
        }
        return new AppendableWriter(target);
    }
}
