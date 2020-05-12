/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.io.homekit.internal.accessories;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.eclipse.smarthome.core.items.GenericItem;
import org.eclipse.smarthome.core.library.types.OnOffType;
import org.eclipse.smarthome.core.library.types.OpenClosedType;
import org.openhab.io.homekit.internal.HomekitAccessoryUpdater;
import org.openhab.io.homekit.internal.HomekitCharacteristicType;
import org.openhab.io.homekit.internal.HomekitSettings;
import org.openhab.io.homekit.internal.HomekitTaggedItem;

import io.github.hapjava.accessories.SmokeSensorAccessory;
import io.github.hapjava.characteristics.HomekitCharacteristicChangeCallback;
import io.github.hapjava.characteristics.impl.smokesensor.SmokeDetectedStateEnum;
import io.github.hapjava.services.impl.SmokeSensorService;

/**
 *
 * @author Cody Cutrer - Initial contribution
 */
public class HomekitSmokeSensorImpl extends AbstractHomekitAccessoryImpl implements SmokeSensorAccessory {

    private final BooleanItemReader smokeDetectedReader;

    public HomekitSmokeSensorImpl(HomekitTaggedItem taggedItem, List<HomekitTaggedItem> mandatoryCharacteristics,
            HomekitAccessoryUpdater updater, HomekitSettings settings) throws IncompleteAccessoryException {
        super(taggedItem, mandatoryCharacteristics, updater, settings);
        this.smokeDetectedReader = new BooleanItemReader(
                getItem(HomekitCharacteristicType.SMOKE_DETECTED_STATE, GenericItem.class), OnOffType.ON,
                OpenClosedType.OPEN);
        this.getServices().add(new SmokeSensorService(this));
    }

    @Override
    public CompletableFuture<SmokeDetectedStateEnum> getSmokeDetectedState() {
        Boolean state = this.smokeDetectedReader.getValue();
        return CompletableFuture.completedFuture(
                (state != null && state) ? SmokeDetectedStateEnum.DETECTED : SmokeDetectedStateEnum.NOT_DETECTED);
    }

    @Override
    public void subscribeSmokeDetectedState(HomekitCharacteristicChangeCallback callback) {
        subscribe(HomekitCharacteristicType.SMOKE_DETECTED_STATE, callback);
    }

    @Override
    public void unsubscribeSmokeDetectedState() {
        unsubscribe(HomekitCharacteristicType.SMOKE_DETECTED_STATE);
    }
}