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

import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;

/**
 * Makes human character appear
 */
@RegisterSystem(RegisterMode.AUTHORITY)
public class CharacterAppearanceServerSystem extends BaseComponentSystem {

    @ReceiveEvent
    public void onCreateDefaultVisualCharacter(ChangeCharacterAppearanceRequest event, EntityRef characterEntity,
                                               CharacterAppearanceComponent characterAppearanceComponent) {
        if (event.getSkinColor() != null) {
            characterAppearanceComponent.skinColor = event.getSkinColor();
        }
        if (event.getEyeColor() != null) {
            characterAppearanceComponent.eyeColor = event.getEyeColor();
        }
        if (event.getHairColor() != null) {
            characterAppearanceComponent.hairColor = event.getHairColor();
        }
        if (event.getPantColor() != null) {
            characterAppearanceComponent.pantColor = event.getPantColor();
        }
        if (event.getShirtColor() != null) {
            characterAppearanceComponent.shirtColor = event.getShirtColor();
        }
        if (event.getShoeColor() != null) {
            characterAppearanceComponent.shoeColor = event.getShoeColor();
        }
        characterEntity.saveComponent(characterAppearanceComponent);
        characterEntity.removeComponent(ShowCharacterAppearanceDialogComponent.class);
    }


}
