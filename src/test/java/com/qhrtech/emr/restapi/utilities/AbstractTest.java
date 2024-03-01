
package com.qhrtech.emr.restapi.utilities;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import com.qhrtech.emr.restapi.config.MappingConfig;
import com.qhrtech.emr.restapi.security.ApiSecurityContext;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.services.SecurityContextService;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.dozer.Mapper;
import org.junit.Before;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import uk.co.jemos.podam.api.PodamFactory;
import uk.co.jemos.podam.api.PodamFactoryImpl;

public abstract class AbstractTest<T> {

  /**
   * Mocked out testObject container. The actual services returned will be supplied by the
   * implementing test.
   */
  @Mock
  private AccuroApiService serviceContainer;

  /**
   * Mocked out security context provider. The context provided will be supplied by the implementing
   * test.
   */
  @Mock
  private SecurityContextService securityContextService;

  /**
   * Not Mocking the dozerMapper as it will add to the complexity. Complex Dozer mappers should have
   * their own tests.
   */
  @Spy
  private Mapper dozerMapper;

  /**
   * The object of the tests will be running against.
   */
  @InjectMocks
  protected final T testObject;

  /**
   * Default Podam factory, used to instantiate fixtures.
   */
  private final PodamFactory podamFactory;

  /**
   * Constructor for specifying a custom base url.
   *
   * @param testObject Object under test.
   */
  public AbstractTest(T testObject) {
    this.testObject = testObject;
    this.dozerMapper = new MappingConfig().modelMapper();
    this.podamFactory = new PodamFactoryImpl();
  }

  @Before
  @SuppressWarnings("unchecked")
  public void setUp() {
    // Service Layer Mocks
    serviceContainer = mock(AccuroApiService.class);
    Map<Class, Object> containerServices = getContainerServices();
    assertNotNull("Container Services Map cannot be null.", containerServices);

    // Security Context Mocks
    securityContextService = mock(SecurityContextService.class);
    ApiSecurityContext context = getSecurityContext();
    assertNotNull("Tests must provide a Security Context", context);
    assertNotNull("All tests must include a Tenant Id", context.getTenantId());

    // Inject Mocks into tested testObject
    MockitoAnnotations.initMocks(this);

    // Returns for injected mocks needs to be set up afterwards
    when(securityContextService.getSecurityContext()).thenReturn(context);

    containerServices.forEach(
        (c, m) -> when(serviceContainer.getImpl(c, context.getTenantId())).thenReturn(c.cast(m)));
    containerServices.forEach(
        (c, m) -> when(serviceContainer.getImpl(c, context.getTenantId(), true))
            .thenReturn(c.cast(m)));
  }


  /**
   * Returns a randomly generated fixture of the requested class.
   *
   * @param clazz Class flag of the fixture requested.
   * @param args Any generic parameters of the class.
   * @param <C> Type of fixture requested.
   * @return A randomly generated instance of the requested class.
   */
  protected <C> C getFixture(Class<C> clazz, Type... args) {
    return podamFactory.manufacturePojo(clazz, args);
  }


  /**
   * Returns a collection of randomly generated fixtures of the requested class.
   *
   * @param clazz Class flag of the fixture requested.
   * @param collectionFactory Supplier of the requested collection type (e.g. HashSet::new).
   * @param count Number of random entities requested.
   * @param args Any generic parameters of the class.
   * @param <C> Type of collection requested.
   * @param <D> Type of fixture requested.
   * @return A collection filled with randomly generated instances of the requested class.
   */
  protected <C extends Collection<D>, D> C getFixtures(Class<D> clazz,
      Supplier<C> collectionFactory, int count, Type... args) {
    C c = collectionFactory.get();
    IntStream.range(0, count).forEach(i -> c.add(getFixture(clazz, args)));
    return c;
  }

  /**
   * <p>
   * Maps an entity to another representation.
   * </p>
   *
   * <p>
   * Can be used to map back and forth between equivalent entities.
   * </p>
   *
   * @param <S> Source Type (e.g. Data layer object type).
   * @param <D> Destination Type (e.g. API Data Transfer Object type).
   * @param source Collection of source objects.
   * @param destinationType Destination Type flag.
   * @return Data transfer object with its values mapped.
   */
  protected <S, D> D mapDto(S source, Class<D> destinationType) {
    return mapToDtoImpl(source, destinationType);
  }

  /**
   * <p>
   * Maps a Collection of entities to a Collection of another representation of the entity.
   * </p>
   *
   * <p>
   * Can be used to map back and forth between equivalent entities.
   * </p>
   *
   * <p>
   * Example Usage: {@code mapDto(myDataLayerObjects, MyDto.class, HashSet::new);}
   * <p>
   *
   * @param <S> Source Type (e.g. Data layer object type).
   * @param <D> Destination Type (e.g. API Data Transfer Object type).
   * @param <C> Type of Collection to return (List, Set, etc).
   * @param source Collection of source objects.
   * @param destinationType Destination Type flag.
   * @param collectionFactory Supplier of A (e.g HashSet::new, TreeSet::new, ArrayList::new).
   * @return Collection of mapped Data Transfer Objects.
   */
  protected <S, D, C extends Collection<D>> C mapDto(
      Collection<S> source,
      Class<D> destinationType,
      Supplier<C> collectionFactory) {
    return source
        .stream()
        .map(s -> mapToDtoImpl(s, destinationType))
        .collect(Collectors.toCollection(collectionFactory));
  }

  /**
   * <p>
   * Maps a Map of keys to one type of entity to a Map of the same keys to another representation.
   * </p>
   *
   * <p>
   * Can be used to map back and forth between equivalent entities.
   * </p>
   *
   * <p>
   * Note: this implementation will make an arbitrary selection if values mapped to the same key are
   * found.
   * </p>
   *
   * <p>
   * Example Usage: {@code mapDto(myMapOfProtossEntityValues, MyApiDto.class, HashMap::new);}
   * </p>
   *
   * @param source The source map (e.g. Keys to Data Layer Entities)
   * @param destinationType The type to convert to (e.g. Api Layer Entity)
   * @param mapFactory Supplier of M (e.g. HashMap::new)
   * @param <S> Source Type
   * @param <D> Destination Type
   * @param <K> Key Type
   * @param <M> Type of Map to return
   * @return Map of Ds mapped to their original keys
   */
  protected <S, D, K, M extends Map<K, D>> M mapDto(Map<K, S> source,
      Class<D> destinationType,
      Supplier<M> mapFactory) {
    return source.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            e -> mapToDtoImpl(e.getValue(), destinationType),
            (v1, v2) -> v1, mapFactory));
  }

  /**
   * <p>
   * Maps a map of keys to collections of entities to a Map of the same keys to collections of
   * another representation.
   * </p>
   *
   * <p>
   * Can be used to map back and forth between entities.
   * </p>
   *
   * <p>
   * Note: this implementation will make an arbitrary selection if values mapped to the same key are
   * found.
   * </p>
   *
   * <p>
   * Example Usage: {@code mapDto(myMapOfDatalayerEntityCollections, MyApiDto.class, ArrayList::new,
   * HashMap::new);}
   * </p>
   *
   * @param source The source map (e.g. Keys to Data Layer Entities)
   * @param destinationType The type to convert to (e.g. Api Layer Entity)
   * @param collectionFactory Supplier of C (Type of collection to use, e.g. ArrayList::new)
   * @param mapFactory Supplier of M (Type of map to produce, e.g. LinkedHashMap::new)
   * @param <S> Source Entity Type
   * @param <D> Destination Entity Type
   * @param <K> Key Type
   * @param <I> Source Collection Type (Collection of S)
   * @param <C> Destination Collection Type
   * @param <M> Type of Map to produce
   * @return Map of Collections of Ds mapped to their original keys
   */
  protected <S, D, K, I extends Collection<S>, C extends Collection<D>, M extends Map<K, C>> M mapDto(
      Map<K, I> source, Class<D> destinationType,
      Supplier<C> collectionFactory,
      Supplier<M> mapFactory) {
    return source.entrySet()
        .stream()
        .collect(Collectors.toMap(Map.Entry::getKey,
            e -> mapDto(e.getValue(), destinationType, collectionFactory),
            (v1, v2) -> v1,
            mapFactory));
  }

  private <S, D> D mapToDtoImpl(S s, Class<D> clazz) {
    return dozerMapper.map(s, clazz);
  }

  /**
   * Providers implementing classes to the dto dozerMapper injected into the controller under test.
   *
   * @return Dozer dozerMapper used by the controller under test.
   */
  protected Mapper getDozerMapper() {
    return dozerMapper;
  }

  /**
   * Called once per test to set up the Api Security Context
   *
   * At the very least, the tenant Id should be set.
   *
   * @return a valid Security Context for the needs of the test.
   */
  protected abstract ApiSecurityContext getSecurityContext();

  /**
   * Build a map of Service Layer managers to return by the mocked testObject container.
   *
   * @return A Map containing all testObject layer managers (Protoss) used by this test class.
   */
  protected abstract Map<Class, Object> getContainerServices();

}
