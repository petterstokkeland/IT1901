package persistence;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import core.ExpenseHandlerImpl;
import java.io.IOException;

/**
 * Custom Gson TypeAdapter for the ExpenseHandlerImpl class. This adapter is used to customize the
 * serialization and deserialization process for instances of ExpenseHandlerImpl.
 */
public class ExpenseHandlerImplTypeAdapter extends TypeAdapter<ExpenseHandlerImpl> {

  private final TypeAdapter<JsonElement> elementAdapter = new Gson().getAdapter(JsonElement.class);

  /**
   * Serializes an instance of ExpenseHandlerImpl to JSON. This method delegates the serialization
   * process to the default Gson implementation, then post-processes the resulting JSON if
   * necessary.
   *
   * @param out The JsonWriter to write the JSON structure to.
   * @param value The ExpenseHandlerImpl instance to serialize.
   * @throws IOException If an error occurs writing to the JsonWriter.
   */
  @Override
  public void write(JsonWriter out, ExpenseHandlerImpl value) throws IOException {
    out.beginObject();
    out.name("expenses");
    elementAdapter.write(out, new Gson().toJsonTree(value.getAllExpenses()));
    out.endObject();
  }

  /**
   * Deserializes a JSON representation into an instance of ExpenseHandlerImpl. This method uses
   * Gson's internal logic to construct the object and then invokes the loadCategories method to
   * perform any necessary post-deserialization steps.
   *
   * @param in The JsonReader to read the JSON structure from.
   * @return An instance of ExpenseHandlerImpl populated with data from the JSON input.
   * @throws IOException If an error occurs reading from the JsonReader.
   */
  @Override
  public ExpenseHandlerImpl read(JsonReader in) throws IOException {
    JsonElement tree = elementAdapter.read(in);
    Gson gson = new Gson();
    ExpenseHandlerImpl handler = gson.fromJson(tree, ExpenseHandlerImpl.class);
    handler.loadCategories();
    return handler;
  }
}
