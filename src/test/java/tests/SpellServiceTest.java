package tests;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class SpellServiceTest {
    public static final String API_KEY = "$2a$10$CbVViIAfPIiG95DPiktzQemq9YSvQqH0H/JwdT0yrUCbnZvVll7iK";

    @BeforeClass
    //spusti sa raz pred vsetkymi testami
    public static void configuration(){
        RestAssured.baseURI = "https://www.potterapi.com/v1";
        RestAssured.basePath = "/spells";
        //base path je endpoint, ktory mozem pre klasy menit
    }

    @Test
    public void itShouldReturnStatus200WhenKeyIsCorrect(){
        given().queryParam("key",API_KEY)
                .when().get()
                .then().statusCode(200)
                .and().contentType(ContentType.JSON);
        //kluc pre prihlasenie na web je v casti key=$2a$10$CbVViIAfPIiG95DPiktzQemq9YSvQqH0H/JwdT0yrUCbnZvVll7iK
        //metoda v danom prikaze je spells, potrebovali sme sa ale autentifikovat
        //ContentTypeJson kontroluje ci odpoved dostavam v Json formate
        //pomocou given zadam kluc vo public static final Stringu
    }
    @Test
    public void itShouldReturnStatus409WhenKeyIsMissing(){
        when().get()
                .then().statusCode(409)
                .and().contentType(ContentType.JSON)
                .and().body("error", is("Must pass API key for request"));
        //nasimulovanie statusu 409 (Conflict) v pripade ze nemam autorizaciu voci API(chyba kluc)
        //pomocou body kontrolujem text vratenej hlasky, is je z kniznice org.hamcrest.Matchers.is
    }

    @Test
    public void itShouldReturnStatus401WhenKeyIsInvalid(){
        given().queryParam("key","invalid")
                .when().get()
                .then().statusCode(401)
                .and().contentType(ContentType.JSON)
                .and().body("error", is("API Key Not Found"));;
        //test pre invalidny kluc
    }

    @Test
    public void itShouldContainSpellFieldForEachSpellInList(){
        Response response =
                given().queryParam("key",API_KEY)
                        .when().get();
        List<String> spells = response.getBody().path("spell");
        assertThat(spells,hasSize(greaterThan(10)));
        //pocet spellov je viac ako 10

        assertThat(spells,hasSize(151));
        //pocet spellov je presne 151

        spells.forEach(spell -> assertThat(spell,is(not(emptyString()))));
        //spelly neobsahuju prazdny string
    }
}
