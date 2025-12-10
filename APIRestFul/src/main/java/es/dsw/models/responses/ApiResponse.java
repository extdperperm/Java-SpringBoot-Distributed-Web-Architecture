package es.dsw.models.responses;

/**********************************************************************************************
 * Clase que será utilizada como interfaz de respuesta a todas las peticiones de la API.      *
 *                                                                                            *
 * En esta Api se ha establecido el estandar de devolver un json estable que indique siempre  *
 * una bandera de error, mensaje de error y el dato objetivo.                                 * 
 **********************************************************************************************/
public class ApiResponse<T> {
    private boolean error;
    private String message;
    private T data;

    public ApiResponse() {}

    public ApiResponse(boolean error, String message, T data) {
        this.error = error;
        this.message = message;
        this.data = data;
    }

    public boolean isError() { return error; }
    
    public String getMessage() { return message; }
    
    public T getData() { return data; }

    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(false, "OK", data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(true, message, null);
    }
}