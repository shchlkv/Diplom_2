public class Api {

    //POST https://stellarburgers.nomoreparties.site/api/auth/register //регистрация/создание пользователя
    public static final String AUTH_REGISTER = "/api/auth/register";

    //POST https://stellarburgers.nomoreparties.site/api/auth/login авторизация пользователя
    public static final String LOGIN = "/api/auth/login";

    //DELETE https://stellarburgers.nomoreparties.site/api/auth/user удалить пользователя
    // GET https://stellarburgers.nomoreparties.site/api/auth/user данные о пользователе
    // PATCH https://stellarburgers.nomoreparties.site/api/auth/user обновление данных о пользователе
    // Для получения данных о пользователе необходимо передать серверу токен в поле authorization.
    public static final String AUTH_USER = "/api/auth/user";

    //GET https://stellarburgers.nomoreparties.site/api/ingredients данные об ингредиентах
    public static final String INGREDIENTS = "/api/ingredients";

   // POST https://stellarburgers.nomoreparties.site/api/orders создание заказа

    public static final String ORDER = "/api/orders";


    }
