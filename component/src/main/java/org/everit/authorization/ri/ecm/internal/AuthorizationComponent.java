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

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentMap;

import org.everit.authorization.AuthorizationManager;
import org.everit.authorization.PermissionChecker;
import org.everit.authorization.qdsl.util.AuthorizationQdslUtil;
import org.everit.authorization.ri.AuthorizationImpl;
import org.everit.authorization.ri.ecm.AuthorizationConstants;
import org.everit.osgi.ecm.annotation.Activate;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Deactivate;
import org.everit.osgi.ecm.annotation.ManualService;
import org.everit.osgi.ecm.annotation.ManualServices;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.component.ComponentContext;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.querydsl.support.QuerydslSupport;
import org.everit.props.PropertyManager;
import org.everit.resource.ResourceService;
import org.everit.transaction.propagator.TransactionPropagator;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;

/**
 * ECM component for {@link AuthorizationManager}, {@link PermissionChecker} and
 * {@link AuthorizationQdslUtil} interface based on {@link AuthorizationImpl}.
 */
@ExtendComponent
@Component(componentId = AuthorizationConstants.SERVICE_FACTORYPID_AUTHORIZATION,
    configurationPolicy = ConfigurationPolicy.FACTORY, label = "Everit Authorization RI",
    description = "Component that registers a PermissionChecker, AuthorizationManager and "
        + "AuthorizationQdslUtil OSGi services. The component uses cache to speed up the results "
        + "that are provided by PermissionChecker.")
@StringAttributes({
    @StringAttribute(attributeId = Constants.SERVICE_DESCRIPTION,
        defaultValue = AuthorizationConstants.DEFAULT_SERVICE_DESCRIPTION,
        priority = AuthorizationAttributePriority.P01_SERVICE_DESCRIPTION,
        label = "Service Description",
        description = "The description of this component configuration. It is used to easily "
            + "identify the service registered by this component.") })
@ManualServices(@ManualService({
    AuthorizationManager.class, PermissionChecker.class, AuthorizationQdslUtil.class }))
public class AuthorizationComponent {

  private ConcurrentMap<String, Boolean> permissionCache;

  private ConcurrentMap<Long, long[]> permissionInheritanceCache;

  private PropertyManager propertyManager;

  private QuerydslSupport querydslSupport;

  private ResourceService resourceService;

  private ServiceRegistration<?> serviceRegistration;

  private TransactionPropagator transactionPropagator;

  /**
   * Activate method of component.
   */
  @Activate
  public void activate(final ComponentContext<AuthorizationComponent> componentContext) {
    AuthorizationImpl authorizationImpl =
        new AuthorizationImpl(this.propertyManager, this.resourceService,
            this.transactionPropagator, this.querydslSupport, this.permissionCache,
            this.permissionInheritanceCache);

    Dictionary<String, Object> serviceProperties =
        new Hashtable<>(componentContext.getProperties());
    this.serviceRegistration =
        componentContext.registerService(new String[] { AuthorizationManager.class.getName(),
            PermissionChecker.class.getName(), AuthorizationQdslUtil.class.getName() },
            authorizationImpl,
            serviceProperties);
  }

  /**
   * Component deactivate method.
   */
  @Deactivate
  public void deactivate() {
    if (this.serviceRegistration != null) {
      this.serviceRegistration.unregister();
    }
  }

  @ServiceRef(attributeId = AuthorizationConstants.ATTR_PERMISSION_CACHE_TARGET,
      defaultValue = AuthorizationConstants.DEFAULT_CACHE_TARGET,
      attributePriority = AuthorizationAttributePriority.P05_PERMISSION_CACHE,
      label = "Permission Cache Configuration target",
      description = "OSGi service filter to identify the cache (java.util.concurrent.ConcurrentMap)"
          + " that stores the permission records.")
  public void setPermissionCache(final ConcurrentMap<String, Boolean> permissionCache) {
    this.permissionCache = permissionCache;
  }

  @ServiceRef(attributeId = AuthorizationConstants.ATTR_PERMISSION_INHERITANCE_CACHE_TARGET,
      defaultValue = AuthorizationConstants.DEFAULT_CACHE_TARGET,
      attributePriority = AuthorizationAttributePriority.P06_PERMISSION_INHERITANCE_CACHE,
      label = "Permission Inheritance Cache Configuration target",
      description = "OSGi service filter to identify the cache (java.util.concurrent.ConcurrentMap)"
          + " that stores the permission inheritance records.")
  public void setPermissionInheritanceCache(
      final ConcurrentMap<Long, long[]> permissionInheritanceCache) {
    this.permissionInheritanceCache = permissionInheritanceCache;
  }

  @ServiceRef(attributeId = AuthorizationConstants.ATTR_PROPERTY_MANAGER_TARGET, defaultValue = "",
      attributePriority = AuthorizationAttributePriority.P03_PROPERTY_MANAGER,
      label = "Property Manager target",
      description = "OSGi service filter to identify Property Manager")
  public void setPropertyManager(final PropertyManager propertyManager) {
    this.propertyManager = propertyManager;
  }

  @ServiceRef(attributeId = AuthorizationConstants.ATTR_QUERYDSL_SUPPORT_TARGET, defaultValue = "",
      attributePriority = AuthorizationAttributePriority.P02_QUERYDSL_SUPPORT,
      label = "QuerydslSupport target",
      description = "OSGi service filter to identify the QuerydslSupport service.")
  public void setQuerydslSupport(final QuerydslSupport querydslSupport) {
    this.querydslSupport = querydslSupport;
  }

  @ServiceRef(attributeId = AuthorizationConstants.ATTR_RESOURCE_SERVICE_TARGET, defaultValue = "",
      attributePriority = AuthorizationAttributePriority.P04_RESOURCE_SERVICE,
      label = "ResourceService target",
      description = "OSGi service filter to identify the ResourceService service.")
  public void setResourceService(final ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  @ServiceRef(attributeId = AuthorizationConstants.ATTR_TRANSACTION_PROPAGATOR_TARGET,
      defaultValue = "",
      attributePriority = AuthorizationAttributePriority.P07_TRANSACTION_PROPAGATOR,
      label = "Transaction Propagator target",
      description = "OSGi service filter to identify the TransactionPropagator service.")
  public void setTransactionPropagator(final TransactionPropagator transactionPropagator) {
    this.transactionPropagator = transactionPropagator;
  }

}
