package dev.lugami.spigot.knockback;

import dev.lugami.spigot.KatsuSpigot;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CraftKnockbackProfile implements KnockbackProfile {

    private String name;
    private double friction = 2.0D;
    private double horizontal = 0.35D;
    private double vertical = 0.35D;
    private double verticalLimit = 0.4D;
    private double extraHorizontal = 0.425D;
    private double extraVertical = 0.085D;

    public CraftKnockbackProfile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String[] getValues() {
        return new String[]{
                "Friction: " + this.friction,
                "Horizontal: " + this.horizontal,
                "Vertical: " + this.vertical,
                "Vertical Limit: " + this.verticalLimit,
                "Extra Horizontal: " + this.extraHorizontal,
                "Extra Vertical: " + this.extraVertical,
        };
    }

    public void save() {
        final String path = "knockback.profiles." + this.name;

        KatsuSpigot.INSTANCE.getConfig().set(path + ".friction", this.friction);
        KatsuSpigot.INSTANCE.getConfig().set(path + ".horizontal", this.horizontal);
        KatsuSpigot.INSTANCE.getConfig().set(path + ".vertical", this.vertical);
        KatsuSpigot.INSTANCE.getConfig().set(path + ".vertical-limit", this.verticalLimit);
        KatsuSpigot.INSTANCE.getConfig().set(path + ".extra-horizontal", this.extraHorizontal);
        KatsuSpigot.INSTANCE.getConfig().set(path + ".extra-vertical", this.extraVertical);
        KatsuSpigot.INSTANCE.getConfig().save();
    }

}
