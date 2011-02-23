package edu.cmu.commons.data.dao.mongo.reflect;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

import javax.persistence.Embeddable;
import javax.persistence.Id;

import org.bson.BSONDecoder;
import org.bson.BSONEncoder;
import org.bson.BSONObject;
import org.bson.BasicBSONObject;
import org.bson.types.BSONTimestamp;
import org.bson.types.BasicBSONList;
import org.bson.types.Binary;
import org.bson.types.Code;
import org.bson.types.CodeWScope;
import org.bson.types.ObjectId;
import org.bson.types.Symbol;

import com.mongodb.DBObject;

import edu.cmu.commons.data.dao.Dao;
import edu.cmu.commons.data.dao.mongo.EntityMarshallerFactory;
import edu.cmu.commons.data.dao.mongo.Marshaller;
import edu.cmu.commons.data.dao.mongo.MarshallerFactory;

/**
 * Creates (or retrieves from an internal cache) EntityMarshaller instances via
 * Java Bean reflection (see {@link Introspector}).
 * 
 * <h1>EntityMarshaller Creation</h1>
 * 
 * During EntityMarshaller creation, new {@link Marshaller} instances are
 * created for Java types encountered during recursive introspection of the type
 * graph rooted at a given Entity type. See below for a discussion of the rules
 * which govern how these {@code Marshaller}s are created for various Java
 * types.
 * 
 * <h2 id="simple">Simple Types</h2>
 * 
 * Instances of simple types directly supported by {@link BSONEncoder} and
 * {@link BSONDecoder} are passed between Java and BSON data models with minimal
 * conversion. These types include:
 * 
 * <ul>
 * <li>Basic types
 * <ul>
 * <li>{@link String}</li>
 * <li>{@link Boolean}</li>
 * <li>{@code byte[]}</li>
 * </ul>
 * </li>
 * <li>Number types, including:
 * <ul>
 * <li>{@link Byte}</li> (treated as Integer during (de)serialization)
 * <li>{@link Short}</li> (treated as Integer during (de)serialization)
 * <li>{@link Integer}</li>
 * <li>{@link AtomicInteger}</li>
 * <li>{@link Long}</li>
 * <li>{@link AtomicLong}</li>
 * <li>{@link Float}</li> (treated as Double during (de)serialization)
 * <li>{@link Double}</li>
 * </ul>
 * </li>
 * <li>Special Java types
 * <ul>
 * <li>{@link Date}</li>
 * <li>{@link UUID}</li>
 * <li>{@link Pattern}</li>
 * </ul>
 * </li>
 * <li>BSON types
 * <ul>
 * <li>{@link BSONObject}</li>
 * <li>{@link ObjectId}</li>
 * <li>{@link Binary}</li>
 * <li>{@link Symbol}</li>
 * <li>{@link BSONTimestamp}</li>
 * <li>{@link Code}</li>
 * <li>{@link CodeWScope}</li>
 * </ul>
 * </li>
 * <li>Collection types where {@code S} is a simple type
 * <ul>
 * <li>{@link Collection}{@code <S>}</li>
 * <li>{@link List}{@code <S>}</li>
 * <li>{@link ArrayList}{@code <S>}</li>
 * <li>{@code S[]} (Marshalling of arrays of simple types is pass-through, but
 * unmarshalling requires a transform from {@link BasicBSONList} to {@code S[]})
 * </li>
 * <li>{@link Map}{@code <String, S>}</li>
 * <li>{@link HashMap}{@code <String, S>}</li>
 * <li>{@link LinkedHashMap}{@code <String, S>}</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * Please note that types derived from these simple types will not benefit from
 * this pass-through behavior because (1) they may have additional properties
 * which the BSONEncoder would otherwise ignore, and (2) the BSONDecoder would
 * not generate instances of the correct derived type when deserializing binary
 * data. Instead, alternate marshalling strategies are required, as detailed
 * below.
 * 
 * <h2 id="composite">Composite Types</h2>
 * 
 * A composite type defines one or more properties, each of which has a name, a
 * type, and accessor/mutator methods (Java Bean concepts are closely related;
 * See {@link BeanInfo} for more information). For our purposes, there are two
 * refinements of composite types; Entity types and Embeddable types.
 * 
 * <h3 id="entity">Entity Types</h3>
 * 
 * An Entity type is a concrete type which has a set of one or more properties
 * and a no-args default constructor. In addition, an entity type must specify
 * which of its properties acts as a unique identifier. This class follows a few
 * basic rules to chose which entity type property to treat as the "id"
 * property:
 * 
 * <ul>
 * <li>If only a single property is defined, then it must be the id property.</li>
 * <li>Property annotated with {@link Id}.</li>
 * <li>Property whose name is {@code "id"}.</li>
 * <li>Property whose type is {@code ObjectId}.</li>
 * </ul>
 * 
 * If none of these rules fires, an exception is raised during EntityMarshaller
 * creation.
 * <p>
 * Generated {@code EntityMarshaller}s produce {@code Map<String, Object>}s
 * during marshalling which contain keys to support all non-<code>null</code>
 * entity instance properties. A special key {@code "_id"} is used for the id
 * property to match MongoDB "User Document" conventions. Property values are
 * marshalled as necessary.
 * 
 * <h3 id="embed">Embeddable Types</h3>
 * 
 * An embeddable type is even more closely related to a Java Bean type than an
 * entity type; It also has a set of properties, but is not required to have an
 * "id" property. Instances of Embeddable types are conceptually "owned" by a
 * single entity instance. When creating {@code EntityMarshaller}s, if this
 * class encounters a type which is composite, but not a registered entity type,
 * then the type is assumed to represent an embeddable type. Alternately, you
 * may explicitly label types as embeddable with the {@link Embeddable}
 * annotation.
 * 
 * <h2 id="poly">Polymorphic Types</h2>
 * 
 * Frequently, a composite type may define a property whose type is an interface
 * or abstract class. The value of such a property at runtime will be an
 * instance of some concrete type which implements (or extends) the property's
 * type. In this situation, the concrete type of the property value must be
 * recorded within marshaled output if we need to construct a new value for this
 * property later on during unmarshalling.
 * <p>
 * By default, generated {@code Marshaller}s ignore this issue by assuming that
 * no new value will need to be created during unmarshalling. In other words, it
 * is assumed that the destination value into which data will be unmarshalled
 * will never be {@code null}. In many cases, this is a reasonable assumption.
 * For instance, let's say we have a {@code Document} entity type which defines
 * a property named {@code words} of type {@code Set<String>}. On default
 * construction of a {@code Document}, its {@code terms} property is initialized
 * with an empty {@code TreeSet}. During unmarshalling of {@code Document} data
 * for the {@code words} property, the existing {@code TreeSet} will be used to
 * store the unmarshalled {@code String}s. If the destination {@code Set} were
 * {@code null}, an exception would be raised.
 * <p>
 * On the other hand, perhaps the {@code Set} implementation used changes based
 * on runtime requirements. For instance, when the {@code Set} contains less
 * than N elements a {@code TreeSet} is used, but once it grows past this
 * threshold, it is replaced with a {@code HashSet}. In this case, polymorphism
 * must be supported by the {@code Marshaller} as mentioned above.
 * <p>
 * During {@code EntityMarshaller} creation, if this class encounters a property
 * which has been annotated with {@link Polymorphic}, then a {@code Marshaller}
 * for the property is created which handles the property as follows: During
 * marshalling, a {@code Map<String, Object>} is created to encode the value's
 * data along with a special {@code "className"} key with which the value's
 * runtime type is recorded. During unmarshalling, if the destination value is
 * {@code null} or its type does not match that defined by the
 * {@code "className"} key, a new instance of the appropriate type will be
 * created via the class's default constructor.
 * 
 * <h2>Container Types</h2>
 * 
 * BSON also has built-in support for {@link Map} and {@link Iterable}
 * serialization which are deserialized to {@link BasicBSONObject} and
 * {@link BasicBSONList}, respectively. However, special care is taken when
 * generating {@code Marshaller}s for container types as the instances they
 * contain may need custom marshalling.
 * 
 * <h3>Collection Types</h3>
 * 
 * As noted in section <a href="#simple">Simple Types</a>, if type
 * {@code Collection<E>}, {@code List<E>}, {@code ArrayList<E>}, or {@code E[]}
 * is encountered where {@code E} is a simple type, then minimal marshalling of
 * data is performed. For instance, during marshalling, if the destination
 * marshalled collection is {@code null} or empty, then the source unmarshalled
 * collection is returned as is. Similarly, during unmarshalling, if the
 * destination unmarshalled collection is {@code null} or empty, then the source
 * marshalled collection is returned as is, though care is taken to properly
 * unmarshal data into Array types. During marshalling or unmarshalling, if
 * destination collection is not {@code null}, then data from the source
 * collection is added to the destination collection without further
 * transformation.
 * <p>
 * If a type {@code C extends Collection<E>} is encountered where {@code C} is
 * not <a href="#composite">composite</a> and {@code E} is not a simple type, a
 * custom {@code Marshaller} is created which produces a new
 * {@code Iterable<Object>} during marshalling whose elements are properly
 * marshalled versions of the input collection's elements. If the destination
 * collection is {@code null} during unmarshalling, an attempt is made to create
 * a new destination collection instance via {@link Class#newInstance()}.
 * <p>
 * If a type {@code C extends Collection<E>} is encountered where {@code C} is
 * not <a href="#composite">composite</a>, it is treated as above, though during
 * unmarshalling it is assumed that the destination {@code Collection} instance
 * into which unmarshalled elements should be added is not {@code null}, or that
 * {@code C} is a concrete type having a no-args default constructor with which
 * a new {@code C} instance may be create.
 * <p>
 * Alternatively, if a {@code Collection} type is part of a composite type
 * property definition and the property is annotated with {@link Polymorphic},
 * an approach similar to that introduced in section <a href="#poly">Polymorphic
 * Types</a> is used. For these {@code Collection} types, custom
 * {@code Marshaller}s are created which produce {@code Map<String, Object>}s
 * during marshalling which contain {@code "className"} and {@code "elements"}
 * keys whose values specify the name of the source collection implementation
 * and collection element data, respectively. If the source collection type is
 * compatible with {@code Collection<E>} where {@code E} is a simple type, then
 * {@code "elements"} will reference the source collection instance directly. If
 * {@code E} is not a simple type, then {@code "elements"} will reference a new
 * {@code Iterable<Object>} whose elements are properly marshalled versions of
 * the source collection's elements. Note that the collection class referenced
 * by {@code "className"} must have a no-args default constructor with which new
 * instances may be created during unmarshalling, as needed.
 * 
 * <h3>Map Types</h3>
 * 
 * Rules for map types are similar to those for collection types. If a type
 * {@code Map<String, V>}, {@code HashMap<String, V>}, or
 * {@code LinkedHashMap<String, V>} is encountered where {@code V} is a simple
 * type, then no marshalling of data is performed. If {@code V} is not a simple
 * type, a custom {@code Marshaller} is created which produces a new
 * {@code BasicDBObject} during marshalling whose keys match the source map's
 * keys, and whose values are properly marshalled versions of the source map's
 * values.
 * <p>
 * If a type {@code M extends Map<K, V>} is encountered where M is not <a
 * href="#composite">composite</a> and {@code K} is not {@code String}, then a
 * custom {@code Marshaller<Map<K, V>, Object>} is created which produces a
 * {@code List<Map<String, Object>>} during marshalling where each
 * {@code Map<String, Object>} has keys {@code "key"} and {@code "value"} which
 * reference properly marshalled values.
 * <p>
 * If a type "isa" {@code Map} and is not <a href="#composite">composite</a>, it
 * is treated as above, though during unmarshalling it is assumed that the
 * destination {@code Map} instance into which unmarshalled entries may be put
 * is not {@code null}. If, on the other hand, the destination {@code Map} is
 * {@code null}, an exception is thrown.
 * <p>
 * Alternatively, if a {@code Map} type is part of a composite type property
 * definition and the property is annotated with {@link Polymorphic}, an
 * approach similar to that introduced in section <a href="#poly">Polymorphic
 * Types</a> is used. For these {@code Map} types, custom {@code Marshaller}s
 * are created which produce {@code Map<String, Object>}s during marshalling
 * which contain {@code "className"} and {@code "entries"} keys whose values
 * specify the name of the source map implementation and map entry data,
 * respectively. If the source map type is compatible with
 * {@code Map<String, V>} where {@code V} is a simple type, then
 * {@code "entries"} will reference the source map instance directly. If
 * {@code V} is not a simple type, then {@code "entries"} will reference a new
 * {@code LinkedHashMap<String, Object>} whose keys match the source map's keys,
 * and whose values are properly marshalled versions of the source map's values.
 * If source map key type is not {@code String}, then {@code "entries"} will
 * reference a new {@code List<Map<String, Object>>} where each
 * {@code Map<String, Object>} has keys {@code "key"} and {@code "value"} which
 * reference properly marshalled values. Note that the map class referenced by
 * {@code "className"} must have a no-args default constructor with which new
 * instances may be created during unmarshalling.
 * 
 * <h2>Hybrid Composite-Container Types</h2>
 * 
 * Entity and embeddable composite types may also be collection or map container
 * types. In these situations, the composite type must not have an existing
 * property named {@code "elements"} in case of a hybrid collection type, or
 * {@code "entries"} in the case of a hybrid map type, as these keys are used to
 * store container data during marshalling.
 * 
 * <h2 id="refs">Entity References</h2>
 * 
 * If a composite or container type references another Entity type, a
 * {@code Marshaller} is created which marshals only referenced Entity
 * instances' id values. During unmarshalling, a special {@link Proxy} Entity
 * instance is created which references the id value, along with a {@link Dao}
 * instance capable of fetching the associated Entity instance on first access
 * of a method not representing the id property accessor. This effectively
 * constitutes a "lazy-loading" behavior for entity references.
 * 
 * <h2>Enum Types</h2>
 * 
 * Custom Enum types are supported via marshalling to/from {@link Symbol}
 * instances.
 * 
 * <h2>Limitations</h2>
 * 
 * <ul>
 * <li>Hybrid Composite-Collection and Composite-Map types must not define
 * properties with names "elements" or "entries", respectively.</li>
 * <li>Cascading of persistence operations on referenced Entity instances is not
 * supported.</li>
 * <li>Eager fetching of referenced entities during unmarshalling not supported.
 * </li>
 * </ul>
 * 
 * @author hazen
 * @see <a
 * href="http://www.mongodb.org/display/DOCS/Java+Language+Center">MongoDB Java
 * Language Center</a>
 */
public class ReflectionMarshallerFactory implements EntityMarshallerFactory,
		MarshallerFactory {
	@Override
	public <T> Marshaller<T, Object> getMarshaller(Class<T> sourceClass) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <E> Marshaller<E, DBObject> getEntityMarshaller(Class<E> entityClass) {
		// TODO Auto-generated method stub
		return null;
	}
}
