package net.minecraft.server;

import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
import java.util.List;

@Getter
@Setter
public class PacketPlayOutSpawnEntityLiving implements Packet<PacketListenerPlayOut> {

	public int id;
	public int type;
	public int x;
	public int y;
	public int z;
	public int motionX;
	public int motionY;
	public int motionZ;
	public byte yaw;
	public byte pitch;
	public byte headYaw;
	public DataWatcher l;
	public List<DataWatcher.WatchableObject> m;

	public PacketPlayOutSpawnEntityLiving() {}

	public PacketPlayOutSpawnEntityLiving(EntityLiving entityliving) {
		this.id = entityliving.getId();
		this.type = (byte) EntityTypes.a(entityliving);
		this.x = MathHelper.floor(entityliving.locX * 32.0D);
		this.y = MathHelper.floor(entityliving.locY * 32.0D);
		this.z = MathHelper.floor(entityliving.locZ * 32.0D);
		this.yaw = (byte) ((int) (entityliving.yaw * 256.0F / 360.0F));
		this.pitch = (byte) ((int) (entityliving.pitch * 256.0F / 360.0F));
		this.headYaw = (byte) ((int) (entityliving.aK * 256.0F / 360.0F));
		double d0 = 3.9D;
		double d1 = entityliving.motX;
		double d2 = entityliving.motY;
		double d3 = entityliving.motZ;

		if (d1 < -d0) {
			d1 = -d0;
		}

		if (d2 < -d0) {
			d2 = -d0;
		}

		if (d3 < -d0) {
			d3 = -d0;
		}

		if (d1 > d0) {
			d1 = d0;
		}

		if (d2 > d0) {
			d2 = d0;
		}

		if (d3 > d0) {
			d3 = d0;
		}

		this.motionX = (int) (d1 * 8000.0D);
		this.motionY = (int) (d2 * 8000.0D);
		this.motionZ = (int) (d3 * 8000.0D);
		this.l = entityliving.getDataWatcher();
	}

	public void a(PacketDataSerializer packetdataserializer) throws IOException {
		this.id = packetdataserializer.e();
		this.type = packetdataserializer.readByte() & 255;
		this.x = packetdataserializer.readInt();
		this.y = packetdataserializer.readInt();
		this.z = packetdataserializer.readInt();
		this.yaw = packetdataserializer.readByte();
		this.pitch = packetdataserializer.readByte();
		this.headYaw = packetdataserializer.readByte();
		this.motionX = packetdataserializer.readShort();
		this.motionY = packetdataserializer.readShort();
		this.motionZ = packetdataserializer.readShort();
		this.m = DataWatcher.b(packetdataserializer);
	}

	public void b(PacketDataSerializer packetdataserializer) throws IOException {
		packetdataserializer.b(this.id);
		packetdataserializer.writeByte(this.type & 255);
		packetdataserializer.writeInt(this.x);
		packetdataserializer.writeInt(this.y);
		packetdataserializer.writeInt(this.z);
		packetdataserializer.writeByte(this.yaw);
		packetdataserializer.writeByte(this.pitch);
		packetdataserializer.writeByte(this.headYaw);
		packetdataserializer.writeShort(this.motionX);
		packetdataserializer.writeShort(this.motionY);
		packetdataserializer.writeShort(this.motionZ);
		this.l.a(packetdataserializer);
	}

	public void a(PacketListenerPlayOut packetlistenerplayout) {
		packetlistenerplayout.a(this);
	}

}