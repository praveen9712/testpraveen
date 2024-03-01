
package com.qhrtech.emr.restapi.services;


import com.qhrtech.emr.restapi.endpoints.utilities.TestUtilities;
import com.qhrtech.emr.restapi.services.impl.AccuroApiServiceImpl;
import com.qhrtech.emr.restapi.services.impl.ImplementationNotFoundException;
import org.junit.Test;

public class AccuroApiServiceImplTest {

  private final AccuroApiServiceImpl accuroApiServiceImpl = new AccuroApiServiceImpl();

  public AccuroApiServiceImplTest() {
  }

  @Test(expected = ImplementationNotFoundException.class)
  public void testInstantiateManagerWithMissingMapping() throws Exception {
    AccuroApiServiceImpl impl =
        accuroApiServiceImpl.getImpl(AccuroApiServiceImpl.class, TestUtilities.nextString(5));

  }

  @Test(expected = NullPointerException.class)
  public void testInstantiateManagerWithNoParam() throws Exception {
    accuroApiServiceImpl.getImpl(null, TestUtilities.nextString(5));
  }

  public void testInstantiateSkipTests() throws Exception {
    accuroApiServiceImpl.getImpl(null, TestUtilities.nextString(5), true);
  }

}

