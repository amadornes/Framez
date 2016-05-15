package com.amadornes.trajectory;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChunkCoordinates;

import com.amadornes.trajectory.api.BlockSet;
import com.amadornes.trajectory.api.ITrajectory;
import com.amadornes.trajectory.api.TrajectoryAPI;
import com.amadornes.trajectory.api.TrajectoryFeature;
import com.amadornes.trajectory.api.vec.BlockPos;
import com.amadornes.trajectory.movement.MovementManager;
import com.amadornes.trajectory.util.MiscUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CommandTrajectory extends CommandBase {

    @Override
    public String getCommandName() {

        return "trajectory";
    }

    @Override
    public String getCommandUsage(ICommandSender p_71518_1_) {

        return "/trajectory <moveblock/movearea> [...]";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) {

        if (args.length == 0)
            throw new WrongUsageException(getCommandUsage(sender));
        if (args[0].equalsIgnoreCase("moveblock")) {
            if (sender.getEntityWorld() == null)
                throw new WrongUsageException("The \"movement\" commands can only be used from inside a world.");
            if (args.length < 5) {
                throw new WrongUsageException("/trajectory moveBlock <x> <y> <z> <type> [extra_data]");
            } else {
                String sX = args[1], sY = args[2], sZ = args[3], type = args[4], sExtraData = null;
                if (args.length > 5) {
                    sExtraData = "";
                    for (int i = 5; i < args.length; i++)
                        sExtraData += args[i] + " ";
                }

                // Parse arguments
                ChunkCoordinates pCoords = sender.getPlayerCoordinates();
                if ((sX.startsWith("~") || sY.startsWith("~") || sZ.startsWith("~")) && pCoords == null)
                    throw new WrongUsageException("Relative coordinates can only be used by players!");
                int x, y, z;
                try {
                    x = MiscUtils.parseCoordinate(sX, pCoords.posX);
                    y = MiscUtils.parseCoordinate(sY, pCoords.posY);
                    z = MiscUtils.parseCoordinate(sZ, pCoords.posZ);
                } catch (Exception ex) {
                    throw new WrongUsageException(ex.getMessage());
                }

                JsonElement json = null;
                if (sExtraData != null) {
                    try {
                        json = new JsonParser().parse(sExtraData);
                    } catch (Exception ex) {
                        throw new WrongUsageException("There was an error parsing the extra movement data!");
                    }
                }
                ITrajectory trajectory;
                try {
                    trajectory = MovementManager.instance.parseTrajectory(type, json);
                } catch (Exception ex) {
                    if (ex.getMessage() != null)
                        throw new WrongUsageException(ex.getMessage());
                    throw new WrongUsageException("There was an error when parsing the extra data!");
                }

                // Move!
                BlockSet blocks = BlockSet.of(sender.getEntityWorld(), x, y, z);
                if (!TrajectoryAPI.instance().canMove(blocks, trajectory))
                    throw new WrongUsageException("Cannot move!");
                TrajectoryAPI.instance().startMoving(blocks, trajectory, EnumSet.of(TrajectoryFeature.MOVE_ENTITIES));

                return;
            }
        } else if (args[0].equalsIgnoreCase("movearea")) {
            System.out.println("MoveArea!");
            if (sender.getEntityWorld() == null)
                throw new WrongUsageException("The \"movement\" commands can only be used from inside a world.");
            if (args.length < 8) {
                throw new WrongUsageException("/trajectory moveArea <minx> <miny> <minz> <maxx> <maxy> <maxz> <type> [extra_data]");
            } else {
                String sMinX = args[1], sMinY = args[2], sMinZ = args[3], sMaxX = args[4], sMaxY = args[5], sMaxZ = args[6], type = args[7], sExtraData = null;
                if (args.length > 8) {
                    sExtraData = "";
                    for (int i = 8; i < args.length; i++)
                        sExtraData += args[i] + " ";
                }

                // Parse arguments
                ChunkCoordinates pCoords = sender.getPlayerCoordinates();
                if ((sMinX.startsWith("~") || sMinY.startsWith("~") || sMinZ.startsWith("~") || sMaxX.startsWith("~")
                        || sMaxY.startsWith("~") || sMaxZ.startsWith("~"))
                        && pCoords == null)
                    throw new WrongUsageException("Relative coordinates can only be used by players!");
                int minX, minY, minZ, maxX, maxY, maxZ;
                try {
                    minX = MiscUtils.parseCoordinate(sMinX, pCoords.posX);
                    minY = MiscUtils.parseCoordinate(sMinY, pCoords.posY);
                    minZ = MiscUtils.parseCoordinate(sMinZ, pCoords.posZ);
                    maxX = MiscUtils.parseCoordinate(sMaxX, pCoords.posX);
                    maxY = MiscUtils.parseCoordinate(sMaxY, pCoords.posY);
                    maxZ = MiscUtils.parseCoordinate(sMaxZ, pCoords.posZ);
                } catch (Exception ex) {
                    throw new WrongUsageException(ex.getMessage());
                }

                if (minX >= maxX)
                    throw new WrongUsageException("Minimum x must be lower than maximum x!");
                if (minY >= maxY)
                    throw new WrongUsageException("Minimum y must be lower than maximum y!");
                if (minZ >= maxZ)
                    throw new WrongUsageException("Minimum z must be lower than maximum z!");

                JsonElement json = null;
                if (sExtraData != null) {
                    try {
                        json = new JsonParser().parse(sExtraData);
                    } catch (Exception ex) {
                        throw new WrongUsageException("There was an error parsing the extra movement data!");
                    }
                }
                ITrajectory trajectory;
                try {
                    trajectory = MovementManager.instance.parseTrajectory(type, json);
                } catch (Exception ex) {
                    if (ex.getMessage() != null)
                        throw new WrongUsageException(ex.getMessage());
                    throw new WrongUsageException("There was an error when parsing the extra data!");
                }

                // Move!
                BlockSet blocks = new BlockSet(sender.getEntityWorld());
                for (int x = 0; x < maxX - minX; x++)
                    for (int y = 0; y < maxY - minY; y++)
                        for (int z = 0; z < maxZ - minZ; z++)
                            blocks.add(new BlockPos(x, y, z).add(minX, minY, minZ));
                if (!TrajectoryAPI.instance().canMove(blocks, trajectory))
                    throw new WrongUsageException("Cannot move!");
                if (blocks.isEmpty())

                    TrajectoryAPI.instance().startMoving(blocks, trajectory, EnumSet.of(TrajectoryFeature.MOVE_ENTITIES));

                return;
            }
        }
        throw new WrongUsageException(getCommandUsage(sender));
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args) {

        List<String> options = new ArrayList<String>();

        if (args.length == 1) {
            options.add("moveblock");
            options.add("movearea");
        }

        if (options.isEmpty())
            return null;

        Iterator<String> it = options.iterator();
        String lastArg = args[args.length - 1];
        while (it.hasNext()) {
            String s = it.next();
            if (!s.startsWith(lastArg))
                it.remove();
        }

        return options;
    }

    @Override
    public int getRequiredPermissionLevel() {

        return 2;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {

        return super.canCommandSenderUseCommand(sender);
    }
}
