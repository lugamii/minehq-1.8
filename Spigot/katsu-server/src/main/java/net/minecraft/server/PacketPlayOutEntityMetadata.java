package net.minecraft.server;

import lombok.Data;
import java.io.IOException;
import java.util.List;

@Data
public class PacketPlayOutEntityMetadata implements Packet<PacketListenerPlayOut> {

    public int a;
    public List<DataWatcher.WatchableObject> b;
    private boolean found = false; // MineHQ

    public PacketPlayOutEntityMetadata() {}

    public PacketPlayOutEntityMetadata(int i, DataWatcher datawatcher, boolean flag) {
        this.a = i;
        if (flag) {
            this.b = datawatcher.c();
        } else {
            this.b = datawatcher.b();
        }
    }

    public PacketPlayOutEntityMetadata(int i, List<DataWatcher.WatchableObject> list) {
        this.a = i;
        this.b = list;
    }

    public void a(PacketDataSerializer packetdataserializer) throws IOException {
        this.a = packetdataserializer.e();
        this.b = DataWatcher.b(packetdataserializer);
    }

    public void b(PacketDataSerializer packetdataserializer) throws IOException {
            packetdataserializer.b(this.a);
            DataWatcher.a(this.b, packetdataserializer);
        }

    public void a(PacketListenerPlayOut packetlistenerplayout) {
        packetlistenerplayout.a(this);
    }
}
