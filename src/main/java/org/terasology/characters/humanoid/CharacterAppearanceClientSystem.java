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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.terasology.assets.management.AssetManager;
import org.terasology.engine.modes.loadProcesses.AwaitedLocalCharacterSpawnEvent;
import org.terasology.entitySystem.entity.EntityBuilder;
import org.terasology.entitySystem.entity.EntityManager;
import org.terasology.entitySystem.entity.EntityRef;
import org.terasology.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.entitySystem.event.EventPriority;
import org.terasology.entitySystem.event.ReceiveEvent;
import org.terasology.entitySystem.systems.BaseComponentSystem;
import org.terasology.entitySystem.systems.RegisterMode;
import org.terasology.entitySystem.systems.RegisterSystem;
import org.terasology.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.logic.characters.StandComponent;
import org.terasology.logic.characters.VisualCharacterComponent;
import org.terasology.logic.characters.WalkComponent;
import org.terasology.logic.characters.events.CreateVisualCharacterEvent;
import org.terasology.logic.console.commandSystem.annotations.Command;
import org.terasology.logic.console.commandSystem.annotations.Sender;
import org.terasology.logic.location.LocationComponent;
import org.terasology.logic.players.LocalPlayer;
import org.terasology.math.geom.Vector3f;
import org.terasology.registry.In;
import org.terasology.rendering.assets.animation.MeshAnimation;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.logic.SkeletalMeshComponent;
import org.terasology.rendering.nui.Color;
import org.terasology.rendering.nui.NUIManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Makes human character appear
 */
@RegisterSystem(RegisterMode.CLIENT)
public class CharacterAppearanceClientSystem extends BaseComponentSystem implements UpdateSubscriberSystem {

    public static final String CONFIG_SCREEN = "CharacterAppearanceScreen";
    private static final float MIN_SPEED_IN_M_PER_S_FOR_WALK_ANIMATION = 1.0f;
    @In
    private AssetManager assetManager;

    @In
    private LocalPlayer localPlayer;

    @In
    private NUIManager nuiManager;

    @In
    private EntityManager entityManager;

    private Map<EntityRef, Vector3f> entityToLastLocationMap = new HashMap<>();



    @ReceiveEvent(priority = EventPriority.PRIORITY_NORMAL)
    public void onCreateDefaultVisualCharacter(CreateVisualCharacterEvent event, EntityRef characterEntity,
                                               CharacterAppearanceComponent characterAppearanceComponent) {
        EntityBuilder entityBuilder = event.getVisualCharacterBuilder();
        entityBuilder.addPrefab("HumanoidCharacters:femaleHuman");
        SkeletalMeshComponent skeletalMeshComponent = entityBuilder.getComponent(SkeletalMeshComponent.class);
        StringBuilder urnBuilder = new StringBuilder();
        urnBuilder.append("HumanoidCharacters:verticalColorArray(");
        urnBuilder.append(colorToHex(characterAppearanceComponent.skinColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.eyeColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.hairColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.shirtColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.pantColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.shoeColor));
        urnBuilder.append(")");

        skeletalMeshComponent.material = assetManager.getAsset(urnBuilder.toString(), Material.class).get();
        entityBuilder.saveComponent(skeletalMeshComponent);
        event.consume();
    }

    @ReceiveEvent
    public void onChangeHumanoidCharacter(OnChangedComponent event, EntityRef characterEntity,
                                               CharacterAppearanceComponent characterAppearanceComponent) {
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
        urnBuilder.append(colorToHex(characterAppearanceComponent.skinColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.eyeColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.hairColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.shirtColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.pantColor));
        urnBuilder.append(";");
        urnBuilder.append(colorToHex(characterAppearanceComponent.shoeColor));
        urnBuilder.append(")");

        skeletalMeshComponent.material = assetManager.getAsset(urnBuilder.toString(), Material.class).get();
        visualCharacter.saveComponent(skeletalMeshComponent);
    }

    static String colorToHex(Color skinColor) {
        return skinColor.toHex().substring(0,6);
    }


    @Command(shortDescription = "Shows character appearance configuration screen",
            helpText = "Shows the screen for configuring the visual character appearance",
            runOnServer = false)
    public String configureCharacterAppearance(@Sender EntityRef sender) {
        showConfigurationScreen();
        return "";
    }

    void showConfigurationScreen() {
        nuiManager.pushScreen(CONFIG_SCREEN);
    }

    @ReceiveEvent(netFilter =  RegisterMode.CLIENT)
    public void onShowCharacterApperanceConfigurationScreenEvent(AwaitedLocalCharacterSpawnEvent event,
                                                                 EntityRef character,
                                                                 ShowCharacterApperanceDialogComponent component) {
        showConfigurationScreen();
    }

    @Override
    public void update(float delta) {
        for (EntityRef characterEntity: entityManager.getEntitiesWith(CharacterAppearanceComponent.class)) {
            updateVisualForCharacterEntity(characterEntity, delta);

        }
    }
    @ReceiveEvent
    public void onMinionDeactivation(BeforeDeactivateComponent event, EntityRef entity,
                                     CharacterAppearanceComponent component) {
        entityToLastLocationMap.remove(entity);
    }

    private void updateVisualForCharacterEntity(EntityRef characterEntity, float elapsedSeconds) {
        LocationComponent locationComponent = characterEntity.getComponent(LocationComponent.class);
        if (locationComponent == null) {
            return;
        }
        VisualCharacterComponent visualCharacterComponent = characterEntity.getComponent(VisualCharacterComponent.class);
        if (visualCharacterComponent == null) {
            return;
        }
        EntityRef visualCharacter = visualCharacterComponent.visualCharacter;
        if (visualCharacter == null || !visualCharacter.isActive()) {
            return;
        }

        WalkComponent walkComponent = visualCharacter.getComponent(WalkComponent.class);
        if (walkComponent == null) {
            return;
        }
        StandComponent standComponent = visualCharacter.getComponent(StandComponent.class);
        if (standComponent == null) {
            return;
        }
        SkeletalMeshComponent skeletalMeshComponent = visualCharacter.getComponent(SkeletalMeshComponent.class);
        if (skeletalMeshComponent == null)  {
            return;
        }
        Vector3f position = locationComponent.getWorldPosition();
        Vector3f lastPosition = entityToLastLocationMap.get(characterEntity);
        entityToLastLocationMap.put(characterEntity, position);

        List<MeshAnimation> wantedAnimationPool;
        float speedInMPerS = lastPosition != null ? position.distance(lastPosition) / elapsedSeconds : 0f;

        if (lastPosition == null || speedInMPerS < MIN_SPEED_IN_M_PER_S_FOR_WALK_ANIMATION) {
            wantedAnimationPool = standComponent.animationPool;
        } else {
            wantedAnimationPool = walkComponent.animationPool;
        }

        if (wantedAnimationPool.equals(skeletalMeshComponent.animationPool)) {
            return;
        }
        skeletalMeshComponent.animation = null;
        skeletalMeshComponent.animationPool.clear();
        skeletalMeshComponent.animationPool.addAll(wantedAnimationPool);
        skeletalMeshComponent.loop = true;
        visualCharacter.saveComponent(skeletalMeshComponent);
    }

}
