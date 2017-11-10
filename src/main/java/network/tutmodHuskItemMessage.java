package network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class tutmodHuskItemMessage implements IMessage
{

    public tutmodHuskItemMessage()
    {
    }

    private void testClass(int size, int size1, int size2)
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    public static class Handler implements IMessageHandler<tutmodHuskItemMessage, IMessage>
    {
        @Override
        public IMessage onMessage(tutmodHuskItemMessage message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(tutmodHuskItemMessage message, MessageContext ctx)
        {
            EntityPlayerMP EntityPlayer = ctx.getServerHandler().player;
            World world = EntityPlayer.getEntityWorld();
        }
    }
}
