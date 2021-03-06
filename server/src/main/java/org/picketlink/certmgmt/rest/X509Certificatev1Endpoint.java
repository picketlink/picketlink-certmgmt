/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2013 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.picketlink.certmgmt.rest;

import org.picketlink.Identity;
import org.picketlink.certmgmt.model.MyUser;
import org.picketlink.certmgmt.model.X509Certificatev1CreationRequest;
import org.picketlink.certmgmt.model.X509Certificatev1DetailResponse;
import org.picketlink.certmgmt.model.X509Certificatev1Response;
import org.picketlink.certmgmt.model.enums.ResponseStatus;
import org.picketlink.certmgmt.model.identity.IdentityModelManager;
import org.picketlink.idm.IdentityManager;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Stateless
@Path("/X509v1Certificate")
public class X509Certificatev1Endpoint {

    @Inject
    private IdentityManager identityManager;

    @Inject
    private IdentityModelManager identityModelManager;

    @Inject
    private Identity identity;

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public X509Certificatev1Response create(X509Certificatev1CreationRequest request) throws Exception {
        X509Certificatev1Response response = new X509Certificatev1Response();
        MyUser myUser = (MyUser) this.identity.getAccount();

        // now fix this. user always exists.
        if (myUser == null) {
            // Alias is not already registered
            myUser = this.identityModelManager.createMyUser(request);
            this.identityManager.add(myUser);
            response.setStatus(200);
            response.setResponseStatus(ResponseStatus.CREATED);
        } else {
            // Alias is already registered
            response.setStatus(400);
            response.setResponseStatus(ResponseStatus.FAILED);
        }
        return response;
    }

    @GET
    @Path("/{keyPassword}")
    @Produces(MediaType.APPLICATION_JSON)
    public X509Certificatev1DetailResponse get(@PathParam("keyPassword") String keyPassword) throws InvalidKeyException,
            NoSuchAlgorithmException {
        X509Certificatev1DetailResponse response = new X509Certificatev1DetailResponse();
        // Agent myUser = BasicModel.getAgent(identityManager, keyPassword);
        MyUser myUser = this.identityModelManager.findByKeyPassword(keyPassword, identityManager);
        if (myUser == null) {
            response.setStatus(400);
            response.setResponseStatus(ResponseStatus.FAILED);
            return response;
        }
        response = this.identityModelManager.getMyUser(myUser, keyPassword);
        response.setStatus(200);
        response.setResponseStatus(ResponseStatus.FETCHED);
        return response;
    }

    @POST
    @Path("/update")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public X509Certificatev1Response update(X509Certificatev1CreationRequest request) throws Exception {
        X509Certificatev1Response response = new X509Certificatev1Response();
        MyUser myUser = this.identityModelManager.findByKeyPassword(request.getKeyPassword(), identityManager);
        if (myUser == null) {
            response.setStatus(400);
            response.setResponseStatus(ResponseStatus.FAILED);
        }

        // Alias is registered
        myUser = this.identityModelManager.createMyUser(request);
        this.identityManager.update(myUser);
        response.setStatus(200);
        response.setResponseStatus(ResponseStatus.UPDATED);
        return response;
    }

    @DELETE
    @Path("/{keyPassword}")
    @Produces(MediaType.APPLICATION_JSON)
    public X509Certificatev1Response delete(@PathParam("keyPassword") String keyPassword) throws Exception {
        X509Certificatev1Response response = new X509Certificatev1Response();
        MyUser myUser = this.identityModelManager.findByKeyPassword(keyPassword, identityManager);
        if (myUser == null) {
            response.setStatus(400);
            response.setResponseStatus(ResponseStatus.FAILED);
            return response;
        }
        this.identityManager.remove(myUser);
        response.setStatus(200);
        response.setResponseStatus(ResponseStatus.DELETED);
        return response;
    }
}