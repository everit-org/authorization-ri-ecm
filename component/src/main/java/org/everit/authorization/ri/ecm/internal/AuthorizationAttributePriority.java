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
package org.everit.authorization.ri.ecm.internal;

/**
 * Constants of Authorization attribute priority.
 */
public final class AuthorizationAttributePriority {

  public static final int P01_SERVICE_DESCRIPTION = 1;

  public static final int P02_QUERYDSL_SUPPORT = 2;

  public static final int P03_PROPERTY_MANAGER = 3;

  public static final int P04_RESOURCE_SERVICE = 4;

  public static final int P05_PERMISSION_CACHE = 5;

  public static final int P06_PERMISSION_INHERITANCE_CACHE = 6;

  public static final int P07_TRANSACTION_PROPAGATOR = 7;

  private AuthorizationAttributePriority() {
  }
}
