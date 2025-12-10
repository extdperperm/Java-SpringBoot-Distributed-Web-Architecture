package es.dsw.models.responses;

//CLASE PARA TODAS LAS RESPUESTAS DE LA APIRest.
//Nota: El atributo data de tipo generico permitirá recoger cualquier objeto del tipo que sea.
//      De esta forma se reduce duplicidad de códgio (error, message).
public class ApiResponse<T> {
    private boolean error;
    private String message;
    private T data;

    public ApiResponse() {}

    public boolean isError() { return error; }
    public void setError(boolean error) { this.error = error; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
