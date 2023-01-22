import dto.Category;
import dto.Pet;
import dto.Tags;
import io.qameta.allure.Description;
import io.restassured.path.json.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import static requests.Requests.*;

public class PetStoreTest {

    public static String endpoint = "/pet";
    public static String endpointNotFoundPet = "/pet/1298798798687675765";
    public static String schemePath = "SchemaPetAdd.json";
    private static final Logger LOGGER = LogManager.getLogger();

    @Test(groups = "apiTestsPetShop",
            description = "Добавить нового Pet в магазин. Найти id нового питомца и его статус")
    @Description("Проверка статуса 200, валидация JsonSchema ответа")
    public void testAddNewPetInStore() {

        //Тело запроса - новый питомец
        Category category = Category.builder().id(12).name("KittyCat").build();
        Tags tags = Tags.builder().id(12).name("KittyCat").build();
        ArrayList<String> photoUrl = new ArrayList<>();
        photoUrl.add("url");
        ArrayList<Tags> tag = new ArrayList<>();
        tag.add(tags);
        Pet newPet = Pet.builder()
                .id(12)
                .category(category)
                .name("KittyCat")
                .photoUrls(photoUrl)
                .tags(tag)
                .status("available")
                .build();

        //Создать нового питомца в магазине и узнать его id
        JsonPath jsonPath = postJsonPath(endpoint, schemePath, newPet);

        //Проверить новый id питомца
        Integer idNewPet = jsonPath.get("id");
        Assert.assertEquals(idNewPet, 12);
        LOGGER.info("id нового петомца в магазине: " + idNewPet);

        //Проверить новый status питомца
        String status = jsonPath.get("status");
        Assert.assertEquals(status, "available");
        LOGGER.info("Status нового питомца в магазине: " + status);

        LOGGER.info("Отправлен POST запрос /pet, проверен код 200 и id нового питомца в магазине");
    }


    @Test(groups = "apiTestsPetShop",
            description = "Изменить имя существующего питомца в магазине. Проверить измененные данные")
    @Description("Проверка статуса 200, валидация JsonSchema ответа")
    public void testUpdateExistPetInStore() {

        //Тело запроса - с новым именем питомца
        Category category = Category.builder().id(12).name("NewName").build();
        Tags tags = Tags.builder().id(12).name("NewName").build();
        ArrayList<String> photoUrl = new ArrayList<>();
        photoUrl.add("url");
        ArrayList<Tags> tag = new ArrayList<>();
        tag.add(tags);
        Pet newPet = Pet.builder()
                .id(12)
                .category(category)
                .name("NewName")
                .photoUrls(photoUrl)
                .tags(tag)
                .status("available")
                .build();
        //Запрос POST с новым именем питомца
        JsonPath jsonPath = putJsonPath(endpoint, schemePath, newPet);

        //Проверить новое имя питомца
        String newName = jsonPath.get("name");
        Assert.assertEquals(newName, "NewName");
        LOGGER.info("Новое имя питомца в магазине: " + newName);

        LOGGER.info("Отправлен POST запрос /pet, проверен код 200 и id нового питомца в магазине");
    }

    @Test(groups = "apiTestsPetShop",
            description = "Проверка несуществующего id питомца в магазине Not Found Pet")
    @Description("Проверка статуса 404,message, type в ответе")
    public void testPetNotFoundInStore() {
        //Запрос POST с несуществующим id питомца в магазине
        JsonPath jsonPath = postNotFound(endpointNotFoundPet);

        //Проверка ответа Код статус и message в ответе
        Integer code = jsonPath.get("code");
        String type = jsonPath.get("type");
        String message = jsonPath.get("message");
        Assert.assertEquals(code, 404);
        Assert.assertEquals(type, "unknown");
        Assert.assertEquals(message, "not found");
        LOGGER.info(code + type + message);
        LOGGER.info("Отправлен POST запрос /pet{id}, проверен код 404 и message в ответе");
    }



}
