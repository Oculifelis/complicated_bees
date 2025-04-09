package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.bees.Comb;
import com.accbdd.complicated_bees.registry.CombRegistration;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class Combs {
    public static final Map<ResourceKey<Comb>, Comb> COMBS = new HashMap<>();

    public static final Comb AMETHYST = comb("amethyst", 0xafad9c, 0xa86df9);
    public static final Comb COAL = comb("coal", 0xafad9c, 0x3f3f3f);
    public static final Comb COPPER = comb("copper", 0xafad9c, 0xd37a5a);
    public static final Comb DIAMOND = comb("diamond", 0xafad9c, 0x79caec);
    public static final Comb DRIPPING = comb("dripping", 0xd68400, 0xfff700);
    public static final Comb DUSTY = comb("dusty", 0xe4d169, 0xccad50);
    public static final Comb EMERALD = comb("emerald", 0xafad9c, 0x3ad261);
    public static final Comb GLOWSTONE = comb("glowstone", 0x652828, 0xcbcd0a);
    public static final Comb GOLD = comb("gold", 0xafad9c, 0xe4d23b);
    public static final Comb HONEY = comb("honey", 0xe7d46a, 0xfea02b);
    public static final Comb IRON = comb("iron", 0xafad9c, 0xdbdbdb);
    public static final Comb LAPIS = comb("lapis", 0xafad9c, 0x1815a2);
    public static final Comb MYSTERIOUS = comb("mysterious", 0xf4ef62, 0xc262f4);
    public static final Comb NETHERITE = comb("netherite", 0x3b3224, 0x3b3224);
    public static final Comb QUARTZ = comb("quartz", 0x652828, 0xc8c4ae);
    public static final Comb REDSTONE = comb("redstone", 0xafad9c, 0xa31300);
    public static final Comb ROCKY = comb("rocky", 0xafad9c, 0xafa87e);
    public static final Comb ROTTEN = comb("rotten", 0x655f00, 0x443507);
    public static final Comb ROYAL = comb("royal",0xa71400, 0xffcc3e);
    public static final Comb SILKY = comb("silky", 0x5a820e, 0xf6ec21);
    public static final Comb SIMMERING = comb("simmering", 0x652828, 0xfa5200);
    public static final Comb SPECTRAL = comb("spectral", 0xfffd68, 0xffffff);
    public static final Comb STRINGY = comb("stringy", 0xe8d880, 0xfcb968);

    private static Comb comb(String path, int outer, int inner) {
        Comb comb = new Comb(outer, inner);
        COMBS.put(ResourceKey.create(CombRegistration.COMB_REGISTRY_KEY, new ResourceLocation(MODID, path)), comb);
        return comb;
    }
}
