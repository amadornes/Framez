package com.amadornes.framez.scalatraits

import codechicken.multipart.TileMultipart
import net.minecraft.world.World
import codechicken.microblock.FaceMicroblock
import codechicken.microblock.HollowMicroblock
import codechicken.microblock.Microblock
import codechicken.multipart.TSlottedPart
import codechicken.multipart.PartMap
import com.amadornes.framez.api.movement.ISticky
import com.amadornes.trajectory.api.vec.BlockPos
import com.amadornes.framez.api.movement.IMovement

trait TStickyTile extends TileMultipart with ISticky {

  override def isSideSticky(world: World, position: BlockPos, side: Int, movement: IMovement): Boolean = {
    val fp = partMap(side)
    if (fp != null) {
      if (fp.isInstanceOf[ISticky]) return fp.asInstanceOf[ISticky].isSideSticky(world, position, side, movement)
      if ((fp.isInstanceOf[FaceMicroblock] || fp.isInstanceOf[HollowMicroblock]) && fp.asInstanceOf[Microblock].getSize == 1) return false
    }

    for (p <- partList)
      if (p.isInstanceOf[ISticky] && (!p.isInstanceOf[TSlottedPart] || (p.asInstanceOf[TSlottedPart].getSlotMask & PartMap.CENTER.mask) != 0))
        if (p.asInstanceOf[ISticky].isSideSticky(world, position, side, movement))
          return true

    false
  }

  override def canStickToSide(world: World, position: BlockPos, side: Int, movement: IMovement): Boolean = {
    val fp = partMap(side)
    if (fp != null) {
      if ((fp.isInstanceOf[FaceMicroblock] || fp.isInstanceOf[HollowMicroblock]) && fp.asInstanceOf[Microblock].getSize == 1) return false
    }
    return true;
  }

  override def canBeOverriden(world: World, position: BlockPos) = false

}