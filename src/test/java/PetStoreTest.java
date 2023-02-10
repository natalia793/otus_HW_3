import dto.Category;
import dto.Order;
import dto.Pet;
import dto.Tags;
import io.qameta.allure.Description;
import io.restassured.path.json.JsonPath;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.ArrayList;
import requests.Requests;



public class PetStoreTest extends Requests {
    public static String idPet = "12";
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
        Pet petNew = postNewPet(Endpoints.Pets.endpoints, SchemaPath.pet.schema, newPet);

        //Проверить новый id питомца
        Integer idNewPet = petNew.getId();
        Assert.assertEquals(idNewPet, 12);
        LOGGER.info("id нового петомца в магазине: " + idNewPet);

        //Проверить новый status питомца
        String status = petNew.getStatus();
        Assert.assertEquals(status, "available");

        //Проверить создание нового питомца
        JsonPath response = getDataInStore(Endpoints.Pets.endpoints + "/" + idNewPet, SchemaPath.pet.schema);
        Integer idResponsePet = response.get("id");
        Assert.assertEquals(idResponsePet, 12);
        String name = response.get("category.name");
        Assert.assertEquals(name, "KittyCat");
        LOGGER.info("Status нового питомца в магазине: " + status);
        LOGGER.info("name нового питомца в магазине: " + name);
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
        Pet pet = putNewData(Endpoints.Pets.endpoints, SchemaPath.pet.schema, newPet);

        //Проверить новое имя питомца
        String newName = pet.getName();
        Assert.assertEquals(newName, "NewName");
        LOGGER.info("Новое имя питомца в магазине: " + newName);

        //Проверить изменение имени питомца питомца с id=12 pet/{id}
        JsonPath response = getDataInStore(Endpoints.Pets.endpoints + "/" + idPet, SchemaPath.pet.schema);
        Integer idResponsePet = response.get("id");
        Assert.assertEquals(idResponsePet, 12);
        String name = response.get("category.name");
        Assert.assertEquals(name, "NewName");
        LOGGER.info("name нового питомца в магазине: " + name);
        LOGGER.info("Отправлен POST запрос /pet, проверен код 200 и id нового питомца в магазине");
    }

    @Test(groups = "apiTestsPetShop",
            description = "Проверка несуществующего id питомца в магазине Not Found Pet")
    @Description("Проверка статуса 404,message, type в ответе")
    public void testPetNotFoundInStore() {
        //Запрос POST с несуществующим id питомца в магазине
        JsonPath jsonPath = postPetNotFoundInStore(Endpoints.NotFoundPet.endpoints);

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

    @Test(groups = "apiTestsPetShop",
            description = "Добавить нового Pet в магазин. Найти id нового питомца и его статус")
    @Description("Проверка статуса 200, валидация JsonSchema ответа")
    public void testAddNewOrderInStore() {

        //Тело запроса - новый заказ питомца
        Order newOrder = Order.builder()
                .id(8)
                .petId(118)
                .quantity(1244)
                .shipDate("2023-02-07T17:18:17.729Z")
                .status("placed")
                .complete(true)
                .build();

        //Создать новый заказ питомца в магазине
        Order newGetOrder = postNewOrder(Endpoints.storeEndpoint.endpoints, SchemaPath.store.schema, newOrder);

        //Проверить новый заказ:
        Integer idNewOrder = newGetOrder.getId();
        Assert.assertEquals(idNewOrder, 8);
        Integer idPet = newGetOrder.getPetId();
        Assert.assertEquals(idPet, 118);
        String status = newGetOrder.getStatus();
        Assert.assertEquals(status, "placed");
        LOGGER.info("id нового заказа питомца в магазине: " + idNewOrder);
        LOGGER.info("Отправлен POST запрос /store/order, проверен код 200 и id, idPet и status заказа в ответе");


        //Проверить наличие нового заказа в магазине
        JsonPath response = getDataInStore(Endpoints.storeEndpoint.endpoints + "/" + idNewOrder, SchemaPath.store.schema);
        Integer idResponse = response.get("id");
        Assert.assertEquals(idResponse, 8);
        Integer petId = response.get("petId");
        Assert.assertEquals(petId, 118);
        Boolean complete = response.get("complete");
        Assert.assertEquals(complete, true);
        String statusOrder = response.get("status");
        Assert.assertEquals(statusOrder, "placed");
        LOGGER.info("Status нового заказа питомца в магазине: " + statusOrder);
        LOGGER.info("complete нового заказа питомца в магазине: " + complete);
        LOGGER.info("Отправлен GET запрос /store/order{idOrder}, проверен код 200 и id, status и complete");
    }


}
