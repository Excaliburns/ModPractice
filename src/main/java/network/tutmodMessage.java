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
import utils.ChestCoordStorage;

import java.util.ArrayList;

public class tutmodMessage implements IMessage
{
    private ChestCoordStorage[] chestPos = new ChestCoordStorage[3];
    private ArrayList<ItemStack> itemsList;
    private ArrayList<Integer> airSlots;

    public tutmodMessage()
    {
    }

    public tutmodMessage(ChestCoordStorage[] chestCoords, ArrayList<ItemStack> newList, ArrayList<Integer> airList)
    {
        for(int i = 0; i < 3 ; i++)
        {
          chestPos[i] = chestCoords[i];
        }
        itemsList = newList;
        airSlots = airList;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {

        for (int x = 0; x < itemsList.size(); x++)
        {
            itemsList.set(x, ByteBufUtils.readItemStack(buf));
        }
        for (int x = 0; x < airSlots.size(); x++)
        {
            airSlots.set(x, buf.readInt());
        }

        for (int x = 0; x < 3; x++)
        {
            chestPos[x].setX(buf.readInt());
            chestPos[x].setY(buf.readInt());
            chestPos[x].setZ(buf.readInt());
        }
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

        for(int x = 0; x < 3 ; x++)
        {
            buf.writeInt(chestPos[x].getX());
            buf.writeInt(chestPos[x].getY());
            buf.writeInt(chestPos[x].getZ());
        }
    }

    public static class Handler implements IMessageHandler<tutmodMessage, IMessage>
    {
        @Override
        public IMessage onMessage(tutmodMessage message, MessageContext ctx)
        {
            FMLCommonHandler.instance().getWorldThread(ctx.netHandler).addScheduledTask(() -> handle(message, ctx));
            return null;
        }

        private void handle(tutmodMessage message, MessageContext ctx)
        {
            TileEntity chestArray[] = new TileEntity[3];
            EntityPlayerMP EntityPlayer = ctx.getServerHandler().player;
            World world = EntityPlayer.getEntityWorld();

            for(int i = 0; i < 3 ; i ++)
            {
                chestArray[i] = world.getTileEntity(new BlockPos(message.chestPos[i].getX(), message.chestPos[i].getY(), message.chestPos[i].getZ()));
            }

            if (world.isBlockLoaded(chestArray[2].getPos()))
            {
                for (int i = 0; i < message.itemsList.size(); i++)
                {
                    ((TileEntityChest) chestArray[2]).setInventorySlotContents(message.airSlots.get(i), message.itemsList.get(i));
                }
                for (int i = 0; i < 2 ; i++)
                {
                    for(int x = 0; i < ((TileEntityChest) chestArray[i]).getSizeInventory() ; x++)
                    {
                        ((TileEntityChest) chestArray[i]).setInventorySlotContents(x, null );
                    }

                    chestArray[i].getTileData().setBoolean("hasBeenAccessed", false);
                }
            }
        }
    }
}
