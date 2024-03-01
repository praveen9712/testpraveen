
package com.qhrtech.emr.restapi.endpoints;

import com.qhrtech.emr.accuro.model.security.permissions.AccuroApiContext;
import com.qhrtech.emr.accuro.permissions.AuditLogUser;
import com.qhrtech.emr.restapi.models.endpoints.Error;
import com.qhrtech.emr.restapi.services.AccuroApiService;
import com.qhrtech.emr.restapi.services.SecurityContextService;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.stream.Collectors;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.dozer.Mapper;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@SecurityScheme(
    type = SecuritySchemeType.OAUTH2,
    name = "oauth2")
@ApiResponse(
    responseCode = "401",
    description = "Unauthorized")
@ApiResponse(
    responseCode = "403",
    description = "Access Denied")
public abstract class AbstractEndpoint {

  @Autowired
  private SecurityContextService securityContextService;
  @Autowired
  private Mapper mapper;
  @Autowired
  private AccuroApiService api;

  protected <T> T getImpl(Class<T> interfaceClass) {
    String tenantId = getTenantId();
    return api.getImpl(interfaceClass, tenantId);
  }

  protected <T> T getImpl(Class<T> interfaceClass, boolean skipPermissionsCheck) {
    String tenantId = getTenantId();
    return api.getImpl(interfaceClass, tenantId, skipPermissionsCheck);
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
    if (source == null) {
      throw Error.webApplicationException(Status.NOT_FOUND, "Resource not found.");
    }

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
   * @param <S> Source Type (e.g. Data layer object type).
   * @param <D> Destination Type (e.g. API Data Transfer Object type).
   * @param <C> Type of Collection to return (List, Set, etc).
   * @param source Collection of source objects.
   * @param destinationType Destination Type flag.
   * @param collectionFactory Supplier of A (e.g HashSet::new, TreeSet::new, ArrayList::new).
   *
   *        <p>
   *        Example Usage: {@code mapDto(myDataLayerObjects, MyDto.class, HashSet::new);}
   *        <p>
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
   * @param source The source map (e.g. Keys to Data Layer Entities)
   * @param destinationType The type to convert to (e.g. Api Layer Entity)
   * @param mapFactory Supplier of M (e.g. HashMap::new)
   * @param <S> Source Type
   * @param <D> Destination Type
   * @param <K> Key Type
   * @param <M> Type of Map to return
   *
   *        <p>
   *        Example Usage: {@code mapDto(myMapOfProtossEntityValues, MyApiDto.class, HashMap::new);}
   *        </p>
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
   *
   *        <p>
   *        Example Usage:
   *        {@code mapDto(myMapOfDatalayerEntityCollections, MyApiDto.class, ArrayList::new,
   * HashMap::new);}
   *        </p>
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

  protected String getOAuthId() {
    return securityContextService.getSecurityContext().getOauthClientId();
  }

  protected AuditLogUser getUser() {
    return securityContextService.getSecurityContext().getUser();
  }


  protected Integer getPatientId() {
    return securityContextService.getSecurityContext().getPatientId();
  }

  protected String getTenantId() {
    return securityContextService.getSecurityContext().getTenantId();
  }

  protected UUID getClientUuid() {
    return securityContextService.getSecurityContext().getSessionUuid();
  }

  private <S, D> D mapToDtoImpl(S s, Class<D> clazz) {
    return mapper.map(s, clazz);
  }

  protected Set<String> getScopes() {
    return securityContextService.getSecurityContext().getScopes();
  }

  protected String getGrantType() {
    return securityContextService.getSecurityContext().getGrantType();
  }

  protected AccuroApiContext getAccuroApiContext() {
    return securityContextService.getSecurityContext().getAccuroApiContext();
  }

  protected void validateUserId() {
    if (getUser().getUserId() == null) {
      throw Error.webApplicationException(Response.Status.BAD_REQUEST,
          "User Id is required for this operation.");
    }
  }

  protected LocalDate dateParser(String value, String name) {
    String message = "Invalid date format: " + name + ". Valid format is YYYY-MM-DD";
    final java.util.regex.Pattern searchPattern =
        java.util.regex.Pattern.compile("\\d{4}-\\d{2}-\\d{2}");
    Matcher m = searchPattern.matcher(value);
    if (!m.matches()) {
      throw new IllegalArgumentException(message);
    }
    LocalDate date;
    try {
      date = new LocalDate(value);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(message);
    }
    return date;

  }

}
