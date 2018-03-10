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

import org.terasology.assets.management.AssetManager;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.logic.characters.VisualCharacterComponent;
import org.terasology.logic.characters.events.CreateVisualCharacterEvent;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.registry.In;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.logic.SkeletalMeshComponent;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.nui.NUIManager;

/**
 * Makes human character appear
 */
@RegisterSystem(RegisterMode.ALWAYS)
public class HumanoidCharacterSystem extends BaseComponentSystem {

    @In
    private AssetManager assetManager;


    @In
    private NUIManager nuiManager;



    @ReceiveEvent(priority = EventPriority.PRIORITY_NORMAL, netFilter =  RegisterMode.CLIENT)
    public void onCreateDefaultVisualCharacter(CreateVisualCharacterEvent event, EntityRef characterEntity,
                                               HumanoidCharacterComponent humanoidCharacterComponent) {
        EntityBuilder entityBuilder = event.getVisualCharacterBuilder();
        entityBuilder.addPrefab("HumanoidCharacters:femaleHuman");
        SkeletalMeshComponent skeletalMeshComponent = entityBuilder.getComponent(SkeletalMeshComponent.class);
        StringBuilder urnBuilder = new StringBuilder();
        urnBuilder.append("HumanoidCharacters:verticalColorArray(");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.skinColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.eyeColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.hairColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.shirtColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.pantColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.shoeColor));
        urnBuilder.append(")");

        skeletalMeshComponent.material = assetManager.getAsset(urnBuilder.toString(), Material.class).get();
        entityBuilder.saveComponent(skeletalMeshComponent);
        event.consume();
    }

    @ReceiveEvent(netFilter =  RegisterMode.CLIENT)
    public void onCreateDefaultVisualCharacter(OnChangedComponent event, EntityRef characterEntity,
                                               HumanoidCharacterComponent humanoidCharacterComponent) {
        VisualCharacterComponent visualCharacterComponent = characterEntity.getComponent(VisualCharacterComponent.class);
        if (visualCharacterComponent == null) {
            return;
        }
        EntityRef visualCharacter = visualCharacterComponent.visualCharacter;
        SkeletalMeshComponent skeletalMeshComponent = visualCharacter.getComponent(SkeletalMeshComponent.class);
        if (skeletalMeshComponent == null) {
            return;
        }
        StringBuilder urnBuilder = new StringBuilder();
        urnBuilder.append("HumanoidCharacters:verticalColorArray(");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.skinColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.eyeColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.hairColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.shirtColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.pantColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(humanoidCharacterComponent.shoeColor));
        urnBuilder.append(")");

        skeletalMeshComponent.material = assetManager.getAsset(urnBuilder.toString(), Material.class).get();
        visualCharacter.saveComponent(skeletalMeshComponent);
    }

    @ReceiveEvent(netFilter =  RegisterMode.AUTHORITY)
    public void onCreateDefaultVisualCharacter(SetHumanoidCharacterColorsRequest event, EntityRef characterEntity,
                                               HumanoidCharacterComponent humanoidCharacterComponent) {
        if (event.getSkinColor() != null) {
            humanoidCharacterComponent.skinColor = event.getSkinColor();
        }
        if (event.getEyeColor() != null) {
            humanoidCharacterComponent.eyeColor = event.getEyeColor();
        }
        if (event.getHairColor() != null) {
            humanoidCharacterComponent.hairColor = event.getHairColor();
        }
        if (event.getPantColor() != null) {
            humanoidCharacterComponent.pantColor = event.getPantColor();
        }
        if (event.getShirtColor() != null) {
            humanoidCharacterComponent.shirtColor = event.getShirtColor();
        }
        if (event.getShoeColor() != null) {
            humanoidCharacterComponent.shoeColor = event.getShoeColor();
        }
        characterEntity.saveComponent(humanoidCharacterComponent);
    }

    static String colorToHex(Color skinColor) {
        return skinColor.toHex().substring(0,6);
    }


    @Command(shortDescription = "Shows character appearance configuration screen",
            helpText = "Shows the screen for configuring the visual character appearance",
            runOnServer = false)
    public String configureCharacterAppearance(@Sender EntityRef sender) {
        nuiManager.pushScreen("ConfigureHumanoidCharacterScreen");
        return "";
    }
}
