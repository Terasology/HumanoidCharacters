// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.humanoidcharacters;

import com.google.common.collect.ImmutableSet;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.engine.rendering.assets.texture.TextureData;
import org.terasology.engine.rendering.assets.texture.TextureUtil;
import org.terasology.gestalt.assets.AssetDataProducer;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.gestalt.assets.module.annotations.RegisterAssetDataProducer;
import org.terasology.gestalt.naming.Name;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
@RegisterAssetDataProducer
public class VerticalColorArrayTextureResolver implements AssetDataProducer<TextureData> {
    private static final Name HUMANOID_CHARACTERS_MODULE = new Name("HumanoidCharacters");
    private static final String PREFIX = "verticalColorArray(";
    private static final String LOWER_CASE_PREFIX = PREFIX.toLowerCase();
    private static final String LOWER_CASE_SUFFIX = ")";
    private static final int RED_START_INDEX = 0;
    private static final int RED_EXCLUSIVE_END_INDEX = 2;
    private static final int GREEN_START_INDEX = 2;
    private static final int GREEN_EXCLUSIVE_END_INDEX = 4;
    private static final int BLUE_START_INDEX = 4;
    private static final int BLUE_EXCLUSIVE_END_INDEX = 6;
    private static final int COLOR_STRING_LENGTH = 6;

    static Color parseColor(String colorString1) throws IOException {
        String colorString = colorString1;
        if (colorString.length() != COLOR_STRING_LENGTH) {
            throw new IOException("Not a 6 hex digit color string: " + colorString);
        }
        String redString = colorString.substring(RED_START_INDEX, RED_EXCLUSIVE_END_INDEX);
        String greenString = colorString.substring(GREEN_START_INDEX, GREEN_EXCLUSIVE_END_INDEX);
        String blueString = colorString.substring(BLUE_START_INDEX, BLUE_EXCLUSIVE_END_INDEX);
        int red = Integer.parseInt(redString, 16);
        int green = Integer.parseInt(greenString, 16);
        int blue = Integer.parseInt(blueString, 16);
        return new Color(red, green, blue);
    }

    @Override
    public Set<ResourceUrn> getAvailableAssetUrns() {
        return Collections.emptySet();
    }

    @Override
    public Set<Name> getModulesProviding(Name resourceName) {
        if (!resourceName.toLowerCase().startsWith(LOWER_CASE_PREFIX)) {
            return Collections.emptySet();
        }
        return ImmutableSet.of(HUMANOID_CHARACTERS_MODULE);
    }

    @Override
    public ResourceUrn redirect(ResourceUrn urn) {
        return urn;
    }

    @Override
    public Optional<TextureData> getAssetData(ResourceUrn urn) throws IOException {
        final String lowerCaseAssetName = urn.getResourceName().toString().toLowerCase();
        if (!HUMANOID_CHARACTERS_MODULE.equals(urn.getModuleName())
                || !lowerCaseAssetName.startsWith(LOWER_CASE_PREFIX)
                || !lowerCaseAssetName.endsWith(LOWER_CASE_SUFFIX)) {
            return Optional.empty();
        }
        String colorArrayString = lowerCaseAssetName.substring(LOWER_CASE_PREFIX.length(),
                lowerCaseAssetName.length() - LOWER_CASE_SUFFIX.length());
        String[] colorStrings = colorArrayString.split(";");

        int imageSize = colorStrings.length;
        BufferedImage resultImage = new BufferedImage(imageSize, imageSize, BufferedImage.TYPE_INT_ARGB);


        for (int y = 0; y < imageSize; y++) {
            Color color = parseColor(colorStrings[y]);
            int colorAsInt = color.getRGB();
            for (int x = 0; x < imageSize; x++) {
                resultImage.setRGB(x, y, colorAsInt);
            }
        }


        final ByteBuffer byteBuffer = TextureUtil.convertToByteBuffer(resultImage);
        return Optional.of(new TextureData(resultImage.getWidth(), resultImage.getHeight(),
                new ByteBuffer[]{byteBuffer}, Texture.WrapMode.REPEAT, Texture.FilterMode.NEAREST));
    }
}
