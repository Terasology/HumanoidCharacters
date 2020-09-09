// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.characters.humanoid;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.math.DoubleMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.engine.rendering.nui.CoreScreenLayer;
import org.terasology.engine.utilities.Assets;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.nui.Color;
import org.terasology.nui.UIWidget;
import org.terasology.nui.WidgetUtil;
import org.terasology.nui.databinding.DefaultBinding;
import org.terasology.nui.widgets.UIImage;
import org.terasology.nui.widgets.UISlider;

import java.math.RoundingMode;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * Screen displayed for editing a region, all changes are made client side and when the "OK" button is pressed the
 * changed values are sent to the server using various events to alter the region entity on the server's side. The color
 * display of the region utilizes a slider and box to generate the color, the rest are very simplistic widgets(number or
 * text entries or checkboxes)
 */
public class CharacterAppearanceScreen extends CoreScreenLayer {

    private static final Logger logger = LoggerFactory.getLogger(CharacterAppearanceScreen.class);
    private static final List<Color> SKIN_COLORS = Arrays.asList(
            new Color(0xffd19fff),
            new Color(0xe7bd91ff),
            new Color(0x9c7248ff));
    private static final List<Color> EYE_COLORS = Arrays.asList(
            new Color(0x292929ff));
    private static final List<Color> HAIR_COLORS = Arrays.asList(
            new Color(0xd1c270ff),
            new Color(0x292929ff),
            new Color(0x7c5823ff));
    private static final List<Color> SHIRT_COLORS = Arrays.asList(
            new Color(0xe7655cff),
            new Color(0x64d769ff),
            new Color(0x1560bdff),
            new Color(0x73a7c4ff));
    private static final List<Color> PANT_COLORS = Arrays.asList(
            new Color(0x6187d3ff),
            new Color(0xc89459ff),
            new Color(0x1560bdff));
    private static final List<Color> SHOE_COLORS = Arrays.asList(
            new Color(0xf0472fff));
    @In
    EntityManager entityManager;
    private Supplier<Color> skinColorSupplier;
    private Supplier<Color> eyeColorSupplier;
    private Supplier<Color> hairColorSupplier;
    private Supplier<Color> shirtColorSupplier;
    private Supplier<Color> pantColorSupplier;
    private Supplier<Color> shoeColorSupplier;
    @In
    private LocalPlayer localPlayer;

    @Override
    public void initialise() {

        WidgetUtil.trySubscribe(this, "okButton", this::onOkButton);
        WidgetUtil.trySubscribe(this, "cancelButton", this::onCancelButton);
    }

    @Override
    public void onOpened() {
        CharacterAppearanceComponent component =
                localPlayer.getCharacterEntity().getComponent(CharacterAppearanceComponent.class);
        this.skinColorSupplier = initSliderForColorTypeAndReturnGetterForColor("skin", component.skinColor,
                SKIN_COLORS);
        this.eyeColorSupplier = initSliderForColorTypeAndReturnGetterForColor("eye", component.eyeColor, EYE_COLORS);
        this.hairColorSupplier = initSliderForColorTypeAndReturnGetterForColor("hair", component.hairColor,
                HAIR_COLORS);
        this.shirtColorSupplier = initSliderForColorTypeAndReturnGetterForColor("shirt", component.shirtColor,
                SHIRT_COLORS);
        this.pantColorSupplier = initSliderForColorTypeAndReturnGetterForColor("pant", component.pantColor,
                PANT_COLORS);
        this.shoeColorSupplier = initSliderForColorTypeAndReturnGetterForColor("shoe", component.shoeColor,
                SHOE_COLORS);
    }

    Supplier<Color> initSliderForColorTypeAndReturnGetterForColor(String colorType, Color color,
                                                                  List<Color> colorsForSelection) {
        UISlider colorSlider = find(colorType + "ColorSlider", UISlider.class);
        UIImage colorImage = find(colorType + "ColorImage", UIImage.class);
        if (colorImage == null || colorSlider == null) {
            logger.warn("Failed to find either slider and/or image for color of " + colorType);
            return () -> Color.WHITE;
        }

        colorSlider.setIncrement(0.01f);
        Function<Object, String> constant = Functions.constant("  ");   // ensure a certain width
        colorSlider.setLabelFunction(constant);
        ResourceUrn uri = TextureUtil.getTextureUriForColor(Color.WHITE);
        Texture tex = Assets.get(uri, Texture.class).get();
        colorImage.setImage(tex);

        ColorSliderBinding binding = new ColorSliderBinding(colorSlider, colorImage, color, colorsForSelection);
        colorSlider.bindValue(binding);
        return () -> binding.getColor();
    }

    public void onOkButton(UIWidget button) {
        Color skinColor = skinColorSupplier.get();
        Color eyeColor = eyeColorSupplier.get();
        Color hairColor = hairColorSupplier.get();
        Color shirtColor = shirtColorSupplier.get();
        Color pantColor = pantColorSupplier.get();
        Color shoeColor = shoeColorSupplier.get();
        localPlayer.getCharacterEntity().send(new ChangeCharacterAppearanceRequest(skinColor, eyeColor, hairColor,
                shirtColor, pantColor, shoeColor));

        getManager().popScreen();
    }

    public void onCancelButton(UIWidget button) {
        getManager().popScreen();
    }

    @Override
    public boolean isLowerLayerVisible() {
        return false;
    }

    private float findClosestIndex(Color color, List<Color> colors) {
        int best = 0;
        float minDist = Float.MAX_VALUE;
        for (int i = 0; i < colors.size(); i++) {
            Color other = colors.get(i);
            float dr = other.rf() - color.rf();
            float dg = other.gf() - color.gf();
            float db = other.bf() - color.bf();

            // there are certainly smarter ways to measure color distance,
            // but Euclidean distance is good enough for the purpose
            float dist = dr * dr + dg * dg + db * db;
            if (dist < minDist) {
                minDist = dist;
                best = i;
            }
        }

        float max = colors.size() - 1;
        return best / max;
    }

    private Color findClosestColor(float findex, List<Color> colors) {
        int index = DoubleMath.roundToInt(findex * (colors.size() - 1), RoundingMode.HALF_UP);
        Color color = colors.get(index);
        return color;
    }

    private final class ColorSliderBinding extends DefaultBinding<Float> {
        private final UISlider colorSlider;
        private final UIImage colorImage;
        private final List<Color> colorsForSelection;
        private Color color;

        private ColorSliderBinding(UISlider colorSlider, UIImage colorImage, Color color,
                                   List<Color> colorsForSelection) {
            super(findClosestIndex(color, colorsForSelection));
            this.colorImage = colorImage;
            this.colorSlider = colorSlider;
            this.color = color;
            this.colorsForSelection = colorsForSelection;
            updateImage();
        }


        @Override
        public void set(Float v) {
            super.set(v);
            color = findClosestColor(v, colorsForSelection);
            updateImage();
        }

        private void updateImage() {
            colorImage.setTint(color);
        }

        public Color getColor() {
            return color;
        }
    }
}
