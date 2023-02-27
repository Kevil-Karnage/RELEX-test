package generator;

public class JsonGenerator {
    public static String jsonShellFormat = "{\n%s\n}";
    public static String jsonParamFormat = "\"%s\": \"%s\",\n";

    public static String generateJsonResponse(String name, String value) {
        return String.format(
                jsonShellFormat,
                String.format(jsonParamFormat, name, value)
        );
    }

    public static String generateBadResponse(String value) {
        return generateJsonResponse("response", value);
    }
}
