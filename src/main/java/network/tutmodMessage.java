package network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import utils.ChestCoordStorage;

import java.util.ArrayList;

public class tutmodMessage implements IMessage
{
    private static TileEntity locatedChest;
    private static ArrayList<ItemStack> itemsList;
    private static ArrayList<Integer> airSlots;

    public tutmodMessage()
    {
    }

    public tutmodMessage(TileEntity chest, ArrayList<ItemStack> newList, ArrayList<Integer> airList)
    {
        locatedChest = chest;
        itemsList = newList;
        airSlots = airList;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
    }

    public static class Handler implements IMessageHandler<tutmodMessage, IMessage>
    {
        @Override
        public IMessage onMessage(tutmodMessage message, MessageContext ctx)
        {
            EntityPlayerMP serverPlayer = ctx.getServerHandler().player;
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(tutmodMessage message, MessageContext ctx)
        {
            EntityPlayerMP EntityPlayer = ctx.getServerHandler().player;
            World world = EntityPlayer.getEntityWorld();

            if (world.isBlockLoaded(locatedChest.getPos()))
            {
                for (int i = 0; i < itemsList.size(); i++)
                {
                    ((TileEntityChest) locatedChest).setInventorySlotContents(airSlots.get(i), itemsList.get(i));
                }
            }
        }
    }
}
