// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.humanoidcharacters;

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
        characterEntity.removeComponent(ShowCharacterApperanceDialogComponent.class);
    }


}
