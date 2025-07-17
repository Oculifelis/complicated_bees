package com.accbdd.complicated_bees.datagen;

import com.accbdd.complicated_bees.client.BeeModel;
import com.accbdd.complicated_bees.registry.ItemsRegistration;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.generators.ModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

import static com.accbdd.complicated_bees.ComplicatedBees.MODID;

public class BeeModelGenerator extends ModelProvider<BeeModel.Builder> {
    public BeeModelGenerator(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, MODID, ITEM_FOLDER, BeeModel.Builder::new, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        ResourceLocation[] bees = {
                ItemsRegistration.PRINCESS.getId(),
                ItemsRegistration.QUEEN.getId(),
                ItemsRegistration.DRONE.getId()
        };

        for (int i = 0; i < bees.length; i++) {
            getBuilder(bees[i].toString());
        }
    }

    @Override
    public String getName() {
        return "Bee Models: " + modid;
    }
}
