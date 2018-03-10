/*
 * Copyright 2017 MovingBlocks
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
import org.terasology.rendering.nui.Color;

import static org.junit.Assert.assertEquals;

/**
 *
 */
public class HumanoidCharacterSystemTest {

    @Test
    public void testColorToHex() {
        assertEquals("FF0000", HumanoidCharacterSystem.colorToHex(new Color(1.0f, 0.0f, 0.0f)));
        assertEquals("00FF00", HumanoidCharacterSystem.colorToHex(new Color(0.0f, 1.0f, 0.0f)));
        assertEquals("0000FF", HumanoidCharacterSystem.colorToHex(new Color(0.0f, 0.0f, 1.0f)));
    }
}
