package com.hidoni.customizableelytra.util;

import java.util.function.Function;

public class ElytraCustomizationData
{
    public final CustomizationType type;
    public final CustomizationHandler handler;

    public ElytraCustomizationData(CustomizationType type, CustomizationHandler handler)
    {
        this.type = type;
        this.handler = handler;
    }

    public enum CustomizationType
    {
        None,
        Dye,
        Banner,
        Split
    }
}
