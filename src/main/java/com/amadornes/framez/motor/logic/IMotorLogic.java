package com.amadornes.framez.motor.logic;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.amadornes.blockdata.BlockData;
import com.amadornes.framez.api.DynamicReference;
import com.amadornes.framez.api.motor.IMotorAction;
import com.amadornes.framez.motor.MotorTrigger;
import com.amadornes.framez.movement.IMovement;
import com.amadornes.framez.movement.MovingBlock;
import com.amadornes.framez.movement.MovingStructure;
import com.amadornes.framez.tile.TileMotor;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraftforge.common.util.INBTSerializable;

public interface IMotorLogic<P> extends INBTSerializable<NBTTagCompound> {

    public static IMotorLogic<?> create(int meta) {

        try {
            return MotorLogicType.VALUES[meta].logicClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static IMotorLogic<?> create(int meta, EntityPlayer player, RayTraceResult hit) {

        try {
            MotorLogicType type = MotorLogicType.VALUES[meta];
            return (IMotorLogic<?>) type.logicClass.getConstructors()[0].newInstance(type.placement.getPlacementData(player, hit));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public default int getID() {

        for (int i = 0; i < MotorLogicType.VALUES.length; i++) {
            if (MotorLogicType.VALUES[i].logicClass == getClass()) {
                return i;
            }
        }
        return -1;
    }

    public void initTriggers(Map<IMotorAction, MotorTrigger> triggers, List<IMotorAction> actionIdMap);

    public EnumFacing getFace();

    public TileMotor getMotor();

    public void setMotor(DynamicReference<TileMotor> motor);

    public default BlockPos getStructureSearchLocation(IMotorAction action) {

        return getMotor().getMotorPos();
    }

    public boolean rotate(EnumFacing axis);

    public default double getConsumedEnergy(MovingStructure structure, double energyApplied) {

        return 0; // TODO: Let's not forget to change this, okay?
    }

    public default void performAction(IMotorAction action) {

        if (action.isMoving()) {
            getMotor().move(action);
        }
    }

    public boolean canMove(MovingStructure structure, IMotorAction action);

    public default boolean canMove() {

        return true;
    }

    public default void move(MovingStructure structure, IMotorAction action, int duration) {

        // Default implementation that just teleports blocks from point A to point B
        // TODO: Implement Project Parallax

        Map<BlockData, BlockPos> transformed = new HashMap<BlockData, BlockPos>();
        for (Entry<MovingBlock, BlockPos> block : structure.getBlocks().entrySet()) {
            BlockData data = block.getKey().toBlockData();
            data.remove(block.getKey().getWorld(), block.getKey().getPos(), 3);
            data = transform(structure, action, data);
            transformed.put(data, block.getValue());
        }
        for (Entry<BlockData, BlockPos> block : transformed.entrySet()) {
            block.getKey().place(getMotor().getMotorWorld(), block.getValue(), 3);
        }
    }

    public default BlockData transform(MovingStructure structure, IMotorAction action, BlockData data) {

        return data;
    }

    public default void onMovementComplete() {

    }

    public IMovement getMovement(Set<MovingBlock> blocks, IMotorAction action);

    public default void onFirstTick() {

    }

    public default void validate() {

    }

    public default void invalidate() {

    }

}
