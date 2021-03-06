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
package org.picketlink.certmgmt.model.identity;

import org.picketlink.certmgmt.model.MyUser;
import org.picketlink.idm.IdentityManager;
import org.picketlink.idm.query.IdentityQuery;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class IdentityModelUtil {

    public MyUser findUserByKeyPassword(String keyPassword, IdentityManager identityManager) {
        if (keyPassword == null) {
            throw new IllegalArgumentException("Invalid keyPassword.");
        }

        IdentityQuery<MyUser> query = identityManager.createIdentityQuery(MyUser.class);
        List<MyUser> result = query.setParameter(MyUser.KEYPASSWORD, keyPassword).getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

    public MyUser findUserByKeyLoginName(String loginName, IdentityManager identityManager) {
        if (loginName == null) {
            throw new IllegalArgumentException("Invalid loginName.");
        }

        IdentityQuery<MyUser> query = identityManager.createIdentityQuery(MyUser.class);
        List<MyUser> result = query.setParameter(MyUser.LOGIN_NAME, loginName).getResultList();

        if (!result.isEmpty()) {
            return result.get(0);
        }
        return null;
    }

}
