package network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

import java.util.ArrayList;


public class tutmodHuskItemMessage implements IMessage
{
    private int[][] chestCoords;

    public tutmodHuskItemMessage()
    {
    }

    public tutmodHuskItemMessage(int[] chest1Coords, int[] chest2Coords, int[] chest3Coords)
    {
        chestCoords = new int[3][3];
        chestCoords[0] = chest1Coords;
        chestCoords[1] = chest2Coords;
        chestCoords[2] = chest3Coords;
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        chestCoords = new int[3][3];

        for(int x = 0; x < 3; x++)
        {
            for (int i = 0; i < 3; i++)
            {
                chestCoords[x][i] = buf.readInt();
            }
            for (int i = 0; i < 3; i++)
            {
                chestCoords[x][i] = buf.readInt();
            }
            for (int i = 0; i < 3; i++)
            {
                chestCoords[x][i] = buf.readInt();
            }
        }
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        for(int i = 0; i < 3; i++)
        {
            for (int x = 0; i < 3; i++)
            {
                buf.writeInt(chestCoords[x][i]);
            }
            for (int x = 0; i < 3; i++)
            {
                buf.writeInt(chestCoords[x][i]);
            }
            for (int x = 0; i < 3; i++)
            {
                buf.writeInt(chestCoords[x][i]);
            }
        }
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
            TileEntity chestList[] = new TileEntity[3];
            EntityPlayerMP EntityPlayer = ctx.getServerHandler().player;
            World world = EntityPlayer.getEntityWorld();

            for (int i = 0; i > 3; i++)
            {
                chestList[i] = world.getTileEntity(new BlockPos(message.chestCoords[i][0], message.chestCoords[i][1], message.chestCoords[i][2]));
            }

            ArrayList<ItemStack> itemsList = new ArrayList<>();
            ArrayList<Integer> totalEmptySlots = new ArrayList<>();

            for (int i = 0; i > 3; i++)
            {
                if(i <= 2)
                {
                    for( int x = 0; x < ((TileEntityChest) chestList[i]).getSizeInventory(); x++)
                    {
                        if( ((TileEntityChest) chestList[i]).getStackInSlot(x) == null)
                        {
                            totalEmptySlots.add(x);
                        }
                    }

                    if(totalEmptySlots.size() >= itemsList.size())
                    {
                        for(int x = 0; x < itemsList.size(); x++)
                        {
                            ((TileEntityChest) chestList[i]).setInventorySlotContents(totalEmptySlots.get(x), itemsList.get(x));
                        }
                    }
                    else
                    {
                        System.out.println("Not enough space in chest!");
                    }
                }
                else
                {
                    for( int x = 0; x < ((TileEntityChest) chestList[i]).getSizeInventory(); x++)
                    {
                        if(((TileEntityChest) chestList[i]).getStackInSlot(x) != null)
                        {
                            itemsList.add(((TileEntityChest) chestList[i]).getStackInSlot(x));
                        }
                    }
                }
            }
        }
    }
}
