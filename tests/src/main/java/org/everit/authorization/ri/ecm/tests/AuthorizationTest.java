/*
 * Copyright (C) 2011 Everit Kft. (http://www.everit.org)
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
package org.everit.authorization.ri.ecm.tests;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.everit.authorization.AuthorizationManager;
import org.everit.authorization.PermissionChecker;
import org.everit.authorization.qdsl.util.AuthorizationQdslUtil;
import org.everit.authorization.ri.schema.qdsl.QPermission;
import org.everit.authorization.ri.schema.qdsl.QPermissionInheritance;
import org.everit.osgi.dev.testrunner.TestRunnerConstants;
import org.everit.osgi.ecm.annotation.Component;
import org.everit.osgi.ecm.annotation.ConfigurationPolicy;
import org.everit.osgi.ecm.annotation.Service;
import org.everit.osgi.ecm.annotation.ServiceRef;
import org.everit.osgi.ecm.annotation.attribute.StringAttribute;
import org.everit.osgi.ecm.annotation.attribute.StringAttributes;
import org.everit.osgi.ecm.extender.ExtendComponent;
import org.everit.persistence.querydsl.support.QuerydslSupport;
import org.everit.resource.ResourceService;
import org.everit.resource.ri.schema.qdsl.QResource;
import org.junit.Assert;
import org.junit.Test;
import org.osgi.service.log.LogService;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.sql.RelationalPathBase;
import com.querydsl.sql.SQLQuery;
import com.querydsl.sql.dml.SQLDeleteClause;

/**
 * Testing the basic functionality of authorization.
 */
@ExtendComponent
@Component(configurationPolicy = ConfigurationPolicy.FACTORY)
@StringAttributes({
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TESTRUNNER_ENGINE_TYPE,
        defaultValue = "junit4"),
    @StringAttribute(attributeId = TestRunnerConstants.SERVICE_PROPERTY_TEST_ID,
        defaultValue = "AuthorizationBasicTest") })
@Service(value = AuthorizationTest.class)
public class AuthorizationTest {

  /**
   * DTO class to Permission Manipulation test.
   */
  private static class PermissionManipulationDTO {

    public long a1;

    public long a2;

    public long a3;

    public long a4;

    public long a5;

    public long a6;

    public long a7;

    public long a8;

    public long t1;

    public long t2;

    public long t3;

    public long t4;

    public long t5;

    public long t6;

    public long t7;

    public long t8;
  }

  private static final long INVALID_RESOURCE_ID = -1;

  private static long[] convert(final Collection<Long> collection) {
    long[] result = new long[collection.size()];
    Iterator<Long> iterator = collection.iterator();
    int i = 0;
    while (iterator.hasNext()) {
      Long element = iterator.next();
      result[i] = element;
      i++;
    }
    Arrays.sort(result);
    return result;
  }

  private static long[] sort(final long[] data) {
    Arrays.sort(data);
    return data;
  }

  private AuthorizationManager authorizationManager;

  private AuthorizationQdslUtil authorizationQdslUtil;

  private LogService logService;

  private PermissionChecker permissionChecker;

  private QuerydslSupport querydslSupport;

  private ResourceService resourceService;

  private void clearResourceTable() {
    this.querydslSupport.execute((connection, configuration) -> {
      QResource resource = QResource.resource;
      new SQLDeleteClause(connection, configuration, resource).where(
          resource.resourceId.ne(this.permissionChecker.getSystemResourceId())).execute();
      return null;
    });
  }

  private void clearTable(final RelationalPathBase<?> path) {
    this.querydslSupport.execute((connection, configuration) -> {
      new SQLDeleteClause(connection, configuration, path).execute();
      return null;
    });
  }

  private PermissionManipulationDTO createPermissionManipulationDTO() {
    PermissionManipulationDTO permissionManipulationDTO = new PermissionManipulationDTO();
    permissionManipulationDTO.a1 = this.resourceService.createResource();
    permissionManipulationDTO.a2 = this.resourceService.createResource();
    permissionManipulationDTO.a3 = this.resourceService.createResource();
    permissionManipulationDTO.a4 = this.resourceService.createResource();
    permissionManipulationDTO.a5 = this.resourceService.createResource();
    permissionManipulationDTO.a6 = this.resourceService.createResource();
    permissionManipulationDTO.a7 = this.resourceService.createResource();
    permissionManipulationDTO.a8 = this.resourceService.createResource();

    permissionManipulationDTO.t1 = this.resourceService.createResource();
    permissionManipulationDTO.t2 = this.resourceService.createResource();
    permissionManipulationDTO.t3 = this.resourceService.createResource();
    permissionManipulationDTO.t4 = this.resourceService.createResource();
    permissionManipulationDTO.t5 = this.resourceService.createResource();
    permissionManipulationDTO.t6 = this.resourceService.createResource();
    permissionManipulationDTO.t7 = this.resourceService.createResource();
    permissionManipulationDTO.t8 = this.resourceService.createResource();
    return permissionManipulationDTO;
  }

  private void extracted(final PermissionManipulationDTO permissionManipulation,
      final String action) {
    this.authorizationManager.addPermission(permissionManipulation.a1, permissionManipulation.t1,
        action);
    this.authorizationManager.addPermission(permissionManipulation.a2, permissionManipulation.t2,
        action);
    this.authorizationManager.addPermission(permissionManipulation.a3, permissionManipulation.t3,
        action);
    this.authorizationManager.addPermission(permissionManipulation.a4, permissionManipulation.t4,
        action);
    this.authorizationManager.addPermission(permissionManipulation.a5, permissionManipulation.t5,
        action);
    this.authorizationManager.addPermission(permissionManipulation.a6, permissionManipulation.t6,
        action);
    this.authorizationManager.addPermission(permissionManipulation.a7, permissionManipulation.t7,
        action);
    this.authorizationManager.addPermission(permissionManipulation.a8, permissionManipulation.t8,
        action);

    this.authorizationManager.addPermissionInheritance(permissionManipulation.a1,
        permissionManipulation.a3);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a1,
        permissionManipulation.a4);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a2,
        permissionManipulation.a4);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a2,
        permissionManipulation.a5);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a3,
        permissionManipulation.a6);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a4,
        permissionManipulation.a6);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a4,
        permissionManipulation.a7);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a5,
        permissionManipulation.a7);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a6,
        permissionManipulation.a8);
    this.authorizationManager.addPermissionInheritance(permissionManipulation.a7,
        permissionManipulation.a8);
  }

  public LogService getLogService() {
    return this.logService;
  }

  private long[] resolveTargetResourcesWithPermission(final long a1, final String action1) {
    return this.querydslSupport.execute((connection, configuration) -> {
      QResource targetResource = new QResource("tr");

      BooleanExpression permissionCheck = this.authorizationQdslUtil.authorizationPredicate(a1,
          targetResource.resourceId, action1);

      List<Long> list = new SQLQuery<Long>(connection, configuration)
          .select(targetResource.resourceId)
          .from(targetResource)
          .where(permissionCheck)
          .fetch();

      return AuthorizationTest.convert(list);
    });
  }

  private long[] resolveTargetResourcesWithPermission(final long a1, final String[] actions) {
    return this.querydslSupport.execute((connection, configuration) -> {
      QResource targetResource = new QResource("tr");

      BooleanExpression permissionCheckBooleanExpression = this.authorizationQdslUtil
          .authorizationPredicate(a1, targetResource.resourceId, actions);

      List<Long> list = new SQLQuery<Long>(connection, configuration)
          .select(targetResource.resourceId)
          .from(targetResource)
          .where(permissionCheckBooleanExpression)
          .fetch();

      return AuthorizationTest.convert(list);
    });
  }

  @ServiceRef(defaultValue = "")
  public void setAuthorizationManager(final AuthorizationManager authorizationManager) {
    this.authorizationManager = authorizationManager;
  }

  @ServiceRef(defaultValue = "")
  public void setAuthorizationQdslUtil(final AuthorizationQdslUtil authorizationQdslUtil) {
    this.authorizationQdslUtil = authorizationQdslUtil;
  }

  @ServiceRef(defaultValue = "")
  public void setLogService(final LogService log) {
    this.logService = log;
  }

  @ServiceRef(defaultValue = "")
  public void setPermissionChecker(final PermissionChecker permissionChecker) {
    this.permissionChecker = permissionChecker;
  }

  @ServiceRef(defaultValue = "")
  public void setQuerydslSupport(final QuerydslSupport qdsl) {
    this.querydslSupport = qdsl;
  }

  @ServiceRef(defaultValue = "")
  public void setResourceService(final ResourceService resourceService) {
    this.resourceService = resourceService;
  }

  // private void stressTest(final long[] authorizedResourceIds, final long[] targetResourceIds,
  // final String action) {
  // logService.log(LogService.LOG_INFO, "Starting stress test");
  // final long iterationNum = 10000000;
  // final int threadNum = 2;
  // final Random r = new Random();
  // final AtomicInteger runningThreads = new AtomicInteger(threadNum);
  // final Object mutex = new Object();
  //
  // long startTime = System.currentTimeMillis();
  //
  // for (int thi = 0; thi < threadNum; thi++) {
  // new Thread(() -> {
  // for (long i = 0; i < iterationNum; i++) {
  // long authorizedResourceId =
  // authorizedResourceIds[r.nextInt(authorizedResourceIds.length)];
  // long targetResourceId = targetResourceIds[r.nextInt(targetResourceIds.length)];
  //
  // permissionChecker.hasPermission(authorizedResourceId, targetResourceId, action);
  // }
  // int runningThreadNum = runningThreads.decrementAndGet();
  // if (runningThreadNum == 0) {
  // synchronized (mutex) {
  // mutex.notify();
  // }
  // }
  // }).start();
  // }
  //
  // synchronized (mutex) {
  // if (runningThreads.get() > 0) {
  // try {
  // mutex.wait();
  // } catch (InterruptedException e) {
  // getLogService().log(LogService.LOG_ERROR, "Waiting for test threads to finish was interrupted",
  // e);
  // Thread.currentThread().interrupt();
  // }
  // }
  // }
  //
  // long endTime = System.currentTimeMillis();
  // getLogService().log(LogService.LOG_INFO, "Stress test finished: " + (endTime - startTime) + "
  // ms");
  // }

  @Test(expected = RuntimeException.class)
  public void testAddPermisisonInheritanceInvalidChildResource() {
    long resourceId = this.resourceService.createResource();
    try {
      this.authorizationManager.addPermissionInheritance(INVALID_RESOURCE_ID, resourceId);
    } finally {
      clearResourceTable();
    }
  }

  @Test(expected = RuntimeException.class)
  public void testAddPermisisonInheritanceInvalidParentResource() {
    long resourceId = this.resourceService.createResource();
    try {
      this.authorizationManager.addPermissionInheritance(INVALID_RESOURCE_ID, resourceId);
    } finally {
      clearResourceTable();
    }
  }

  @Test(expected = RuntimeException.class)
  public void testAddPermissionInvalidAuthorizedResourceId() {
    long resourceId = this.resourceService.createResource();
    try {
      this.authorizationManager.addPermission(INVALID_RESOURCE_ID, resourceId, "");
    } finally {
      clearResourceTable();
    }
  }

  @Test(expected = RuntimeException.class)
  public void testAddPermissionInvalidTargetResourceId() {
    long resourceId = this.resourceService.createResource();
    try {
      this.authorizationManager.addPermission(resourceId, INVALID_RESOURCE_ID, "");
    } finally {
      clearResourceTable();
    }
  }

  @Test(expected = NullPointerException.class)
  public void testAddPermissionNullAction() {
    this.authorizationManager.addPermission(INVALID_RESOURCE_ID, INVALID_RESOURCE_ID, null);
  }

  @Test
  public void testCyclicPermissionInheritance() {
    long a1 = this.resourceService.createResource();
    long a2 = this.resourceService.createResource();
    long a3 = this.resourceService.createResource();

    this.authorizationManager.addPermissionInheritance(a1, a2);
    this.authorizationManager.addPermissionInheritance(a2, a1);
    this.authorizationManager.addPermissionInheritance(a1, a3);
    this.authorizationManager.addPermissionInheritance(a2, a3);

    Assert.assertArrayEquals(AuthorizationTest.sort(new long[] { a1, a2, a3 }),
        AuthorizationTest.sort(this.permissionChecker.getAuthorizationScope(a3)));
    Assert.assertArrayEquals(AuthorizationTest.sort(new long[] { a1, a2 }),
        AuthorizationTest.sort(this.permissionChecker.getAuthorizationScope(a1)));

    this.authorizationManager.clearCache();
    clearTable(QPermission.permission);
    clearTable(QPermissionInheritance.permissionInheritance);
    clearResourceTable();
  }

  @Test
  public void testPermissionCheckInvalidResource() {
    long resourceId = this.resourceService.createResource();

    Assert.assertFalse(this.permissionChecker.hasPermission(INVALID_RESOURCE_ID, resourceId, ""));

    Assert.assertFalse(this.permissionChecker.hasPermission(resourceId, INVALID_RESOURCE_ID, ""));

    clearResourceTable();
  }

  @Test(expected = NullPointerException.class)
  public void testPermissionCheckNullAction() {
    this.permissionChecker.hasPermission(INVALID_RESOURCE_ID, INVALID_RESOURCE_ID, (String[]) null);
  }

  @Test(expected = NullPointerException.class)
  public void testPermissionCheckNullActionInArray() {
    this.permissionChecker.hasPermission(INVALID_RESOURCE_ID, INVALID_RESOURCE_ID, (String) null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPermissionCheckZeroAction() {
    this.permissionChecker.hasPermission(INVALID_RESOURCE_ID, INVALID_RESOURCE_ID);
  }

  @Test
  public void testPermissionManipulation() {
    PermissionManipulationDTO permissionManipulationDTO = createPermissionManipulationDTO();

    final String action1 = "action1";

    extracted(permissionManipulationDTO, action1);

    Assert.assertArrayEquals(AuthorizationTest.sort(new long[] { permissionManipulationDTO.a1,
        permissionManipulationDTO.a2, permissionManipulationDTO.a3, permissionManipulationDTO.a6,
        permissionManipulationDTO.a4 }),
        AuthorizationTest
            .sort(this.permissionChecker.getAuthorizationScope(permissionManipulationDTO.a6)));

    Assert.assertFalse(this.permissionChecker.hasPermission(permissionManipulationDTO.a1,
        permissionManipulationDTO.t1, "x"));
    Assert.assertTrue(this.permissionChecker.hasPermission(permissionManipulationDTO.a1,
        permissionManipulationDTO.t1, action1));
    Assert.assertTrue(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t1, action1));
    Assert.assertFalse(this.permissionChecker.hasPermission(permissionManipulationDTO.a1,
        permissionManipulationDTO.t8, action1));
    Assert.assertTrue(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t4, action1));

    this.authorizationManager.removePermissionInheritance(permissionManipulationDTO.a4,
        permissionManipulationDTO.a7);

    Assert.assertTrue(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t4, action1));

    this.authorizationManager.removePermissionInheritance(permissionManipulationDTO.a4,
        permissionManipulationDTO.a6);

    Assert.assertFalse(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t4, action1));
    Assert.assertTrue(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t2, action1));
    Assert.assertArrayEquals(AuthorizationTest.sort(new long[] { permissionManipulationDTO.a1,
        permissionManipulationDTO.a3, permissionManipulationDTO.a6 }),
        AuthorizationTest
            .sort(this.permissionChecker.getAuthorizationScope(permissionManipulationDTO.a6)));

    this.authorizationManager.removePermissionInheritance(permissionManipulationDTO.a2,
        permissionManipulationDTO.a5);

    Assert.assertFalse(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t2, action1));

    Assert.assertTrue(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t1, action1));
    this.authorizationManager.removePermission(permissionManipulationDTO.a1,
        permissionManipulationDTO.t1, action1);
    Assert.assertFalse(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t1, action1));

    // Uncomment if you want some stress testing
    // stressTest(new long[] { permissionManipulationDTO.a1, permissionManipulationDTO.a2,
    // permissionManipulationDTO.a3, permissionManipulationDTO.a4, permissionManipulationDTO.a5,
    // permissionManipulationDTO.a6, permissionManipulationDTO.a7, permissionManipulationDTO.a8 },
    // new long[] { permissionManipulationDTO.t1, permissionManipulationDTO.t2,
    // permissionManipulationDTO.t3, permissionManipulationDTO.t4, permissionManipulationDTO.t5,
    // permissionManipulationDTO.t6, permissionManipulationDTO.t7, permissionManipulationDTO.t8 },
    // action1);

    this.authorizationManager.clearCache();

    Assert.assertFalse(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t1, action1));
    Assert.assertTrue(this.permissionChecker.hasPermission(permissionManipulationDTO.a8,
        permissionManipulationDTO.t8, action1));

    this.authorizationManager.clearCache();
    clearTable(QPermission.permission);
    clearTable(QPermissionInheritance.permissionInheritance);
    clearResourceTable();
  }

  @Test
  public void testQueryExtension() {
    long a1 = this.resourceService.createResource();
    long a2 = this.resourceService.createResource();
    long a3 = this.resourceService.createResource();

    long t1 = this.resourceService.createResource();
    long t2 = this.resourceService.createResource();
    long t3 = this.resourceService.createResource();

    final String action1 = "action1";
    final String action2 = "action2";
    final String action3 = "action3";
    final String action4 = "action4";

    this.authorizationManager.addPermission(a1, t1, action1);
    this.authorizationManager.addPermission(a1, t1, action2);
    this.authorizationManager.addPermission(a2, t2, action1);
    this.authorizationManager.addPermission(a3, t3, action3);

    this.authorizationManager.addPermissionInheritance(a1, a2);
    this.authorizationManager.addPermissionInheritance(a1, a3);

    long[] resources = resolveTargetResourcesWithPermission(a1, action1);
    Assert.assertArrayEquals(new long[] { t1 }, resources);

    resources = resolveTargetResourcesWithPermission(a2, action1);
    Assert.assertArrayEquals(new long[] { t1, t2 }, resources);

    resources = resolveTargetResourcesWithPermission(a2, action2);
    Assert.assertArrayEquals(new long[] { t1 }, resources);

    resources = resolveTargetResourcesWithPermission(a2, action4);
    Assert.assertArrayEquals(new long[] {}, resources);

    resources = resolveTargetResourcesWithPermission(a2, new String[] { action4 });
    Assert.assertArrayEquals(new long[] {}, resources);

    resources = resolveTargetResourcesWithPermission(a3, new String[] { action3, action2 });
    Assert.assertArrayEquals(new long[] { t1, t3 }, resources);

    this.authorizationManager.clearCache();
    clearTable(QPermission.permission);
    clearTable(QPermissionInheritance.permissionInheritance);
    clearResourceTable();
  }

  public void testRemovePermissionInheritanceInvalidResources() {
    this.authorizationManager.removePermissionInheritance(INVALID_RESOURCE_ID, INVALID_RESOURCE_ID);
  }

  @Test(expected = NullPointerException.class)
  public void testRemovePermissionNullAction() {
    this.authorizationManager.removePermission(INVALID_RESOURCE_ID, INVALID_RESOURCE_ID, null);
  }

  @Test
  public void testSystemResource() {
    long systemResourceId = this.permissionChecker.getSystemResourceId();
    Assert.assertTrue(
        this.permissionChecker.hasPermission(systemResourceId, INVALID_RESOURCE_ID, ""));

    long a1 = this.resourceService.createResource();

    this.authorizationManager.addPermissionInheritance(systemResourceId, a1);

    Assert.assertTrue(this.permissionChecker.hasPermission(a1, INVALID_RESOURCE_ID, ""));

    Assert.assertArrayEquals(AuthorizationTest.sort(new long[] { systemResourceId, a1 }),
        resolveTargetResourcesWithPermission(systemResourceId, ""));

    Assert.assertArrayEquals(AuthorizationTest.sort(new long[] { systemResourceId, a1 }),
        resolveTargetResourcesWithPermission(a1, ""));

    clearTable(QPermission.permission);
    clearTable(QPermissionInheritance.permissionInheritance);
    clearResourceTable();
  }
}
