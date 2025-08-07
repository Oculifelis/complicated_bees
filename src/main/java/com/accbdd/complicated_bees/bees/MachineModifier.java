package com.accbdd.complicated_bees.bees;

public class MachineModifier {
    private final float speedMod; //how fast the machine goes
    private final float efficiencyMod; //how much energy the machine uses
    private final float outputMod; //bonus to products
    private final int processingMod; //how many items the machine processes

    public MachineModifier(float speedMod, float efficiencyMod, float outputMod, int processingMod) {
        this.speedMod = speedMod;
        this.efficiencyMod = efficiencyMod;
        this.outputMod = outputMod;
        this.processingMod = processingMod;
    }

    public static MachineModifier of(MachineModifier... modifiers) {
        float speedMod = 1;
        float efficiencyMod = 1;
        float outputMod = 1;
        int processingMod = 1;
        for (MachineModifier modifier : modifiers) {
            speedMod *= modifier.speedMod;
            efficiencyMod *= modifier.efficiencyMod;
            outputMod *= modifier.outputMod;
            processingMod += modifier.processingMod;
        }

        return new MachineModifier(speedMod, efficiencyMod, outputMod, processingMod);
    }

    public float getEfficiencyMod() {
        return efficiencyMod;
    }

    public float getOutputMod() {
        return outputMod;
    }

    public float getSpeedMod() {
        return speedMod;
    }

    public int getProcessingMod() {
        return processingMod;
    }

    public static class Builder {
        private float speedMod = 1;
        private float efficiencyMod = 1;
        private float outputMod = 1;
        private int processingMod = 1;

        public MachineModifier build() {
            return new MachineModifier(speedMod, efficiencyMod, outputMod, processingMod);
        }

        public Builder speed(float mod) {
            this.speedMod = mod;
            return this;
        }

        public Builder efficiency(float mod) {
            this.efficiencyMod = mod;
            return this;
        }

        public Builder output(float mod) {
            this.outputMod = mod;
            return this;
        }

        public Builder processing(int mod) {
            this.processingMod = mod;
            return this;
        }
    }
}
