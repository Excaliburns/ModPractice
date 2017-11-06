package items;

import core.TutMod;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import network.tutmodMessage;
import network.tutmodPacketHandler;
import utils.ChestCoordStorage;
import utils.ModItems;

import java.util.ArrayList;

public class HuskItem extends Item
{

    private ArrayList<ItemStack> newlist = new ArrayList<>();
    private ChestCoordStorage[] chestList = new ChestCoordStorage[2];
    private ArrayList<Integer> airList = new ArrayList<>();
    private int totalItemUses = 0;
    private boolean itemisUsed = false;

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
    public EnumActionResult onItemUseFirst(EntityPlayer player, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand)
    {
        if (!worldIn.isRemote)
        {
            if (player.getHeldItem(player.getActiveHand()).getItem() == ModItems.huskItem)
            {

                TileEntity locatedChest = worldIn.getTileEntity(pos);

                if (locatedChest instanceof TileEntityChest)
                {
                    System.out.println("Chest located at: " + pos.getX() + ", " + pos.getY() + ", " + pos.getZ());


                    if (!locatedChest.getTileData().hasKey("hasBeenAccessed") && totalItemUses <= 1)
                    {

                        totalItemUses++;

                        chestList[totalItemUses - 1] = new ChestCoordStorage(pos.getX(), pos.getY(), pos.getZ());

                        locatedChest.getTileData().setBoolean("hasBeenAccessed", true);

                        for (int i = 0; i < ((TileEntityChest) locatedChest).getSizeInventory(); i++)
                        {

                            ItemStack itemHolder = (((TileEntityChest) locatedChest).getStackInSlot(i));

                            if (itemHolder.getItem() != Items.AIR)
                            {
                                player.sendMessage(new TextComponentString("Getting Called!"));
                                newlist.add(itemHolder);
                            }
                        }
                    }

                    if (!locatedChest.getTileData().getBoolean("hasBeenAccessed") && itemisUsed)
                    {
                        System.out.println("Using item!");

                        for (int i = 0; i < ((TileEntityChest) locatedChest).getSizeInventory(); i++)
                        {
                            if (((TileEntityChest) locatedChest).getStackInSlot(i).getItem() == Items.AIR)
                            {
                                airList.add(i);
                            }
                        }

                        if (airList.size() > newlist.size())
                        {
                            System.out.println(newlist.toString());
                            System.out.println(newlist.size());

                            tutmodPacketHandler.INSTANCE.sendToServer(new tutmodMessage(locatedChest, newlist, airList));

                            ClearChestStorage(chestList[0].getX(), chestList[0].getY(), chestList[0].getZ(), worldIn, player);
                            ClearChestStorage(chestList[1].getX(), chestList[1].getY(), chestList[1].getZ(), worldIn, player);

                            newlist.clear();
                            totalItemUses = 0;
                        } else
                        {
                            System.out.println("Not enough space!");
                        }
                    }

                    if (totalItemUses == 2)
                        itemisUsed = true;

                    System.out.println(newlist);
                }

            }
            System.out.println(totalItemUses);
        }
        return super.onItemUseFirst(player, worldIn, pos, side, hitX, hitY, hitZ, hand);
    }

    private void ClearChestStorage(int x, int y, int z, World worldIn, EntityPlayer playerIn)
    {

        BlockPos chestBlock = new BlockPos(x, y, z);

        TileEntity locatedChest = worldIn.getTileEntity(chestBlock);

        for (int i = 0; i < ((TileEntityChest) locatedChest).getSizeInventory(); i++)
        {
            locatedChest.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null).extractItem(i, ((TileEntityChest) locatedChest).getStackInSlot(i).getCount(), false);
        }

        locatedChest.getTileData().setBoolean("hasBeenAccessed", false);

    }
}