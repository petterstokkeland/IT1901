package persistence;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import core.ExpenseHandler;
import core.ExpenseHandlerImpl;
import java.lang.reflect.Type;

/**
 * Adapter class responsible for the serialization and deserialization of {@link ExpenseHandlerImpl}
 * objects.
 */
public class ExpenseHandlerAdapter
    implements JsonSerializer<ExpenseHandler>, JsonDeserializer<ExpenseHandler> {

  /**
   * Serializes the given {@code ExpenseHandler} object into its equivalent JSON representation.
   *
   * @param src the source {@code ExpenseHandler} object to be serialized.
   * @param typeOfSrc the type of the source object.
   * @param context the serialization context.
   * @return a {@code JsonElement} representation of the source object.
   */
  @Override
  public JsonElement serialize(
      ExpenseHandler src, Type typeOfSrc, JsonSerializationContext context) {
    return context.serialize(src, ExpenseHandlerImpl.class);
  }

  /**
   * Deserializes the given JSON element into its equivalent {@code ExpenseHandler} object.
   *
   * @param json the JSON element being deserialized.
   * @param typeOfT the type of the desired object.
   * @param context the deserialization context.
   * @return an {@link ExpenseHandler} object deserialized from the given JSON element.
   * @throws JsonParseException if json is not in the expected format of {@code ExpenseHandlerImpl}.
   */
  @Override
  public ExpenseHandler deserialize(
      JsonElement json, Type typeOfT, JsonDeserializationContext context)
      throws JsonParseException {
    ExpenseHandlerImpl handler = context.deserialize(json, ExpenseHandlerImpl.class);
    handler.loadCategories();
    return handler;
  }
}
