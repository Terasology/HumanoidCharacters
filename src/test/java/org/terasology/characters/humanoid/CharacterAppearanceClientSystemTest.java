// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.characters.humanoid;

import org.junit.Test;
import org.terasology.nui.Color;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class CharacterAppearanceClientSystemTest {

    @Test
    public void testColorToHex() {
        assertEquals("FF0000", CharacterAppearanceClientSystem.colorToHex(new Color(1.0f, 0.0f, 0.0f)));
        assertEquals("00FF00", CharacterAppearanceClientSystem.colorToHex(new Color(0.0f, 1.0f, 0.0f)));
        assertEquals("0000FF", CharacterAppearanceClientSystem.colorToHex(new Color(0.0f, 0.0f, 1.0f)));
    }
}
