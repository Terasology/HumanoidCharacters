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

import com.google.common.collect.ImmutableSet;
import org.terasology.gestalt.assets.AssetDataProducer;
import org.terasology.gestalt.assets.ResourceUrn;
import org.terasology.engine.rendering.assets.material.MaterialData;
import org.terasology.engine.rendering.assets.shader.Shader;
import org.terasology.engine.rendering.assets.texture.Texture;
import org.terasology.gestalt.assets.management.AssetManager;
import org.terasology.gestalt.assets.module.annotations.RegisterAssetDataProducer;
import org.terasology.gestalt.naming.Name;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;
import java.util.Set;

/**
 *
 */
@RegisterAssetDataProducer
public class VerticalColorArrayMaterialResolver implements AssetDataProducer<MaterialData> {
    private static final Name HUMANOID_CHARACTERS_MODULE = new Name("HumanoidCharacters");
    private static final String PREFIX = "verticalColorArray(";
    private static final String LOWER_CASE_PREFIX = PREFIX.toLowerCase();
    private static final String LOWER_CASE_SUFFIX= ")";


    private AssetManager assetManager;

    public VerticalColorArrayMaterialResolver(AssetManager assetManager) {
        this.assetManager = assetManager;
    }


    @Override
    public Set<ResourceUrn> getAvailableAssetUrns() {
        return Collections.emptySet();
    }

    @Override
    public Set<Name> getModulesProviding(Name resourceName) {
        if (!resourceName.toLowerCase().startsWith(LOWER_CASE_PREFIX)) {
            return Collections.emptySet();
        }
        return ImmutableSet.of(HUMANOID_CHARACTERS_MODULE);
    }

    @Override
    public ResourceUrn redirect(ResourceUrn urn) {
        return urn;
    }

    @Override
    public Optional<MaterialData> getAssetData(ResourceUrn urn) throws IOException {
        final String lowerCaseAssetName = urn.getResourceName().toString().toLowerCase();
        if (!HUMANOID_CHARACTERS_MODULE.equals(urn.getModuleName())
                || !lowerCaseAssetName.startsWith(LOWER_CASE_PREFIX)
                || !lowerCaseAssetName.endsWith(LOWER_CASE_SUFFIX)) {
            return Optional.empty();
        }
        String argument = lowerCaseAssetName.substring(LOWER_CASE_PREFIX.length(),
                lowerCaseAssetName.length() - LOWER_CASE_SUFFIX.length());



        Optional<Texture> foundTexture = assetManager.getAsset(urn, Texture.class);
        if (!foundTexture.isPresent()) {
            return Optional.empty();
        }
        Texture texture = foundTexture.get();

        Optional<Shader> optionalShader = assetManager.getAsset("engine:genericMeshMaterial!instance", Shader.class);
        if (!optionalShader.isPresent()) {
            return Optional.empty();
        }

        MaterialData materialData = new MaterialData(optionalShader.get());
        materialData.setParam("diffuse",texture);
        materialData.setParam("colorOffset",new float[]{1.0f, 1.0f, 1.0f});
        materialData.setParam("textured", true);
        return Optional.of(materialData);

    }
}
