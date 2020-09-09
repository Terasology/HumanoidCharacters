// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.characters.humanoid;

import org.terasology.engine.network.NetworkEvent;
import org.terasology.engine.network.ServerEvent;
import org.terasology.nui.Color;

/**
 * Send to character at server when the owner selected colors for it.
 */
@ServerEvent
public class ChangeCharacterAppearanceRequest extends NetworkEvent {
    private Color skinColor;
    private Color eyeColor;
    private Color hairColor;
    private Color shirtColor;
    private Color pantColor;
    private Color shoeColor;

    protected ChangeCharacterAppearanceRequest() {
    }

    public ChangeCharacterAppearanceRequest(Color skinColor, Color eyeColor, Color hairColor, Color shirtColor,
                                            Color pantColor, Color shoeColor) {
        this.skinColor = skinColor;
        this.eyeColor = eyeColor;
        this.hairColor = hairColor;
        this.shirtColor = shirtColor;
        this.pantColor = pantColor;
        this.shoeColor = shoeColor;
    }

    public Color getSkinColor() {
        return skinColor;
    }

    public Color getEyeColor() {
        return eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public Color getShirtColor() {
        return shirtColor;
    }

    public Color getPantColor() {
        return pantColor;
    }

    public Color getShoeColor() {
        return shoeColor;
    }
}
