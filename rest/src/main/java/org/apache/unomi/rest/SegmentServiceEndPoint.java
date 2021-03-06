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

package org.apache.unomi.rest;

import org.apache.cxf.rs.security.cors.CrossOriginResourceSharing;
import org.apache.unomi.api.Metadata;
import org.apache.unomi.api.PartialList;
import org.apache.unomi.api.Profile;
import org.apache.unomi.api.query.Query;
import org.apache.unomi.api.segments.Segment;
import org.apache.unomi.api.services.SegmentService;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * A JAX-RS endpoint to manage {@link Segment}s.
 */
@WebService
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOriginResourceSharing(
        allowAllOrigins = true,
        allowCredentials = true
)
public class SegmentServiceEndPoint {

    private SegmentService segmentService;

    public SegmentServiceEndPoint() {
        System.out.println("Initializing segment service endpoint...");
    }

    @WebMethod(exclude=true)
    public void setSegmentService(SegmentService segmentService) {
        this.segmentService = segmentService;
    }

    /**
     * Retrieves a list of profiles matching the conditions defined by the segment identified by the specified identifier, ordered according to the specified {@code sortBy}
     * String and and paged: only {@code size} of them are retrieved, starting with the {@code offset}-th one.
     *
     * @param segmentId the identifier of the segment for which we want to retrieve matching profiles
     * @param offset    zero or a positive integer specifying the position of the first element in the total ordered collection of matching elements
     * @param size      a positive integer specifying how many matching elements should be retrieved or {@code -1} if all of them should be retrieved
     * @param sortBy    an optional ({@code null} if no sorting is required) String of comma ({@code ,}) separated property names on which ordering should be performed, ordering
     *                  elements according to the property order in the
     *                  String, considering each in turn and moving on to the next one in case of equality of all preceding ones. Each property name is optionally followed by
     *                  a column ({@code :}) and an order specifier: {@code asc} or {@code desc}.
     * @return a {@link PartialList} of profiles matching the specified segment
     */
    @GET
    @Path("/{segmentID}/match")
    public PartialList<Profile> getMatchingIndividuals(@PathParam("segmentID") String segmentId, @QueryParam("offset") @DefaultValue("0") int offset, @QueryParam("size") @DefaultValue("50") int size, @QueryParam("sort") String sortBy) {
        return segmentService.getMatchingIndividuals(segmentId, offset, size, sortBy);
    }

    /**
     * Retrieves the number of profiles matching the conditions defined by the segment identified by the specified identifier.
     *
     * @param segmentId the identifier of the segment for which we want to retrieve matching profiles
     * @return the number of profiles matching the conditions defined by the segment identified by the specified identifier
     */
    @GET
    @Path("/{segmentID}/count")
    public long getMatchingIndividualsCount(@PathParam("segmentID") String segmentId) {
        return segmentService.getMatchingIndividualsCount(segmentId);
    }

    /**
     * Determines whether the specified profile is part of the segment identified by the specified identifier.
     *
     * @param profile   the profile we want to check
     * @param segmentId the identifier of the segment against which we want to check the profile
     * @return {@code true} if the specified profile is in the specified segment, {@code false} otherwise
     */
    @GET
    @Path("/{segmentID}/match/{profile}")
    public Boolean isProfileInSegment(@PathParam("profile") Profile profile, @PathParam("segmentID") String segmentId) {
        return segmentService.isProfileInSegment(profile, segmentId);
    }

    /**
     * Retrieves the 50 first segment metadatas.
     *
     * @return a List of the 50 first segment metadata
     */
    @GET
    @Path("/")
    public List<Metadata> getSegmentMetadatas() {
        return segmentService.getSegmentMetadatas(0, 50, null).getList();
    }

    /**
     * Retrieves the list of segment metadata of segments depending on the segment identified by the specified identifier. A segment is depending on another one if it includes
     * that segment as part of its condition for profile matching.
     *
     * TODO: rename?
     *
     * @param segmentId the identifier of the segment which impact we want to evaluate
     * @return a list of metadata of segments depending on the specified segment
     */
    @GET
    @Path("/{segmentID}/impacted")
    public List<Metadata> getSegmentImpacted(@PathParam("segmentID") String segmentId) {
        return segmentService.getImpactedSegmentMetadata(segmentId);
    }

    /**
     * Persists the specified segment in the context server.
     *
     * @param segment the segment to be persisted
     */
    @POST
    @Path("/")
    public void setSegmentDefinition(Segment segment) {
        segmentService.setSegmentDefinition(segment);
    }

    /**
     * Retrieves the metadata for segments matching the specified {@link Query}.
     *
     * @param query the query that the segments must match for their metadata to be retrieved
     * @return a {@link PartialList} of segment metadata
     */
    @POST
    @Path("/query")
    public PartialList<Metadata> getListMetadatas(Query query) {
        return segmentService.getSegmentMetadatas(query);
    }

    /**
     * Retrieves the segment identified by the specified identifier.
     *
     * @param segmentId the identifier of the segment to be retrieved
     * @return the segment identified by the specified identifier or {@code null} if no such segment exists
     */
    @GET
    @Path("/{segmentID}")
    public Segment getSegmentDefinition(@PathParam("segmentID") String segmentId) {
        return segmentService.getSegmentDefinition(segmentId);
    }

    /**
     * Removes the segment definition identified by the specified identifier. We can specify that we want the operation to be validated beforehand so that we can
     * know if any other segment that might use the segment we're trying to delete as a condition might be impacted. If {@code validate} is set to {@code false}, no
     * validation is performed. If set to {@code true}, we will first check if any segment depends on the one we're trying to delete and if so we will not delete the
     * segment but rather return the list of the metadata of the impacted segments. If no dependents are found, then we properly delete the segment.
     *
     * @param segmentId the identifier of the segment we want to delete
     * @param validate  whether or not to perform validation
     * @return a list of impacted segment metadata if any or an empty list if no such impacted segments are found or validation was skipped
     */
    @DELETE
    @Path("/{segmentID}")
    public List<Metadata> removeSegmentDefinition(@PathParam("segmentID") String segmentId, @QueryParam("validate") boolean validate) {
        return segmentService.removeSegmentDefinition(segmentId, validate);
    }

    /**
     * TODO: remove
     *
     * @deprecated not needed anymore
     */
    @GET
    @Path("/resetQueries")
    public void resetQueries() {
        for (Metadata metadata : segmentService.getSegmentMetadatas(0, 50, null).getList()) {
            Segment s = segmentService.getSegmentDefinition(metadata.getId());
            segmentService.setSegmentDefinition(s);
        }
    }

}
