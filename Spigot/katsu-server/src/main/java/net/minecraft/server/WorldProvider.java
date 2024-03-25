//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.minecraft.server;

public abstract class WorldProvider {
    public static final float[] a = new float[]{1.0F, 0.75F, 0.5F, 0.25F, 0.0F, 0.25F, 0.5F, 0.75F};
    protected World b;
    private WorldType type;
    private String i;
    protected WorldChunkManager c;
    protected boolean d;
    public boolean e;
    protected final float[] f = new float[16];
    protected int dimension;
    private final float[] j = new float[4];

    public WorldProvider() {
    }

    public final void a(World var1) {
        this.b = var1;
        this.type = var1.getWorldData().getType();
        this.i = var1.getWorldData().getGeneratorOptions();
        this.b();
        this.a();
    }

    protected void a() {
        float var1 = 0.0F;

        for(int var2 = 0; var2 <= 15; ++var2) {
            float var3 = 1.0F - (float)var2 / 15.0F;
            this.f[var2] = (1.0F - var3) / (var3 * 3.0F + 1.0F) * (1.0F - var1) + var1;
        }

    }

    protected void b() {
        WorldType var1 = this.b.getWorldData().getType();
        if (var1 == WorldType.FLAT) {
            WorldGenFlatInfo var2 = WorldGenFlatInfo.a(this.b.getWorldData().getGeneratorOptions());
            this.c = new WorldChunkManagerHell(BiomeBase.getBiome(var2.a(), BiomeBase.ad), 0.5F);
        } else if (var1 == WorldType.DEBUG_ALL_BLOCK_STATES) {
            this.c = new WorldChunkManagerHell(BiomeBase.PLAINS, 0.0F);
        } else {
            this.c = new WorldChunkManager(this.b);
        }

    }

    public IChunkProvider getChunkProvider() {
        if (this.type == WorldType.FLAT) {
            return new ChunkProviderFlat(this.b, this.b.getSeed(), this.b.getWorldData().shouldGenerateMapFeatures(), this.i);
        } else if (this.type == WorldType.DEBUG_ALL_BLOCK_STATES) {
            return new ChunkProviderDebug(this.b);
        } else {
            return this.type == WorldType.CUSTOMIZED ? new ChunkProviderGenerate(this.b, this.b.getSeed(), this.b.getWorldData().shouldGenerateMapFeatures(), this.i) : new ChunkProviderGenerate(this.b, this.b.getSeed(), this.b.getWorldData().shouldGenerateMapFeatures(), this.i);
        }
    }

    public boolean canSpawn(int var1, int var2) {
        return this.b.c(new BlockPosition(var1, 0, var2)) == Blocks.GRASS;
    }

    public float a(long var1, float var3) {
        int var4 = (int)(var1 % 24000L);
        float var5 = ((float)var4 + var3) / 24000.0F - 0.25F;
        if (var5 < 0.0F) {
            ++var5;
        }

        if (var5 > 1.0F) {
            --var5;
        }

        float var6 = var5;
        var5 = 1.0F - (float)((Math.cos((double)var5 * Math.PI) + 1.0) / 2.0);
        var5 = var6 + (var5 - var6) / 3.0F;
        return var5;
    }

    public int a(long var1) {
        return (int)(var1 / 24000L % 8L + 8L) % 8;
    }

    public boolean d() {
        return true;
    }

    public boolean e() {
        return true;
    }

    public static WorldProvider byDimension(int var0) {
        if (var0 == -1) {
            return new WorldProviderHell();
        } else if (var0 == 0) {
            return new WorldProviderNormal();
        } else {
            return var0 == 1 ? new WorldProviderTheEnd() : null;
        }
    }

    public BlockPosition h() {
        return null;
    }

    public int getSeaLevel() {
        return this.type == WorldType.FLAT ? 4 : this.b.F() + 1;
    }

    public abstract String getName();

    public abstract String getSuffix();

    public WorldChunkManager m() {
        return this.c;
    }

    public boolean n() {
        return this.d;
    }

    public boolean o() {
        return this.e;
    }

    public float[] p() {
        return this.f;
    }

    public int getDimension() {
        return this.dimension;
    }

    public WorldBorder getWorldBorder() {
        return new WorldBorder();
    }
}
