package com.amadornes.framez.movement;

import java.util.ArrayList;
import java.util.List;

public enum SpeedUnit {

    SPM(true, true, "s/m", "Seconds per movement", new Converter() {

        @Override
        public double convertFromTPM(double tpm) {

            return tpm / 20D;
        }

        @Override
        public double convertToTPM(double value) {

            return value * 20D;
        }
    }), //
    TPM(true, true, "t/m", "Ticks per movement", new Converter() {

        @Override
        public double convertFromTPM(double tpm) {

            return tpm;
        }

        @Override
        public double convertToTPM(double value) {

            return value;
        }
    }), //
    BPS(true, false, "b/s", "Blocks per second", new Converter() {

        @Override
        public double convertFromTPM(double tpm) {

            return 1D / (tpm / 20D);
        }

        @Override
        public double convertToTPM(double value) {

            return 1D / (value * 20D);
        }
    }), //
    BPT(true, false, "b/t", "Blocks per tick", new Converter() {

        @Override
        public double convertFromTPM(double tpm) {

            return 1D / tpm;
        }

        @Override
        public double convertToTPM(double value) {

            return 1D / value;
        }
    }), //
    RADPS(false, true, "r/s", "Radians per second", new Converter() {

        @Override
        public double convertFromTPM(double tpm) {

            return ((Math.PI / 2D) * 20) / tpm;
        }

        @Override
        public double convertToTPM(double value) {

            return ((Math.PI / 2D) * 20) / value;
        }
    }), //
    RADPT(false, true, "r/t", "Radians per tick", new Converter() {

        @Override
        public double convertFromTPM(double tpm) {

            return (Math.PI / 2D) / tpm;
        }

        @Override
        public double convertToTPM(double value) {

            return (Math.PI / 2D) / value;
        }
    }), //
    DEGPS(false, true, "º/s", "Degrees per second", new Converter() {

        @Override
        public double convertFromTPM(double tpm) {

            return (90D * 20) / tpm;
        }

        @Override
        public double convertToTPM(double value) {

            return (90D * 20) / value;
        }
    }), //
    DEGPT(false, true, "º/t", "Degrees per tick", new Converter() {

        @Override
        public double convertFromTPM(double tpm) {

            return 90D / tpm;
        }

        @Override
        public double convertToTPM(double value) {

            return 90D / value;
        }
    });

    private interface Converter {

        public double convertFromTPM(double tpm);

        public double convertToTPM(double value);
    }

    public static List<SpeedUnit> getSlidingUnits() {

        List<SpeedUnit> l = new ArrayList<SpeedUnit>();

        for (SpeedUnit u : values())
            if (u.supportsSliding())
                l.add(u);

        return l;
    }

    public static List<SpeedUnit> getRotationUnits() {

        List<SpeedUnit> l = new ArrayList<SpeedUnit>();

        for (SpeedUnit u : values())
            if (u.supportsRotation())
                l.add(u);

        return l;
    }

    private boolean slide, rotate;
    private String unit, description;
    private Converter converter;

    private SpeedUnit(boolean slide, boolean rotate, String unit, String description, Converter converter) {

        this.slide = slide;
        this.rotate = rotate;
        this.unit = unit;
        this.description = description;
        this.converter = converter;
    }

    public boolean supportsSliding() {

        return slide;
    }

    public boolean supportsRotation() {

        return rotate;
    }

    public String getUnit() {

        return unit;
    }

    public String getDescription() {

        return description;
    }

    public double convertFromTPM(double tpm) {

        return converter.convertFromTPM(tpm);
    }

    public double convertToTPM(double value) {

        return converter.convertToTPM(value);
    }

}
