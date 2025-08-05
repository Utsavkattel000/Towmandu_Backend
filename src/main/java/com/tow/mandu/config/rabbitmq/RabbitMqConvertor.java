package com.tow.mandu.config.rabbitmq;


import com.google.gson.Gson;
import com.tow.mandu.pojo.EmailPojo;

public class RabbitMqConvertor {

    private static final Gson gson = new Gson();

    public static String CONVERTOR(String sendTo, Object data, String userName, String subject) {
        EmailPojo emailPojo = new EmailPojo();
        emailPojo.setTo(sendTo);
//        emailPojo.setUserName(userName);
        emailPojo.setSubject(subject);
//        emailPojo.setData(String.valueOf(data));
        return gson.toJson(emailPojo);
    }
    /**
     * Converts the provided data into a JSON string wrapped in an {@link EmailPojo}, with the data object serialized to JSON.
     * This method is designed for complex objects that need to be fully serialized into JSON format before being wrapped.
     * Warning: The data object must be serializable by Gson. Types from the {@code java.time} package
     * (e.g., {@link java.time.LocalDate}, {@link java.time.LocalDateTime}, {@link java.time.Instant}) or other classes
     * with private fields in restricted Java modules (e.g., {@link java.util.UUID}, {@link java.net.InetAddress}) are not
     * supported by default and will cause a {@link com.google.gson.JsonIOException} with an
     * {@link java.lang.reflect.InaccessibleObjectException}. To avoid issues, use a DTO excluding such types or convert
     * them to String before using them.
     *
     * @param sendTo   the recipient email address
     * @param data     the data object to be serialized to JSON (e.g., a POJO or collection)
     * @param userName the username associated with the message
     * @param subject  the subject of the message
     * @return a JSON string representing the {@link EmailPojo} containing the JSON-serialized data
     */
    public static String CONVERTOR_WITH_JSON_DATA(String sendTo, Object data, String userName, String subject) {
        EmailPojo emailPojo = new EmailPojo();
        emailPojo.setTo(sendTo);
//        emailPojo.setUserName(userName);
        emailPojo.setSubject(subject);
//        emailPojo.setData(gson.toJson(data));
        return gson.toJson(emailPojo);
    }

}
