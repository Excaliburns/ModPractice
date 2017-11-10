package network;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class tutmodPacketHandler
{
    private static int packetId = 0;

    public static SimpleNetworkWrapper INSTANCE = null;

    public tutmodPacketHandler()
    {

    }

    public static int nextID()
    {
        return packetId++;
    }

    public static void registerMessages(String channelName)
    {
        INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(channelName);
        registerMessages();
    }

    public static void registerMessages()
    {
        INSTANCE.registerMessage(tutmodHuskItemMessage.Handler.class, tutmodHuskItemMessage.class, nextID(), Side.SERVER);
    }
}
