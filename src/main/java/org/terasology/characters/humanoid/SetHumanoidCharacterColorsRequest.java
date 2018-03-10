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

import org.terasology.network.NetworkEvent;
import org.terasology.network.ServerEvent;
import org.terasology.rendering.nui.Color;

/**
 * Send to character at server when the owner selected colors for it.
 */
@ServerEvent
public class SetHumanoidCharacterColorsRequest extends NetworkEvent {
    private Color skinColor;
    private Color eyeColor;
    private Color hairColor;
    private Color shirtColor;
    private Color pantColor;
    private Color shoeColor;

    protected SetHumanoidCharacterColorsRequest() {
    }

    public SetHumanoidCharacterColorsRequest(Color skinColor, Color eyeColor, Color hairColor, Color shirtColor, Color pantColor, Color shoeColor) {
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
