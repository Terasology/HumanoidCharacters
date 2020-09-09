// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.characters.humanoid;

import org.junit.Test;
import org.terasology.engine.rendering.assets.texture.TextureData;
import org.terasology.gestalt.assets.ResourceUrn;

import java.awt.Color;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Test for {@link VerticalColorArrayTextureResolver}
 */
public class VerticalColorArrayTextureResolverTest {

    @Test
    public void testStringCanBeResolvedAndReturnsAssetOfCorrectHeight() throws Exception {

        VerticalColorArrayTextureResolver resolver = new VerticalColorArrayTextureResolver();
        Optional<TextureData> result = resolver.getAssetData(new ResourceUrn("HumanoidCharacters:verticalColorArray" +
                "(FF00FF;FFFF00)"));
        TextureData textureData = result.get();
        assertEquals(2, textureData.getHeight());
    }

    @Test
    public void testParseColor() throws Exception {
        assertEquals(new Color(255, 0, 0), VerticalColorArrayTextureResolver.parseColor("FF0000"));
        assertEquals(new Color(0, 255, 0), VerticalColorArrayTextureResolver.parseColor("00FF00"));
        assertEquals(new Color(0, 0, 255), VerticalColorArrayTextureResolver.parseColor("0000FF"));
        assertEquals(new Color(16, 0, 15), VerticalColorArrayTextureResolver.parseColor("10000F"));
    }

}
