package pololu.analog;


public interface PerChannelSource<C, T>
{
    T forChannel(C channelId);
}
