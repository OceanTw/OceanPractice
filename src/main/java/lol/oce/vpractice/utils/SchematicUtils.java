package lol.oce.vpractice.utils;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.BlockArrayClipboard;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.ForwardExtentCopy;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import lol.oce.vpractice.Practice;
import lol.oce.vpractice.arenas.ArenaDuplicate;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SchematicUtils {

    public void saveSchem(String filename, int x1, int y1, int z1, int x2, int y2, int z2, org.bukkit.World world) {
        World weWorld = new BukkitWorld(world);
        BlockVector3 pos1 = BlockVector3.at(x1, y1, z1);
        BlockVector3 pos2 = BlockVector3.at(x2, y2, z2);
        CuboidRegion cReg = new CuboidRegion(weWorld, pos1, pos2);
        File dataDirectory = new File(Practice.getInstance().getDataFolder(), "maps");
        if (!dataDirectory.exists()) {
            dataDirectory.mkdirs();
        }
        File file = new File(dataDirectory, filename + ".schematic");
        try {
            BlockArrayClipboard clipboard = new BlockArrayClipboard(cReg);
            ForwardExtentCopy copy = new ForwardExtentCopy(weWorld, cReg, clipboard, pos1);
            Operations.complete(copy);
            ClipboardFormat format = ClipboardFormats.findByFile(file);
            if (format != null) {
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    format.getWriter(fos).write(clipboard);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pasteSchem(String filename, Location location, ArenaDuplicate arena) {
        File dataDirectory = new File(Practice.getInstance().getDataFolder(), "maps");
        File file = new File(dataDirectory, filename + ".schematic");

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        if (format == null) {
            throw new IllegalArgumentException("Unsupported schematic format");
        }

        try (FileInputStream fis = new FileInputStream(file);
             ClipboardReader reader = format.getReader(fis)) {

            Clipboard clipboard = reader.read();
            World weWorld = BukkitAdapter.adapt(location.getWorld());
            BlockVector3 to = BlockVector3.at(location.getX(), location.getY(), location.getZ());

            try (EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(weWorld, -1)) {
                Operation operation = new ClipboardHolder(clipboard)
                        .createPaste(editSession)
                        .to(to)
                        .ignoreAirBlocks(false)
                        .build();
                Operations.complete(operation);
            }

            arena.setRedSpawn(new Location(location.getWorld(), to.getX(), to.getY(), to.getZ()));
            arena.setBlueSpawn(new Location(location.getWorld(), to.getX() + clipboard.getDimensions().getX(), to.getY(), to.getZ()));
            arena.setCorner1(new Location(location.getWorld(), to.getX(), to.getY(), to.getZ()));
            arena.setCorner2(new Location(location.getWorld(), to.getX() + clipboard.getDimensions().getX(), to.getY() + clipboard.getDimensions().getY(), to.getZ() + clipboard.getDimensions().getZ()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}