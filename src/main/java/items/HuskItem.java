package items;

import core.TutMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import network.tutmodHuskItemMessage;
import network.tutmodPacketHandler;

public class HuskItem extends Item
{
    public HuskItem()
    {
        setRegistryName("huskitem");
        setUnlocalizedName(TutMod.MODID + ".huskitem");

    }

    @SideOnly(Side.CLIENT)
    public void initModel()
    {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }

    @Override
    public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        if(world.getTileEntity(pos) instanceof TileEntityChest)
        {
            NBTTagCompound itemUses;
            if(player.getHeldItem(hand).hasTagCompound())
            {
                itemUses = player.getHeldItem(hand).getTagCompound();
            }
            else
            {
                itemUses = new NBTTagCompound();
            }

            System.out.println(itemUses);

            if(!itemUses.hasKey("Uses"))
            {

                itemUses.setInteger("Uses", 1);

                int ChestCoord[] = new int[3];


                ChestCoord[0] = pos.getX(); ChestCoord[1] = pos.getY(); ChestCoord[2] = pos.getZ();
                itemUses.setIntArray("Chest1Coord", ChestCoord);


            System.out.println("Chest 1 Set");
            }
            else if(itemUses.getInteger("Uses") == 1)
            {
                itemUses.setInteger("Uses", itemUses.getInteger("Uses") + 1);

                int ChestCoord[] = new int[3];


                ChestCoord[0] = pos.getX(); ChestCoord[1] = pos.getY(); ChestCoord[2] = pos.getZ();

                if(ChestCoord != itemUses.getIntArray("Chest1Coord"))
                {
                    itemUses.setIntArray("Chest2Coord", ChestCoord);
                }
                else
                System.out.println("You have selected the same chest twice. Try again.");


                System.out.println("Chest 2 Set");
            }
            else if(itemUses.getInteger("Uses") == 2)
            {
                int ChestCoord[] = new int[3];


                ChestCoord[0] = pos.getX(); ChestCoord[1] = pos.getY(); ChestCoord[2] = pos.getZ();
                itemUses.setIntArray("Chest3Coord", ChestCoord);


                System.out.println("Chest 3 Set");


                itemUses.setInteger("Uses", itemUses.getInteger("Uses") + 1);
            }

            player.getHeldItem(hand).setTagCompound(itemUses);
            System.out.println(player.getHeldItem(hand).getTagCompound().getInteger("Uses"));
        }

        if(world.isRemote && player.getHeldItem(hand).hasTagCompound() && player.getHeldItem(hand).getTagCompound().getInteger("Uses") == 3)
        {
                System.out.println("Sending data to server..");
                tutmodPacketHandler.INSTANCE.sendToServer(new tutmodHuskItemMessage(player.getHeldItem(hand).getTagCompound().getIntArray("Chest1Coord"), player.getHeldItem(hand).getTagCompound().getIntArray("Chest2Coord"), player.getHeldItem(hand).getTagCompound().getIntArray("Chest3Coord")));
        }
        return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
    }
}