/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.unomi.services.conditions.initializers;

import org.apache.unomi.api.EventProperty;
import org.apache.unomi.api.conditions.initializers.ChoiceListInitializer;
import org.apache.unomi.api.conditions.initializers.ChoiceListValue;
import org.apache.unomi.api.services.EventService;

import java.util.ArrayList;
import java.util.List;

/**
 * Initializer for the set of available event properties.
 */
public class EventPropertyChoiceListInitializer implements ChoiceListInitializer {

    EventService eventService;

    public List<ChoiceListValue> getValues(Object context) {
        List<EventProperty> eventProperties = eventService.getEventProperties();
        List<ChoiceListValue> choiceListValues = new ArrayList<>(eventProperties.size());
        for (EventProperty eventProperty : eventProperties) {
            choiceListValues.add(new PropertyTypeChoiceListValue(eventProperty.getId(), eventProperty.getId(), eventProperty.getValueType()));
        }
        return choiceListValues;
    }

    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
