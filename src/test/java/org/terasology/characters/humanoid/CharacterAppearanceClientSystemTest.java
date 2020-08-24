// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.characters.humanoid;

import org.junit.jupiter.api.Test;
import org.terasology.nui.Color;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 *
 */
public class CharacterAppearanceClientSystemTest {

    @Test
    public void testRedColorToHex() {
        assertEquals("FF0000", CharacterAppearanceClientSystem.colorToHex(new Color(255, 0, 0)));
    }

    @Test
    public void testGreenColorToHex() {
        assertEquals("00FF00", CharacterAppearanceClientSystem.colorToHex(new Color(0, 255, 0)));
    }

    @Test
    public void testBlueColorToHex() {
        assertEquals("0000FF", CharacterAppearanceClientSystem.colorToHex(new Color(0, 0, 255)));
    }

}
