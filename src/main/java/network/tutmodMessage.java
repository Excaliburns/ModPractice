package network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import java.util.ArrayList;

public class tutmodMessage implements IMessage
{
    private static int[] chestPos = new int[3];
    private static ArrayList<ItemStack> itemsList;
    private static ArrayList<Integer> airSlots;

    public tutmodMessage()
    {
    }

    public tutmodMessage(int x, int y, int z, ArrayList<ItemStack> newList, ArrayList<Integer> airList)
    {
        chestPos[0] = x;
        chestPos[1] = y;
        chestPos[2] = z;
        itemsList = newList;
        airSlots = airList;
    }

    @Override
    public void toBytes(ByteBuf buf)
    {

        for(int x = 0; x < itemsList.size() ; x++)
        {
            ByteBufUtils.writeItemStack(buf, itemsList.get(x));

        }
        for(int x = 0; x < airSlots.size() ; x++)
        {
            buf.writeInt(airSlots.get(x));
        }

        for(int x = 0; x < 3; x++)
        {
            buf.writeInt(chestPos[x]);
        }

    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        for(int x = 0; x < itemsList.size() ; x++)
        {
          itemsList.set(x, ByteBufUtils.readItemStack(buf));
        }

        for(int x = 0; x < airSlots.size() ; x++)
        {
            airSlots.set(x, buf.readInt());
        }

        for(int x = 0; x < 3; x++)
        {
            chestPos[x] = buf.readInt();
        }

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

            BlockPos chestBlock = new BlockPos(chestPos[0], chestPos[1], chestPos[2]);

            System.out.println(itemsList + ", " + airSlots );

            TileEntity locatedChest = world.getTileEntity(chestBlock);

            if (world.isBlockLoaded(locatedChest.getPos()))
            {
                for (int i = 0; i < itemsList.size(); i++)
                {
                    System.out.println(itemsList.get(i));
                    ((TileEntityChest) locatedChest).setInventorySlotContents(airSlots.get(i), itemsList.get(i));
                }
            }
        }
    }
}
