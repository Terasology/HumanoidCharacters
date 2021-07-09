// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.characters.humanoid;

import org.terasology.engine.network.FieldReplicateType;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;
import org.terasology.nui.Color;

/**
 * Humanoid character properties
 */
@Replicate(FieldReplicateType.OWNER_TO_SERVER_TO_CLIENT)
public class CharacterAppearanceComponent implements Component{

    @Replicate(FieldReplicateType.OWNER_TO_SERVER_TO_CLIENT)
    public Color skinColor;
    @Replicate(FieldReplicateType.OWNER_TO_SERVER_TO_CLIENT)
    public Color eyeColor;
    @Replicate(FieldReplicateType.OWNER_TO_SERVER_TO_CLIENT)
    public Color hairColor;
    @Replicate(FieldReplicateType.OWNER_TO_SERVER_TO_CLIENT)
    public Color shirtColor;
    @Replicate(FieldReplicateType.OWNER_TO_SERVER_TO_CLIENT)
    public Color pantColor;
    @Replicate(FieldReplicateType.OWNER_TO_SERVER_TO_CLIENT)
    public Color shoeColor;
}
