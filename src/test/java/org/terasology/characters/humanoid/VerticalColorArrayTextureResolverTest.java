/*
 * Copyright 2018 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.characters.humanoid;

import org.junit.Test;
import org.terasology.assets.ResourceUrn;
import org.terasology.rendering.assets.texture.TextureData;

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
        Optional<TextureData> result = resolver.getAssetData(new ResourceUrn("HumanoidCharacters:verticalColorArray(FF00FF;FFFF00)"));
        TextureData textureData = result.get();
        assertEquals(2, textureData.getHeight());
    }

    @Test
    public void testParseColor() throws Exception {
        assertEquals(new Color(255, 0, 0), VerticalColorArrayTextureResolver.parseColor("FF0000"));
        assertEquals(new Color(0, 255, 0), VerticalColorArrayTextureResolver.parseColor("00FF00"));
        assertEquals(new Color(0, 0, 255), VerticalColorArrayTextureResolver.parseColor("0000FF"));
        assertEquals(new Color(16,0, 15), VerticalColorArrayTextureResolver.parseColor("10000F"));
    }

}
