package tests;

import io.restassured.RestAssured;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class CharacterServiceTest {
    public static final String API_KEY = "$2a$10$CbVViIAfPIiG95DPiktzQemq9YSvQqH0H/JwdT0yrUCbnZvVll7iK";
    @BeforeClass
    //spusti sa raz pred vsetkymi testami
    public static void configuration(){
        RestAssured.baseURI = "https://www.potterapi.com/v1";
        RestAssured.basePath = "/characters";
        //base path je endpoint, ktory mozem pre klasy menit
    }

    @Test
    public void itShouldFindCharacterUsingHouseAndDeathEaterParam(){
        given().queryParam("key",API_KEY)
                .queryParam("house","Gryffindor")
                .queryParam("deathEater",true)
                .when().get()
                .then().body("[0].name",is("Peter Pettigrew"));
        //filtracia elementu house, najde vsetky house s hodnotou Gryffindor a sucasne najde vsetky deathEater
        //      s hodnotou true, nakoniec skontrolujem ci je v danom zozname meno Peter Pettigrew v prvom prvku(udava ho
        //      nulte pole pred name
    }

    @Test
    public void itShouldFindCharacterUsingIdRoute(){
        given().queryParam("key",API_KEY)
                //                .when().get("/5a12292a0f5ae10021650d7e")
                .pathParam("characterId","5a12292a0f5ae10021650d7e")
                .when().get("/{characterId}")
                .then().body("name",is("Harry Potter"));
        //do when mozem vlozit priamo id alebo zadefinovany parameter characterId pomocou pathParam (tymto sposobom je
        //  je v tom vacsi prehlad, v pripade aj ked pathParam vlozim do inej metody
    }
}
