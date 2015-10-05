/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.biz)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.everit.authorization.ri.ecm;

/**
 * Constants of the Authorization component.
 */
public final class AuthorizationConstants {

  public static final String ATTR_PERMISSION_CACHE_TARGET = "permissionCache.target";

  public static final String ATTR_PERMISSION_INHERITANCE_CACHE_TARGET =
      "permissionInheritanceCache.target";

  public static final String ATTR_PROPERTY_MANAGER_TARGET = "propertyManager.target";

  public static final String ATTR_QUERYDSL_SUPPORT_TARGET = "querydslSupport.target";

  public static final String ATTR_RESOURCE_SERVICE_TARGET = "resourceService.target";

  public static final String ATTR_TRANSACTION_PROPAGATOR_TARGET = "transactionHelper.target";

  public static final String DEFAULT_CACHE_TARGET = "(MUST_BE_SET=TO_SOMETHING)";

  public static final String DEFAULT_SERVICE_DESCRIPTION = "Default Authorization";

  public static final String SERVICE_FACTORYPID_AUTHORIZATION =
      "org.everit.authorization.ri.ecm.Authorization";

  private AuthorizationConstants() {
  }
}
