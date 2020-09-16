// Copyright 2020 The Terasology Foundation
// SPDX-License-Identifier: Apache-2.0
package org.terasology.humanoidcharacters;

import org.terasology.engine.entitySystem.Component;
import org.terasology.engine.network.FieldReplicateType;
import org.terasology.engine.network.Replicate;

/**
 * When this component is attached to a character a apperance selection dialg will be shown when the player joins.
 */
@Replicate(FieldReplicateType.SERVER_TO_CLIENT)
public class ShowCharacterApperanceDialogComponent implements Component {
}
