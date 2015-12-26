package pl.asie.charset.pipes;

import java.lang.ref.WeakReference;

import io.netty.buffer.ByteBuf;
import gnu.trove.map.TIntObjectMap;
import gnu.trove.map.hash.TIntObjectHashMap;

import net.minecraftforge.fml.common.network.ByteBufUtils;

import mcmultipart.multipart.IMultipart;
import pl.asie.charset.lib.DirectionUtils;
import pl.asie.charset.lib.network.PacketPart;

public class PacketItemUpdate extends PacketPart {
	protected PipeItem item;
	private static final TIntObjectMap<WeakReference<PipeItem>> itemIdCache = new TIntObjectHashMap<WeakReference<PipeItem>>();
	private boolean syncStack;

	public PacketItemUpdate() {
		super();
	}

	public PacketItemUpdate(IMultipart part, PipeItem item, boolean syncStack) {
		super(part);
		this.item = item;
		this.syncStack = syncStack;
	}

    public void readItemData(ByteBuf buf) {
        if (part == null || !(part instanceof PartPipe)) {
            return;
        }

        short id = buf.readShort();
        int dirs = buf.readUnsignedByte();
        int flags = buf.readUnsignedByte();
        int progress = buf.readUnsignedByte();
        boolean addWhenDone = false;

        WeakReference<PipeItem> ref = itemIdCache.get(id);
        item = ref != null ? ref.get() : null;
        syncStack = (flags & 0x04) != 0;

        if (item != null && (item.getOwner() != part || !item.getOwner().getPipeItems().contains(item))) {
            item.getOwner().removeItemClientSide(item);
            itemIdCache.remove(id);
            item = null;
        }

        if (item == null) {
            PartPipe pipe = (PartPipe) part;
            for (PipeItem p : pipe.getPipeItems()) {
                if (p.id == id) {
                    item = p;
                    break;
                }
            }
        }

        if (item == null) {
            if (syncStack) {
                item = new PipeItem((PartPipe) part, id);
                addWhenDone = true;
            } else {
                return;
            }
        }

        item.blocksSinceSync = 0;
        item.input = DirectionUtils.get(dirs & 7);
        item.output = DirectionUtils.get((dirs >> 3) & 7);
        item.reachedCenter = (flags & 0x01) != 0;
        item.progress = progress;

        boolean stuck = (flags & 0x02) != 0;

        item.setStuckFlagClient(stuck);

        if (syncStack) {
            item.stack = ByteBufUtils.readItemStack(buf);
        }

        itemIdCache.put(id, new WeakReference<PipeItem>(item));

        if (addWhenDone) {
            ((PartPipe) part).addItemClientSide(item);
        }
    }

    public void writeItemData(ByteBuf buf) {
        buf.writeShort(item.id);
        buf.writeByte(DirectionUtils.ordinal(item.input) | (DirectionUtils.ordinal(item.output) << 3));
        buf.writeByte((item.reachedCenter ? 0x01 : 0) | (item.isStuck() ? 0x02 : 0) | (syncStack ? 0x04 : 0));
        buf.writeByte(item.progress);

        if (syncStack) {
            ByteBufUtils.writeItemStack(buf, item.getStack());
        }
    }

	@Override
	public void readData(ByteBuf buf) {
		super.readData(buf);
        readItemData(buf);
	}

	@Override
	public void writeData(ByteBuf buf) {
		super.writeData(buf);
        writeItemData(buf);
	}
}
