// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.humanoidcharacters;

import org.terasology.engine.core.modes.loadProcesses.AwaitedLocalCharacterSpawnEvent;
import org.terasology.engine.entitySystem.entity.EntityBuilder;
import org.terasology.engine.entitySystem.entity.EntityManager;
import org.terasology.engine.entitySystem.entity.EntityRef;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.BeforeDeactivateComponent;
import org.terasology.engine.entitySystem.entity.lifecycleEvents.OnChangedComponent;
import org.terasology.engine.entitySystem.event.EventPriority;
import org.terasology.engine.entitySystem.event.ReceiveEvent;
import org.terasology.engine.entitySystem.systems.BaseComponentSystem;
import org.terasology.engine.entitySystem.systems.RegisterMode;
import org.terasology.engine.entitySystem.systems.RegisterSystem;
import org.terasology.engine.entitySystem.systems.UpdateSubscriberSystem;
import org.terasology.engine.logic.characters.StandComponent;
import org.terasology.engine.logic.characters.VisualCharacterComponent;
import org.terasology.engine.logic.characters.WalkComponent;
import org.terasology.engine.logic.characters.events.CreateVisualCharacterEvent;
import org.terasology.engine.logic.console.commandSystem.annotations.Command;
import org.terasology.engine.logic.console.commandSystem.annotations.Sender;
import org.terasology.engine.logic.location.LocationComponent;
import org.terasology.engine.logic.players.LocalPlayer;
import org.terasology.engine.registry.In;
import org.terasology.engine.rendering.assets.animation.MeshAnimation;
import org.terasology.engine.rendering.assets.material.Material;
import org.terasology.engine.rendering.logic.SkeletalMeshComponent;
import org.terasology.engine.rendering.nui.NUIManager;
import org.terasology.gestalt.assets.management.AssetManager;
import org.terasology.math.geom.Vector3f;
import org.terasology.nui.Color;

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
    private final Map<EntityRef, Vector3f> entityToLastLocationMap = new HashMap<>();
    @In
    private AssetManager assetManager;
    @In
    private LocalPlayer localPlayer;
    @In
    private NUIManager nuiManager;
    @In
    private EntityManager entityManager;

    static String colorToHex(Color skinColor) {
        return skinColor.toHex().substring(0, 6);
    }

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
        VisualCharacterComponent visualCharacterComponent =
                characterEntity.getComponent(VisualCharacterComponent.class);
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

    @ReceiveEvent(netFilter = RegisterMode.CLIENT)
    public void onShowCharacterApperanceConfigurationScreenEvent(AwaitedLocalCharacterSpawnEvent event,
                                                                 EntityRef character,
                                                                 ShowCharacterApperanceDialogComponent component) {
        showConfigurationScreen();
    }

    @Override
    public void update(float delta) {
        for (EntityRef characterEntity : entityManager.getEntitiesWith(CharacterAppearanceComponent.class)) {
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
        VisualCharacterComponent visualCharacterComponent =
                characterEntity.getComponent(VisualCharacterComponent.class);
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
        if (skeletalMeshComponent == null) {
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
