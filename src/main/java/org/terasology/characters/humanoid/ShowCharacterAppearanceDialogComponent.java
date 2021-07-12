// Copyright 2021 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.characters.humanoid;

import org.terasology.engine.network.FieldReplicateType;
import org.terasology.engine.network.Replicate;
import org.terasology.gestalt.entitysystem.component.Component;

/**
 * When this component is attached to a character a appearance selection dialog will be shown when the player joins.
 */
@Replicate(FieldReplicateType.SERVER_TO_CLIENT)
public class ShowCharacterAppearanceDialogComponent implements Component<ShowCharacterAppearanceDialogComponent> {
    @Override
    public void copy(ShowCharacterAppearanceDialogComponent other) {
    }
}
