package com.amadornes.framez.scalatraits

import java.util.Random
import com.amadornes.framez.api.movement.ISticky
import com.amadornes.framez.block.BlockMetamorphicStone
import com.amadornes.framez.compat.fmp.MicroMaterialMetamorphicStone
import codechicken.microblock.CommonMicroblock
import codechicken.microblock.FaceMicroblock
import codechicken.microblock.HollowMicroblock
import codechicken.multipart.IRandomDisplayTick
import codechicken.multipart.TMultiPart
import codechicken.multipart.TRandomUpdateTick
import codechicken.multipart.scalatraits.TRandomDisplayTickTile
import net.minecraft.world.World
import com.amadornes.trajectory.api.vec.BlockPos
import com.amadornes.framez.api.movement.IMovement

trait TMetamorphicStoneMicroblock extends CommonMicroblock with IRandomDisplayTick {

  override def onNeighborChanged() {
    super.onNeighborChanged();
    doTheThing();
  }
  override def onAdded() {
    super.onAdded();
    doTheThing();
  }
  override def onMoved() {
    super.onMoved();
    doTheThing();
  }
  override def onChunkLoad() {
    super.onChunkLoad();
    doTheThing();
  }
  override def onWorldJoin() {
    super.onWorldJoin();
    doTheThing();
  }

  def doTheThing() {

    if (this.isInstanceOf[FaceMicroblock] || this.isInstanceOf[HollowMicroblock]) {
      var mb: CommonMicroblock = this.asInstanceOf[CommonMicroblock];
      if (mb.getIMaterial.isInstanceOf[MicroMaterialMetamorphicStone]) {
        var b: BlockMetamorphicStone = mb.getIMaterial.asInstanceOf[MicroMaterialMetamorphicStone].block.asInstanceOf[BlockMetamorphicStone]
        b.convertIfNeeded(mb.world, mb.x, mb.y, mb.z, mb.getSlot, mb.getIMaterial.asInstanceOf[MicroMaterialMetamorphicStone].meta)
      }
    }
  }

  override def randomDisplayTick(random: Random) = {
    if (this.isInstanceOf[FaceMicroblock] || this.isInstanceOf[HollowMicroblock]) {
      var mb: CommonMicroblock = this.asInstanceOf[CommonMicroblock];
      if (mb.getIMaterial.isInstanceOf[MicroMaterialMetamorphicStone]) {
        var b: BlockMetamorphicStone = mb.getIMaterial.asInstanceOf[MicroMaterialMetamorphicStone].block.asInstanceOf[BlockMetamorphicStone]
        b.spawnFancyParticles(mb.world, mb.x, mb.y, mb.z, mb.getSlot, mb.getIMaterial.asInstanceOf[MicroMaterialMetamorphicStone].meta, getBounds, random)
        b.spawnFancyParticles(mb.world, mb.x, mb.y, mb.z, mb.getSlot ^ 1, mb.getIMaterial.asInstanceOf[MicroMaterialMetamorphicStone].meta, getBounds, random)
      }
    }
  }

}

trait TStopperMicroblock extends CommonMicroblock /*with IMovable*/ {

  //override def getMovementType(world: World, x: Int, y: Int, z: Int, side: Int, movement: IMovement, motor: IMotor): BlockMovementType = {
  //
  //    if (side != getSlot)
  //      return null;
  //
  //    var x_ = x + FramezUtils.getOffsetX(side);
  //    var y_ = y + FramezUtils.getOffsetY(side);
  //    var z_ = z + FramezUtils.getOffsetZ(side);
  //
  //    if (motor != null && x_ == motor.getX() && y_ == motor.getY() && z_ == motor.getZ() && movement != null && movement.isInstanceOf[IMovementSlide]) {
  //      var dir = movement.asInstanceOf[IMovementSlide].getDirection();
  //      var x__ = x + FramezUtils.getOffsetX(dir ^ 1)
  //      var y__ = y + FramezUtils.getOffsetY(dir ^ 1)
  //      var z__ = z + FramezUtils.getOffsetZ(dir ^ 1)
  //
  //      if (BlockStopper.findStopper(world, x__, y__, z__, dir))
  //        return BlockMovementType.UNMOVABLE;
  //
  //      // System.out.println("Needs: " + motor.needsRestart());
  //      //
  //      // if (motor.needsRestart()) {
  //      // // motor.setNeedsRestart(true);
  //      // return BlockMovementType.UNMOVABLE;
  //      // }
  //
  //      def stickies: Buffer[ISticky] = JavaConversions.asScalaBuffer(FrameMovementRegistry.instance().findStickies(world, x__, y__, z__));
  //      for (s <- stickies)
  //        if (s.isSideSticky(world, x__, y__, z__, dir, movement, motor))
  //          return BlockMovementType.MOVABLE;
  //    }
  //
  //    if (BlockStopper.findStopper(world, x_, y_, z_, side ^ 1))
  //      return BlockMovementType.UNMOVABLE;
  //
  //    def stickies: Buffer[ISticky] = JavaConversions.asScalaBuffer(FrameMovementRegistry.instance().findStickies(world, x_, y_, z_));
  //    for (s <- stickies)
  //      if (s.isSideSticky(world, x_, y_, z_, side ^ 1, movement, motor))
  //        return BlockMovementType.MOVABLE;
  //
  // return BlockMovementType.UNMOVABLE;
  //}
  //
  // override def startMoving(block: IMovingBlock) = false
  //
  // override def finishMoving(block: IMovingBlock) = false

}

trait TFrameMicroblock extends CommonMicroblock with ISticky {

  override def isSideSticky(world: World, position: BlockPos, side: Int, movement: IMovement): Boolean = true

  override def canStickToSide(world: World, position: BlockPos, side: Int, movement: IMovement) = true

  override def canBeOverriden(world: World, position: BlockPos) = false

  override def occlusionTest(npart: TMultiPart) =
    if (npart.isInstanceOf[TFrameMicroblock] && getMaterial != npart.asInstanceOf[CommonMicroblock].getMaterial) false else super.occlusionTest(npart)

}